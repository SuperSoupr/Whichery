package com.supersouper.whichery.common.entity.ai;

import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

import com.supersouper.whichery.api.demonology.IPossessedAnimal;
import com.supersouper.whichery.common.entity.demon.EntityDemonicShadow;

/**
 * Flies to a suitable attack target and attaches itself to it via the MC riding system.
 */
public class EntityAIFlyToTargetAndAttach extends EntityAIBase {

    private final EntityDemonicShadow demon;
    private final double speed;

    public EntityAIFlyToTargetAndAttach(EntityDemonicShadow demon, double speed) {
        this.demon = Objects.requireNonNull(demon);
        this.speed = speed;

        this.setMutexBits(0b11);
    }

    @Override
    public boolean shouldExecute() {
        return demon.ridingEntity == null && demon.getAttackTarget() != null
            && IPossessedAnimal.canBePossessed(demon.getAttackTarget());
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.demon.getAttackTarget();

        double targetCenterX = target.boundingBox.minX + (target.boundingBox.maxX - target.boundingBox.minX) / 2;
        double targetCenterY = target.boundingBox.minY + (target.boundingBox.maxY - target.boundingBox.minY) / 2;
        double targetCenterZ = target.boundingBox.minZ + (target.boundingBox.maxZ - target.boundingBox.minZ) / 2;

        double demonCenterX = this.demon.boundingBox.minX
            + (this.demon.boundingBox.maxX - this.demon.boundingBox.minX) / 2;
        double demonCenterY = this.demon.boundingBox.minY
            + (this.demon.boundingBox.maxY - this.demon.boundingBox.minY) / 2;
        double demonCenterZ = this.demon.boundingBox.minZ
            + (this.demon.boundingBox.maxZ - this.demon.boundingBox.minZ) / 2;

        double dx = targetCenterX - demonCenterX;
        double dy = targetCenterY - demonCenterY;
        double dz = targetCenterZ - demonCenterZ;

        double distance = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);

        if (distance > 0.15) { // don't make the threshold too small, otherwise the attaching may not work reliably
            this.demon.motionX = (dx / distance) * this.speed;
            this.demon.motionY = (dy / distance) * this.speed;
            this.demon.motionZ = (dz / distance) * this.speed;
        } else {
            attachToTarget(target);
        }
    }

    /*
     * Use the MC riding system to attach to the target. This comes with various advantages regarding position
     * synchronization etc., which we would have to implement otherwise.
     */
    private void attachToTarget(EntityLivingBase target) {
        this.demon.motionX = 0;
        this.demon.motionY = 0;
        this.demon.motionZ = 0;

        if (target.riddenByEntity != null) {
            target.riddenByEntity.mountEntity(null);
        }

        this.demon.mountEntity(target);
    }

    @Override
    public void resetTask() {
        this.demon.setAttackTarget(null);
    }
}
