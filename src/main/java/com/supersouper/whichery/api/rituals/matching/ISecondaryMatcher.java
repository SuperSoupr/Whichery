package com.supersouper.whichery.api.rituals.matching;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public interface ISecondaryMatcher {

    boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes);

    ItemStack[] getItemStacks();

    int getRequiredMatchCount();
}
