package com.supersouper.whichery.common.entity.demon;

import static com.supersouper.whichery.common.util.TimeUtils.minutesToTicks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.supersouper.whichery.common.entity.ai.EntityAICancelAttackWhenPlayerWatches;
import com.supersouper.whichery.common.entity.ai.EntityAIStareBackAtAwarePlayer;
import com.supersouper.whichery.common.util.EntityUtils;

public class EntityPossessedChicken extends EntityChicken implements IPossessedAnimal {

    private static final int STARING_AT_AWARE_PLAYER_DATA_WATCHER_KEY = 15;

    public EntityPossessedChicken(World world) {
        super(world);

        setupAi();
    }

    protected void setupAi() {
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();

        this.tasks.addTask(0, new EntityAISwimming(this));

        this.tasks.addTask(1, new EntityAIStareBackAtAwarePlayer<>(this));

        // Sneaky attack behavior
        this.tasks.addTask(2, new EntityAICancelAttackWhenPlayerWatches(this));
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityChicken.class, 1.4, false));

        // "Normal behavior"
        this.tasks.addTask(5, new EntityAIPanic(this, 1.4));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0));
        this.tasks.addTask(7, new EntityAITempt(this, 1.0, Items.wheat_seeds, false));
        this.tasks.addTask(8, new EntityAIFollowParent(this, 1.1));

        this.tasks.addTask(9, new EntityAIWander(this, 1.0));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(11, new EntityAILookIdle(this));

        this.targetTasks.addTask(
            0,
            new EntityAINearestAttackableTarget(this, EntityChicken.class, minutesToTicks(20), false, true, entity -> {
                if (entity instanceof EntityChicken && !(entity instanceof EntityPossessedChicken)) {
                    return EntityUtils.canEntitiesFightWithoutNearbyPlayersWatching(this, entity);
                }

                return false;
            }));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(STARING_AT_AWARE_PLAYER_DATA_WATCHER_KEY, 0);
    }

    @Override
    public void setStaringAtAwarePlayer(boolean value) {
        this.dataWatcher.updateObject(STARING_AT_AWARE_PLAYER_DATA_WATCHER_KEY, value ? 1 : 0);
    }

    @Override
    public boolean isStaringAtAwarePlayer() {
        return this.dataWatcher.getWatchableObjectInt(STARING_AT_AWARE_PLAYER_DATA_WATCHER_KEY) == 1;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityToAttack) {
        return entityToAttack.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
    }

    @Override
    public EntityLiving createUnpossessedDummyHostEntity() {
        EntityChicken chicken = new EntityChicken(worldObj);
        chicken.setHealth(this.getHealth());
        chicken.setGrowingAge(this.getGrowingAge());

        return chicken;
    }

    public static EntityPossessedChicken createPossessedChicken(EntityChicken chicken) {
        EntityPossessedChicken possessedChicken = new EntityPossessedChicken(chicken.worldObj);
        possessedChicken
            .setLocationAndAngles(chicken.posX, chicken.posY, chicken.posZ, chicken.rotationYaw, chicken.rotationPitch);

        possessedChicken.setHealth(chicken.getHealth());
        possessedChicken.setGrowingAge(chicken.getGrowingAge());

        return possessedChicken;
    }

    /*
     * Make sure that the silhouette is rendered in render pass 1, otherwise there are visual problems if the silhouette
     * is rendered before water.
     */
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }
}
