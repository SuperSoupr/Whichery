package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.item.Item;

import com.supersouper.whichery.ModBlocks;

public class BlockHawthornLeaves extends BlockWhicheryLeaves {

    public BlockHawthornLeaves() {
        super("hawthorn");
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        return ModBlocks.HAWTHORN_SAPLING.getItem();
    }
}
