package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.item.Item;

import com.supersouper.whichery.ModBlocks;

public class BlockAlderLeaves extends BlockWhicheryLeaves {

    public BlockAlderLeaves() {
        super("alder");
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        return ModBlocks.ALDER_SAPLING.getItem();
    }
}
