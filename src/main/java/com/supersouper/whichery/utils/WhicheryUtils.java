package com.supersouper.whichery.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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

    public static String[] StringNBTTagListToArray(NBTTagList tagList) {
        if (tagList == null) return null;
        if (tagList.func_150303_d() != 8) {
            throw new IllegalArgumentException(
                "Incorrect tag list type. Expected 8 but got " + tagList.func_150303_d());
        }
        String[] result = new String[tagList.tagCount()];
        for (int i = 0; i < tagList.tagCount(); i++) {
            result[i] = tagList.getStringTagAt(i);
        }
        return result;
    }

    public static NBTTagList StringArrayToNBTTagList(String[] array) {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < array.length; i++) {
            tagList.appendTag(new NBTTagString(array[i]));
        }
        return tagList;
    }
}
