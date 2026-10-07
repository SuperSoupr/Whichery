package com.supersouper.whichery.common.entity.ai;

import java.util.Objects;
import java.util.Random;

import net.minecraft.entity.ai.EntityAIBase;

import com.supersouper.whichery.common.entity.demon.EntityDemonicShadow;

public class EntityAIDemonicShadowIdle extends EntityAIBase {

    private static final int ASCEND_END_TICKS = 100;
    private static final int SEARCH_END_TICKS = 650;
    private static final int RETURN_END_TICKS = 750;
    private static final int PAUSE_END_TICKS = 770;

    private final EntityDemonicShadow demon;
    private final Random rand;

    private double originX;
    private double originY;
    private double originZ;

    private double angle;
    private double radius;

    private int durationTicks;

    private MovementPhase movementPhase;

    private enum MovementPhase {
        /**
         * Rise from spawn point start orbiting around it
         */
        ASCENDING,

        /**
         * Orbit faster and with a larger radius to increase chances of finding a host
         */
        SEARCHING,

        /**
         * No host was found, return towards spawn point
         */
        RETURNING,

        /**
         * Briefly pause at spawn point
         */
        PAUSE,

        /**
         * Move into the ground and despawn when in the void. Lore-wise the demon just fled the scene, but it is still
         * present in the overworld.
         */
        FLEE
    }

    public EntityAIDemonicShadowIdle(EntityDemonicShadow demon) {
        this.demon = Objects.requireNonNull(demon);
        this.rand = demon.getRNG();

        setMutexBits(0b11);
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public void startExecuting() {
        originX = demon.posX;
        originY = demon.posY;
        originZ = demon.posZ;

        angle = 2 * Math.PI * rand.nextDouble();
        radius = 1.5 + rand.nextDouble() * 1.5;

        durationTicks = 0;
        movementPhase = MovementPhase.ASCENDING;
    }

    @Override
    public void updateTask() {
        durationTicks++;

        updateMovementPhase();

        switch (movementPhase) {
            case ASCENDING:
            case SEARCHING:
                updateOrbitMovement();
                break;

            case RETURNING:
                updateReturningMovement();
                break;

            case PAUSE:
                updatePauseMovement();
                break;

            case FLEE:
                updateFleeMovement();
                break;
        }

        // TODO not compatible with CubicChunks
        if (movementPhase == MovementPhase.FLEE && demon.posY < -16) {
            demon.setDead();
        }
    }

    private void updateMovementPhase() {
        if (movementPhase == MovementPhase.FLEE) {
            return;
        }

        if (durationTicks < ASCEND_END_TICKS) {
            movementPhase = MovementPhase.ASCENDING;
        } else if (durationTicks < SEARCH_END_TICKS) {
            movementPhase = MovementPhase.SEARCHING;
        } else if (durationTicks < RETURN_END_TICKS) {
            movementPhase = MovementPhase.RETURNING;
        } else if (durationTicks < PAUSE_END_TICKS) {
            movementPhase = MovementPhase.PAUSE;
        } else {
            movementPhase = MovementPhase.FLEE;
            demon.hiss();
        }
    }

    private void updateOrbitMovement() {
        boolean isAscending = movementPhase == MovementPhase.ASCENDING;
        double orbitSpeed = isAscending ? 0.055 : 0.075;
        angle += orbitSpeed + Math.sin(durationTicks * 0.025) * 0.012;

        double newRadius;

        if (isAscending) {
            newRadius = 1.5 + durationTicks * 0.015;
        } else {
            final double progressInSearchPhase = (durationTicks - ASCEND_END_TICKS)
                / (double) (SEARCH_END_TICKS - ASCEND_END_TICKS);
            newRadius = 3 + progressInSearchPhase * 6 + Math.sin(durationTicks * 2 * Math.PI / 350) * 3;
        }

        // Smooth radius change
        radius += (newRadius - radius) * 0.035;

        // Position where the demon should move towards
        double targetX = originX + Math.cos(angle) * radius;
        double targetY;
        double targetZ = originZ + Math.sin(angle) * radius;

        if (isAscending) {
            double ascendProgress = durationTicks / (double) ASCEND_END_TICKS;
            targetY = originY + ascendProgress * 5;
        } else {
            targetY = originY + 5.0 + Math.sin(2 * Math.PI * durationTicks / 200);
        }

        double acceleration = isAscending ? 0.020 : 0.025;

        moveToward(targetX, targetY, targetZ, acceleration);

        demon.motionX += Math.sin(durationTicks * 0.13) * 0.0025;
        demon.motionY += Math.sin(durationTicks * 0.19) * 0.0020;
        demon.motionZ += Math.cos(durationTicks * 0.11) * 0.0025;

        dampen(0.93);
    }

    private void updateReturningMovement() {
        double progress = Math
            .min((durationTicks - SEARCH_END_TICKS) / (double) (RETURN_END_TICKS - SEARCH_END_TICKS), 1);

        /*
         * Scale the radius inverse to progress * progress here, so at first it shrinks slow,
         * then faster as it approaches the center
         */
        double returnRadius = 8 * (1 - progress * progress);

        angle += 0.075 * (1 - progress);

        // Position to move towards to
        double targetY = originY + 5 - progress * 5 + Math.sin(durationTicks * 0.09) * 0.5 * (1 - progress);
        double targetX = originX + Math.cos(angle) * returnRadius;
        double targetZ = originZ + Math.sin(angle) * returnRadius;

        double acceleration = 0.014 + progress * 0.025;

        moveToward(targetX, targetY, targetZ, acceleration);

        dampen(0.93 + progress * 0.055);

        if (progress > 0.75) {
            dampen(0.80);
        }
    }

    private void updatePauseMovement() {
        dampen(0.7);

        demon.motionX += (originX - demon.posX) * 0.01;
        demon.motionZ += (originZ - demon.posZ) * 0.01;
    }

    private void updateFleeMovement() {
        int fleeTicks = durationTicks - PAUSE_END_TICKS;
        double acceleration = 0.008 + fleeTicks * 0.004;

        demon.motionY -= acceleration;
        demon.motionX *= 0.94;
        demon.motionZ *= 0.94;

        if (demon.motionY < -2) {
            demon.motionY = -2;
        }
    }

    private void moveToward(double targetX, double targetY, double targetZ, double acceleration) {
        demon.motionX += (targetX - demon.posX) * acceleration;
        demon.motionY += (targetY - demon.posY) * acceleration;
        demon.motionZ += (targetZ - demon.posZ) * acceleration;
    }

    private void dampen(double amount) {
        demon.motionX *= amount;
        demon.motionY *= amount;
        demon.motionZ *= amount;
    }

    @Override
    public void resetTask() {
        durationTicks = 0;
    }
}
