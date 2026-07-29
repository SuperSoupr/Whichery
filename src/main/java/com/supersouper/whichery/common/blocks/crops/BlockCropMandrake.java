package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;

public class BlockCropMandrake extends BlockWhicheryCrop {

    public BlockCropMandrake(String id, int maxStage) {
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
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }
}
