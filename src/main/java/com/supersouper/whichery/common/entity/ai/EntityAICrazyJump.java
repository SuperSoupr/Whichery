package com.supersouper.whichery.common.entity.ai;

import static com.supersouper.whichery.common.util.TimeUtils.minutesToTicks;

import net.minecraft.entity.EntityCreature;

public class EntityAICrazyJump extends EntityAICrazyBase {

    private final float chancePerTickInPercent;

    public EntityAICrazyJump(EntityCreature entity, float chancePerTick) {
        super(entity, 0.85f);

        this.chancePerTickInPercent = chancePerTick;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return this.rand.nextFloat() < this.chancePerTickInPercent;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        jumpAndScream();
    }

    @Override
    public void updateTask() {
        if (this.entity.onGround && this.entity.motionY <= 0) {
            jumpAndScream();
        }

        super.updateTask();
    }

    @Override
    protected int getRandomDurationTicks() {
        return minutesToTicks(0.5) + this.rand.nextInt(minutesToTicks(0.75));
    }

    private void jumpAndScream() {
        this.entity.getJumpHelper()
            .setJumping();
        screamIfPossible();

    }
}
