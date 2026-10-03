package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;

import com.supersouper.whichery.ModBlocks;

public class ItemSeedHenbane extends ItemWhicherySeed {

    public ItemSeedHenbane() {
        super("henbane");
    }

    @Override
    protected Block getCropBlock() {
        return ModBlocks.HENBANE.get();
    }
}
