package com.supersouper.whichery.common.rituals.matching;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

public class ChalkItemSecondaryMatcher implements ISecondaryMatcher {

    private final ItemStack stack;
    private final boolean matchNBT;

    public ChalkItemSecondaryMatcher(ItemStack stack) {
        this.stack = stack.copy();
        this.matchNBT = stack.hasTagCompound();
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        for (TileEntity te : tes) {
            if (!(te instanceof ChalkTileEntity cte)) {
                continue;
            }

            if (WhicheryUtils.matchIngredient(stack, cte.getStackInSlot(0), matchNBT)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack[] getItemStacks() {
        return new ItemStack[0];
    }

    @Override
    public int getRequiredMatchCount() {
        return 1;
    }
}
