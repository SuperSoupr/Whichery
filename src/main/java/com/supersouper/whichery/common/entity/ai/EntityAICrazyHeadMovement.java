package com.supersouper.whichery.common.entity.ai;

import static com.supersouper.whichery.common.util.TimeUtils.minutesToTicks;

import net.minecraft.entity.EntityCreature;

public class EntityAICrazyHeadMovement extends EntityAICrazyBase {

    private final float chancePerTickInPercent;

    private int yOffset;
    private int nodIntervalTicks;

    public EntityAICrazyHeadMovement(EntityCreature entity, float chancePerTickInPercent) {
        super(entity, 0.2f);
        this.chancePerTickInPercent = chancePerTickInPercent;
        this.setMutexBits(0b11); // Exclude other wander (01) and look (10) tasks
    }

    @Override
    public boolean shouldExecute() {
        return this.rand.nextFloat() < this.chancePerTickInPercent;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();

        this.yOffset = this.rand.nextBoolean() ? 1 : 0;
        this.nodIntervalTicks = 10 + rand.nextInt(15);
    }

    @Override
    public void updateTask() {
        this.entity.getLookHelper()
            .setLookPosition(
                this.entity.posX,
                this.entity.posY + this.yOffset,
                this.entity.posZ,
                10.0F,
                this.entity.getVerticalFaceSpeed());

        if (durationTicks % nodIntervalTicks == 0) {
            screamIfPossible();
            this.yOffset = this.yOffset == 0 ? 1 : 0;
        }

        super.updateTask();
    }

    @Override
    protected int getRandomDurationTicks() {
        return minutesToTicks(0.5) + this.rand.nextInt(minutesToTicks(1));
    }
}
