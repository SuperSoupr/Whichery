package com.supersouper.whichery.api.rituals.matching;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBlockMatcher {

    boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes);

    ItemStack getItemStack();

    int itemStackHashCode();

    void place(World world, int x, int y, int z);

}
