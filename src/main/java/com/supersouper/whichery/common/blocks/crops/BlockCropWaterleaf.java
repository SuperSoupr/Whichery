package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.ModItems;

public class BlockCropWaterleaf extends BlockWhicheryCrop {

    public BlockCropWaterleaf(String id, int maxStage) {
        super(id, maxStage);
    }

    @Override
    protected Item getSeedItem() {
        return ModItems.WATERLEAF_SEED.get();
    }

    @Override
    public void addDropsGrown(World world, int x, int y, int z, int metadata, int fortune, ArrayList<ItemStack> drops) {
        drops.add(new ItemStack(ModItems.WATERLEAF_SEED.get(), world.rand.nextInt(2) + 1));
    }

    // Drop nothing when the top is not grown
    @Override
    public void addDropsAlways(World world, int x, int y, int z, int metadata, int fortune,
        ArrayList<ItemStack> drops) {}

    @Override
    public boolean canPlaceBlockOn(Block ground) {
        return ground == ModBlocks.WATERLEAF_STEM.get();
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z) == ModBlocks.WATERLEAF_STEM.get();
    }
}
