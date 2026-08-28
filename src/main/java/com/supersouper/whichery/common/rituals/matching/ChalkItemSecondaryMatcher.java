package com.supersouper.whichery.common.rituals.matching;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;
import com.supersouper.whichery.common.rituals.effects.ChalkItemConsumeEffect;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

public class ChalkItemSecondaryMatcher implements ISecondaryMatcher {

    public final ItemStack stack;
    public final ItemStack resultStack;
    public final boolean onlyPlaceOnComplete;
    public final boolean matchNBT;

    public ChalkItemSecondaryMatcher(ItemStack stack) {
        this(stack, null, false);
    }

    public ChalkItemSecondaryMatcher(ItemStack stack, ItemStack result) {
        this(stack, result, false);
    }

    public ChalkItemSecondaryMatcher(ItemStack stack, ItemStack result, boolean onlyPlaceOnComplete) {
        this.stack = stack.copy();
        this.resultStack = result != null ? result.copy() : null;
        this.onlyPlaceOnComplete = onlyPlaceOnComplete;
        this.matchNBT = stack.hasTagCompound();
    }

    @Override
    public void onRitualConstructed(Ritual ritual) {
        boolean has = false;
        for (Class<?> clazz : ritual.effectClasses) {
            if (clazz == ChalkItemConsumeEffect.class) {
                has = true;
                break;
            }
        }
        if (!has) {
            if (resultStack == null) {
                Whichery.LOG.warn("Ritual '{}' has an item matcher but no ChalkItemConsumeEffect effect", ritual.name);
            } else {
                throw new IllegalArgumentException(
                    "Ritual '" + ritual.name
                        + "' has an item matcher with a result item but no ChalkItemConsumeEffect effect");
            }
        }
    }

    @Override
    public boolean match(EntityPlayer player, IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        for (TileEntity te : tes) {
            if (!(te instanceof ChalkRuneTileEntity cte)) {
                continue;
            }

            if (WhicheryUtils.matchIngredient(stack, cte.getStackInSlot(1), matchNBT)
                && cte.getStackInSlot(0) == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack[] getItemStacks() {
        return new ItemStack[] { stack };
    }
}
