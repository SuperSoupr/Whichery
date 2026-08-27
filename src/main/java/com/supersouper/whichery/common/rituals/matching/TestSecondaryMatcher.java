package com.supersouper.whichery.common.rituals.matching;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;

public class TestSecondaryMatcher implements ISecondaryMatcher {

    @Override
    public boolean match(EntityPlayer player, IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        return player == null || player.capabilities.isCreativeMode;
    }

    @Override
    public ItemStack[] getItemStacks() {
        return new ItemStack[0];
    }

    @Override
    public boolean shouldRunBeforeWorldMatch() {
        return true;
    }
}
