package com.supersouper.whichery.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import com.supersouper.whichery.common.entity.demon.IPossessedEntity;
import com.supersouper.whichery.common.entity.extendedproperties.DemonologyProperty;

public class EntityAIStareBackAtAwarePlayer<T extends EntityCreature & IPossessedEntity> extends EntityAIBase {

    public static final int MAX_STARE_DISTANCE = 16;

    private final T entity;
    private EntityPlayer awarePlayer;

    public EntityAIStareBackAtAwarePlayer(T entity) {
        this.entity = entity;
        this.setMutexBits(0b11);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.ticksExisted % 20 == 0) {
            AxisAlignedBB scanArea = this.entity.boundingBox
                .expand(MAX_STARE_DISTANCE, MAX_STARE_DISTANCE, MAX_STARE_DISTANCE);

            for (EntityPlayer player : this.entity.worldObj.getEntitiesWithinAABB(EntityPlayer.class, scanArea)) {
                DemonologyProperty property = DemonologyProperty.get(player);
                if (property != null && property.isCanSeeDemonsPossessingHosts() && isPlayerLookingAtEntity(player)) {
                    awarePlayer = player;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPlayerLookingAtEntity(EntityPlayer player) {
        Vec3 playerLookVec = player.getLookVec()
            .normalize();
        Vec3 playerToTargetVec = Vec3.createVectorHelper(
            this.entity.posX - player.posX,
            this.entity.posY + this.entity.height / 2.0 - (player.posY + player.getEyeHeight()),
            this.entity.posZ - player.posZ);
        double playerToTargetDistance = playerToTargetVec.lengthVector();
        playerToTargetVec = playerToTargetVec.normalize();
        double d1 = playerLookVec.dotProduct(playerToTargetVec);
        return d1 > 1.0D - 0.025D / playerToTargetDistance && player.canEntityBeSeen(this.entity);
    }

    @Override
    public void startExecuting() {
        entity.setStaringAtAwarePlayer(true);
    }

    @Override
    public boolean continueExecuting() {
        if (awarePlayer.isEntityAlive()) {
            DemonologyProperty property = DemonologyProperty.get(awarePlayer);
            if (property != null && property.isCanSeeDemonsPossessingHosts()) {
                return this.entity.getDistanceSqToEntity(this.awarePlayer) <= MAX_STARE_DISTANCE * MAX_STARE_DISTANCE;
            }
        }

        return false;
    }

    @Override
    public void resetTask() {
        this.awarePlayer = null;
        this.entity.setStaringAtAwarePlayer(false);
    }

    @Override
    public void updateTask() {
        this.entity.getLookHelper()
            .setLookPosition(
                this.awarePlayer.posX,
                this.awarePlayer.posY + this.awarePlayer.getEyeHeight(),
                this.awarePlayer.posZ,
                10.0F,
                this.entity.getVerticalFaceSpeed());
    }
}
