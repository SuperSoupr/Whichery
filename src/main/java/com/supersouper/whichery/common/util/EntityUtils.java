package com.supersouper.whichery.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class EntityUtils {

    public static boolean canEntitiesFightWithoutNearbyPlayersWatching(Entity entity, Entity entity2) {
        // Scan in a box with center between the two entities
        AxisAlignedBB scanArea = entity.boundingBox
            .getOffsetBoundingBox(entity2.posX - entity.posX, entity2.posY - entity.posY, entity2.posZ - entity.posZ)
            .expand(16, 16, 16);

        for (EntityPlayer player : entity.worldObj.getEntitiesWithinAABB(EntityPlayer.class, scanArea)) {
            if (canEntitiesFightWithoutPlayerWatching(player, entity, entity2)) {
                return false;
            }
        }

        return true;
    }

    public static boolean canEntitiesFightWithoutPlayerWatching(EntityPlayer player, Entity entity, Entity entity2) {
        return !isPlayerLookingAwayFromEntity(player, entity) || !isPlayerLookingAwayFromEntity(player, entity2);
    }

    public static boolean isPlayerLookingAwayFromEntity(EntityPlayer player, Entity entity) {
        Vec3 playerEyePos = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);

        Vec3 playerLookVec = player.getLook(1);

        Vec3 entityPos = Vec3.createVectorHelper(entity.posX, entity.posY + entity.height / 2.0, entity.posZ);

        Vec3 playerToEntityVec = Vec3.createVectorHelper(
            entityPos.xCoord - playerEyePos.xCoord,
            entityPos.yCoord - playerEyePos.yCoord,
            entityPos.zCoord - playerEyePos.zCoord);

        /*
         * Negative dot product means that playerLookVec and playerToEntityVec have an angle of more than 90 degrees,
         * meaning the player looks away.
         */
        return playerLookVec.dotProduct(playerToEntityVec) < 0;
    }

}
