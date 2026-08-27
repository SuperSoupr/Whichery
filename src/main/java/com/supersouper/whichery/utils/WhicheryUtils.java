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

    public static double lerpD(double cur, double target, double speed) {
        return cur + (target - cur) * speed;
    }

    public static float lerpF(float cur, float target, float speed) {
        return cur + (target - cur) * speed;
    }

    public static void rotate(int[] point, int pivotX, int pivotZ) {
        int dx = point[0] - pivotX;
        int dz = point[1] - pivotZ;
        point[0] = pivotX - dz;
        point[1] = pivotZ + dx;
    }

    public static void rotated(double[] point, double pivotX, double pivotZ) {
        double dx = point[0] - pivotX;
        double dz = point[1] - pivotZ;
        point[0] = pivotX - dz;
        point[1] = pivotZ + dx;
    }
}
