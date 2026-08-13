package com.supersouper.whichery.utils;

import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WhicheryUtils {

    public static final Random rand = new Random();

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

    public static int[] byteArrayToIntArray(byte[] byteArray) {
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i] = byteArray[i];
        }
        return intArray;
    }

    public static byte[] intArrayToByteArray(int[] intArray) {
        byte[] byteArray = new byte[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            byteArray[i] = (byte) intArray[i];
        }
        return byteArray;
    }

    @SideOnly(Side.CLIENT)
    public static boolean resourceExists(ResourceLocation location) {
        try {
            IResource resource = Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(location);

            return resource != null;
        } catch (IOException e) {
            return false;
        }
    }

}
