package com.supersouper.whichery.common.entity.ai;

import java.util.Objects;
import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

import com.supersouper.whichery.mixins.accessors.EntityLivingBaseAccessor;

/**
 * Base class for "crazy" behavior. Crazy behavior means:
 * <ul>
 * <li>It is only for a limited duration</li>
 * <li>The crazy entity may scream during it</li>
 * </ul>
 */
public abstract class EntityAICrazyBase extends EntityAIBase {

    protected final EntityCreature entity;
    protected final Random rand;
    private final float screamProbabilityPerExecutionPercent;

    protected int durationTicks;
    protected boolean isScreaming;

    public EntityAICrazyBase(EntityCreature entity, float screamProbabilityPerExecutionPercent) {
        this.entity = Objects.requireNonNull(entity);
        this.rand = this.entity.getRNG();
        this.screamProbabilityPerExecutionPercent = screamProbabilityPerExecutionPercent;
    }

    @Override
    public void startExecuting() {
        isScreaming = rand.nextFloat() < this.screamProbabilityPerExecutionPercent;
        durationTicks = getRandomDurationTicks();
    }

    @Override
    public boolean continueExecuting() {
        return entity.isEntityAlive() && durationTicks > 0;
    }

    @Override
    public void updateTask() {
        --durationTicks;
    }

    protected abstract int getRandomDurationTicks();

    protected void screamIfPossible() {
        if (isScreaming) {
            EntityLivingBaseAccessor accessor = (EntityLivingBaseAccessor) entity;
            this.entity
                .playSound(accessor.getHurtSoundMixin(), accessor.getSoundVolumeMixin(), accessor.getSoundPitchMixin());
        }
    }

}
