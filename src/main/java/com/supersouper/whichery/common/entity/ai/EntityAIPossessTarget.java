package com.supersouper.whichery.common.entity.ai;

import static com.supersouper.whichery.api.demonology.PossessedAnimalRegistry.createPossessedEntityFromHost;

import java.util.Objects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

import com.supersouper.whichery.api.demonology.IPossessedAnimal;
import com.supersouper.whichery.common.entity.demon.EntityDemonicShadow;
import com.supersouper.whichery.mixins.accessors.EntityLivingBaseAccessor;

/**
 * Possesses the entity the demon is attached to.
 */
public class EntityAIPossessTarget extends EntityAIBase {

    public static final int SHRINK_START_TIME_TICKS = 20;
    public static final int POSSESS_TIME_TICKS = 80;

    private final EntityDemonicShadow demon;

    private int durationTicks;

    public EntityAIPossessTarget(EntityDemonicShadow demon) {
        this.demon = Objects.requireNonNull(demon);

        this.setMutexBits(0b11);
    }

    @Override
    public boolean shouldExecute() {
        return demon.ridingEntity != null && demon.ridingEntity instanceof EntityLivingBase entityLivingBase
            && IPossessedAnimal.canBePossessedByDemonicShadow(entityLivingBase, demon);
    }

    @Override
    public void startExecuting() {
        this.demon.setShouldShrink(false);
        this.durationTicks = 0;
    }

    @Override
    public void updateTask() {
        this.durationTicks++;

        if (this.durationTicks == SHRINK_START_TIME_TICKS) {
            demon.setShouldShrink(true);
        }

        if (this.durationTicks >= POSSESS_TIME_TICKS) {
            possess((EntityLivingBase) demon.ridingEntity);
        }
    }

    private void possess(EntityLivingBase target) {
        Entity possessedEntity = createPossessedEntityFromHost(target);

        EntityLivingBaseAccessor accessor = (EntityLivingBaseAccessor) target;
        target.playSound(accessor.getHurtSoundMixin(), accessor.getSoundVolumeMixin(), accessor.getSoundPitchMixin());

        this.demon.worldObj.spawnEntityInWorld(possessedEntity);
        this.demon.setDead();
        target.setDead();
    }

    @Override
    public void resetTask() {
        this.durationTicks = 0;

        if (!this.demon.isDead) {
            /*
             * Don't set those when dead, as than can cause a package race condition where the death package arrives
             * after the mount change, which causes the demon to flicker for a split second at full size again.
             */
            this.demon.setShouldShrink(false);

            if (this.demon.ridingEntity != null) {
                this.demon.mountEntity(null);
            }
        }
    }
}
