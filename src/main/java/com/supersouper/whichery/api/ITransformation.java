package com.supersouper.whichery.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ITransformation {

    String getId();

    default void onTransform(EntityPlayer player) {}

    default void onUntransform(EntityPlayer player) {}

    default void onTick(EntityPlayer player) {}

    /** Default player: 0.6F width, 1.8F height, 1.62F eye height) */
    float getWidth();

    float getHeight();

    float getEyeHeight();

    @SideOnly(Side.CLIENT)
    EntityLivingBase getRenderEntity(EntityPlayer player);
}
