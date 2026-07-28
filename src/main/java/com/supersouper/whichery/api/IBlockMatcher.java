package com.supersouper.whichery.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBlockMatcher {

    boolean match(IBlockAccess world, int x, int y, int z);

    ItemStack toItemStack();

    int itemStackHashCode();

    void place(World world, int x, int y, int z);

}
