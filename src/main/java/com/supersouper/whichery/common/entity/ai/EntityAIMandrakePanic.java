package com.supersouper.whichery.common.entity.ai;

import com.supersouper.whichery.common.entity.EntityMandrake;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIMandrakePanic extends EntityAIBase {

    private final EntityMandrake mandrake;
    private final double speed;
    private double targetX, targetY, targetZ;

    public EntityAIMandrakePanic(EntityMandrake entity, double speed) {
        this.mandrake = entity;
        this.speed = speed;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (mandrake.getNavigator().noPath()) {
            return this.pickNewTarget();
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return !mandrake.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        mandrake.getNavigator().tryMoveToXYZ(targetX, targetY, targetZ, speed);
    }

    private boolean pickNewTarget() {
        Vec3 target = RandomPositionGenerator.findRandomTarget(mandrake, 10, 7);
        if (target == null) return false;
        targetX = target.xCoord;
        targetY = target.yCoord;
        targetZ = target.zCoord;
        return true;
    }
}
