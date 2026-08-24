package com.supersouper.whichery.api.rituals.matching;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockMatcher {

    boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes);

    ItemStack getItemStack();

    int[] itemStackHashCodes();

    void place(World world, int x, int y, int z);

    @SideOnly(Side.CLIENT)
    void drawIcon(Tessellator t, int x, int y, int w, int h);

}
