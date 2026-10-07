package com.supersouper.whichery.common.entity.ai;

import static com.supersouper.whichery.common.util.TimeUtils.minutesToTicks;

import net.minecraft.entity.EntityCreature;

public class EntityAICrazyStare extends EntityAICrazyBase {

    private final float chancePerTickInPercent;

    private int yOffset;

    public EntityAICrazyStare(EntityCreature entity, float chancePerTickInPercent) {
        super(entity, 0.05f);
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

        this.yOffset = this.rand.nextBoolean() ? 1 : 0; // Either stare up or down
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

        if (durationTicks % 100 == 0) {
            screamIfPossible();
        }

        super.updateTask();
    }

    @Override
    protected int getRandomDurationTicks() {
        return minutesToTicks(2) + this.rand.nextInt(minutesToTicks(2));
    }
}
