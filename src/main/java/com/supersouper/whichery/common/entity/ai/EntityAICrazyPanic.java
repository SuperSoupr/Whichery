package com.supersouper.whichery.common.entity.ai;

import static com.supersouper.whichery.common.util.TimeUtils.minutesToTicks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

/**
 * Panic with no apparent reason.
 */
public class EntityAICrazyPanic extends EntityAICrazyBase {

    private final float chancePerTickInPercent;
    private final double speed;

    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public EntityAICrazyPanic(EntityCreature entity, float chancePerTickInPercent, double speed) {
        super(entity, 0.2f);

        this.chancePerTickInPercent = chancePerTickInPercent;
        this.speed = speed;

        setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.rand.nextFloat() > this.chancePerTickInPercent) {
            return false;
        }

        return tryToFindRandomPosition();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();

        this.entity.getNavigator()
            .tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        if (super.continueExecuting()) {
            if (!this.entity.getNavigator()
                .noPath()) {
                return true;
            } else if (tryToFindRandomPosition()) {
                this.entity.getNavigator()
                    .tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateTask() {
        if (durationTicks % 30 == 0) {
            screamIfPossible();
        }

        super.updateTask();
    }

    private boolean tryToFindRandomPosition() {
        Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 5, 4);

        if (vec3 == null) {
            return false;
        } else {
            this.randPosX = vec3.xCoord;
            this.randPosY = vec3.yCoord;
            this.randPosZ = vec3.zCoord;
            return true;
        }
    }

    @Override
    protected int getRandomDurationTicks() {
        return minutesToTicks(0.5) + rand.nextInt(minutesToTicks(0.5));
    }
}
