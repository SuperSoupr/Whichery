package com.supersouper.whichery.common.entity.ai;

import static com.supersouper.whichery.common.util.TimeUtils.minutesToTicks;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

import com.google.common.collect.ImmutableList;
import com.supersouper.whichery.common.entity.demon.IDemon;

/*
 * This is a "composite" AI task that delegates to other tasks.
 */
public class EntityAIUnrestAroundDemon extends EntityAIBase {

    public static final int DEMON_SCAN_DISTANCE = 12;

    private final EntityCreature entity;
    private final Random rand;

    private EntityAIBase currentTask;

    private final List<EntityAIBase> allTasks;

    public EntityAIUnrestAroundDemon(EntityCreature entity, double panicSpeed) {
        this.entity = Objects.requireNonNull(entity);
        this.rand = entity.getRNG();

        EntityAICrazyStare weirdStareTask = new EntityAICrazyStare(entity, 1f);
        EntityAICrazyJump jumpAndScreamTask = new EntityAICrazyJump(entity, 1f);
        EntityAICrazyPanic panicTask = new EntityAICrazyPanic(entity, 1f, panicSpeed);
        EntityAICrazyHeadMovement headMovementTask = new EntityAICrazyHeadMovement(entity, 1f);

        allTasks = ImmutableList.of(weirdStareTask, jumpAndScreamTask, panicTask, headMovementTask);

        // Only execute this task if any contained task could execute (as a random one is chosen)
        setMutexBits(
            allTasks.stream()
                .mapToInt(EntityAIBase::getMutexBits)
                .reduce(0, (mutex1, mutex2) -> mutex1 | mutex2));
    }

    @Override
    public boolean shouldExecute() {
        if (this.rand.nextInt(minutesToTicks(4)) == 0 && isDemonNearby()) {
            currentTask = allTasks.get(rand.nextInt(allTasks.size()));

            return currentTask.shouldExecute();
        }

        return false;
    }

    @Override
    public void startExecuting() {
        if (currentTask != null) {
            currentTask.startExecuting();
        }
    }

    @Override
    public boolean continueExecuting() {
        if (currentTask != null) {
            return currentTask.continueExecuting();
        }

        return false;
    }

    @Override
    public void updateTask() {
        if (currentTask != null) {
            currentTask.updateTask();
        }
    }

    @Override
    public void resetTask() {
        if (currentTask != null) {
            currentTask.resetTask();
            currentTask = null;
        }
    }

    private boolean isDemonNearby() {
        return !entity.worldObj
            .selectEntitiesWithinAABB(
                IDemon.class,
                this.entity.boundingBox.expand(DEMON_SCAN_DISTANCE, DEMON_SCAN_DISTANCE, DEMON_SCAN_DISTANCE),
                null)
            .isEmpty();
    }
}
