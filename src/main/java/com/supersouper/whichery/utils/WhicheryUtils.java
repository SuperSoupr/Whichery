package com.supersouper.whichery.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.oredict.OreDictionary;

public class WhicheryUtils {

    public static MovingObjectPosition rayTraceLook(EntityPlayerMP player) {
        return rayTraceLook(player, player.theItemInWorldManager.getBlockReachDistance(), false);
    }

    public static MovingObjectPosition rayTraceLook(EntityPlayerMP player, double reach, boolean hitLiquids) {
        Vec3 eyes = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 look = player.getLookVec();
        Vec3 end = eyes.addVector(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach);
        return player.worldObj.func_147447_a(eyes, end, hitLiquids, !hitLiquids, false);
    }

    public static boolean matchIngredient(ItemStack target, ItemStack input, boolean matchNBT) {
        if (target == null || input == null) return target == input;
        if (!OreDictionary.itemMatches(target, input, false)) return false;
        return !matchNBT || ItemStack.areItemStackTagsEqual(target, input);
    }

    public static boolean[] byteArrayToBooleanArray(byte[] byteArray) {
        boolean[] boolArray = new boolean[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            boolArray[i] = byteArray[i] != 0;
        }
        return boolArray;
    }

    public static byte[] booleanArrayToByteArray(boolean[] boolArray) {
        byte[] byteArray = new byte[boolArray.length];
        for (int i = 0; i < boolArray.length; i++) {
            byteArray[i] = (byte) (boolArray[i] ? 1 : 0);
        }
        return byteArray;
    }

}
