package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;

public class BlockCropWitchesThimble extends BlockWhicheryCrop {

    public BlockCropWitchesThimble(String id, int maxStage) {
        super(id, maxStage);
    }

    @Override
    protected Item getSeedItem() {
        return ModItems.WITCHES_THIMBLE_SEED.get();
    }

    @Override
    public void addDropsAlways(World world, int x, int y, int z, int metadata, int fortune,
        ArrayList<ItemStack> drops) {
        if (world.isDaytime()) return;
        super.addDropsAlways(world, x, y, z, metadata, fortune, drops);
    }

    @Override
    public void addDropsGrown(World world, int x, int y, int z, int metadata, int fortune, ArrayList<ItemStack> drops) {
        if (world.isDaytime()) return;
        drops.add(new ItemStack(ModItems.WITCH_PETAL.get(), world.rand.nextInt(3) + 1));
        if (world.rand.nextInt(100) < 20) drops.add(ModItems.WITCHES_THIMBLE_SEED.newItemStack(1));
    }
}
