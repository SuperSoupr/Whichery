package com.supersouper.whichery.common.entity.demon;

import static com.supersouper.whichery.common.entity.ai.EntityAIPossessTarget.POSSESS_TIME_TICKS;
import static com.supersouper.whichery.common.entity.ai.EntityAIPossessTarget.SHRINK_START_TIME_TICKS;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.supersouper.whichery.api.demonology.IDemon;
import com.supersouper.whichery.common.entity.ai.EntityAIDemonicShadowIdle;
import com.supersouper.whichery.common.entity.ai.EntityAIFlyToTargetAndAttach;
import com.supersouper.whichery.common.entity.ai.EntityAIPossessTarget;
import com.supersouper.whichery.common.entity.ai.EntityAIRandomTargetToPossess;

public class EntityDemonicShadow extends EntityFlying implements IDemon {

    private static final int SHOULD_SHRINK_DATA_WATCHER_ID = 25;

    /**
     * Client-only.
     */
    private int shrinkTicksClient = -1;
    private boolean hasPlayedSpawnSound = false;

    public EntityDemonicShadow(World world) {
        super(world);

        this.setSize(1, 1.5f);
        this.noClip = true;
        this.isImmuneToFire = true; // So it does not render a burning animation

        setupAI();
    }

    private void setupAI() {
        tasks.addTask(0, new EntityAIPossessTarget(this));
        tasks.addTask(1, new EntityAIFlyToTargetAndAttach(this, 0.3));
        tasks.addTask(2, new EntityAIDemonicShadowIdle(this));

        targetTasks.addTask(0, new EntityAIRandomTargetToPossess(this, 16));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(SHOULD_SHRINK_DATA_WATCHER_ID, 0);
    }

    public void setShouldShrink(boolean shouldShrink) {
        this.dataWatcher.updateObject(SHOULD_SHRINK_DATA_WATCHER_ID, shouldShrink ? 1 : 0);
    }

    public boolean getShouldShrink() {
        return this.dataWatcher.getWatchableObjectInt(SHOULD_SHRINK_DATA_WATCHER_ID) == 1;
    }

    /**
     * Client-only.
     */
    public float getShrinkFactor() {
        if (this.isDead || (getShouldShrink() && this.ridingEntity == null)) {
            return 1;
        }

        if (getShouldShrink()) {
            float factor = shrinkTicksClient / ((float) POSSESS_TIME_TICKS - SHRINK_START_TIME_TICKS);

            if (factor > 1) {
                factor = 1;
            }

            return factor;
        }

        return 0;
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
        // Do nothing
    }

    @Override
    public double getYOffset() {
        if (this.ridingEntity != null || getShouldShrink()) {
            return (this.ridingEntity.height / 2) - this.height / 2;
        }
        return super.getYOffset();
    }

    public void hiss() {
        float pitch = 0.4F + this.rand.nextFloat() * 0.1F;

        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.wither.shoot", 0.75f, pitch);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote && !this.hasPlayedSpawnSound) {
            hiss();

            this.hasPlayedSpawnSound = true;
        }

        if (worldObj.isRemote) {
            if (getShouldShrink()) {
                ++shrinkTicksClient;
            } else {
                shrinkTicksClient = 0;
            }

            double speedX = this.motionX;
            double speedY = this.motionY;
            double speedZ = this.motionZ;
            double speedSq = speedX * speedX + speedY * speedY + speedZ * speedZ;

            int particleCount = speedSq > 0.0025 ? 6 : 3;

            for (int i = 0; i < particleCount; ++i) {
                double px = this.posX + (this.rand.nextDouble() - 0.5) * this.width * 0.4;
                double py = this.posY + this.rand.nextDouble() * this.height;
                double pz = this.posZ + (this.rand.nextDouble() - 0.5) * this.width * 0.4;

                double red = 0.001;
                double green = 0.001;
                double blue = 0.001;

                this.worldObj.spawnParticle("mobSpellAmbient", px, py, pz, red, green, blue);
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);

        tagCompound.setBoolean("PlayedSpawnSound", this.hasPlayedSpawnSound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);

        this.hasPlayedSpawnSound = tagCompound.getBoolean("PlayedSpawnSound");
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1; // Render in translucency pass
    }
}
