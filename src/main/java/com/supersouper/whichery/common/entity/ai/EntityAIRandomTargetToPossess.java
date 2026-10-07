package com.supersouper.whichery.common.entity.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;

import com.supersouper.whichery.api.demonology.IPossessedAnimal;
import com.supersouper.whichery.common.entity.demon.EntityDemonicShadow;

public class EntityAIRandomTargetToPossess extends EntityAIBase {

    private final EntityDemonicShadow demon;
    private final int targetRange;

    public EntityAIRandomTargetToPossess(EntityDemonicShadow demon, int targetRange) {
        Objects.requireNonNull(demon);

        this.demon = demon;
        this.targetRange = targetRange;

        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.demon.getAttackTarget() != null || this.demon.ticksExisted % 5 != 0) {
            return false;
        }

        AxisAlignedBB searchArea = this.demon.boundingBox.expand(targetRange, targetRange, targetRange);

        List<EntityLivingBase> hosts = new ArrayList<>(
            this.demon.worldObj.selectEntitiesWithinAABB(
                EntityLivingBase.class,
                searchArea,
                entity -> entity instanceof EntityLivingBase entityLivingBase
                    && IPossessedAnimal.canBePossessed(entityLivingBase)));
        Collections.shuffle(hosts);

        if (!hosts.isEmpty()) {
            this.demon.setAttackTarget(hosts.get(0));
            return true;
        }

        return false;
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase target = this.demon.getAttackTarget();

        if (target != null && target.isEntityAlive()) {
            return this.demon.getDistanceSqToEntity(target) <= Math.pow(this.targetRange, 2);
        }

        return false;
    }

    @Override
    public void resetTask() {
        demon.setAttackTarget(null);
    }
}
