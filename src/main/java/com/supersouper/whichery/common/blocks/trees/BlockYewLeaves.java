package com.supersouper.whichery.common.blocks.trees;

import net.minecraft.item.Item;

import java.util.Random;

public class BlockYewLeaves extends BlockWhicheryLeaves {

    public BlockYewLeaves() {
        super("yew");
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        return null;
    }
}
