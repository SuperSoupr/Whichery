package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.item.Item;

import com.supersouper.whichery.ModBlocks;

public class BlockYewLeaves extends BlockWhicheryLeaves {

    public BlockYewLeaves() {
        super("yew");
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        return ModBlocks.YEW_SAPLING.getItem();
    }
}
