package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.item.Item;

public class BlockRowanLeaves extends BlockWhicheryLeaves {

    public BlockRowanLeaves() {
        super("rowan");
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        return null;
    }
}
