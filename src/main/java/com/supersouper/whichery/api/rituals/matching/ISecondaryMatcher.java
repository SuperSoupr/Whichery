package com.supersouper.whichery.api.rituals.matching;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.supersouper.whichery.api.rituals.Ritual;

public interface ISecondaryMatcher {

    default void onRitualConstructed(Ritual ritual) {};

    boolean match(@Nullable EntityPlayer player, IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes);

    ItemStack[] getItemStacks();

    default boolean shouldRunBeforeWorldMatch() {
        return false;
    }
}
