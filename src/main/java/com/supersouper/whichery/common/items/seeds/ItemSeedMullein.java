package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;

import com.supersouper.whichery.ModBlocks;

public class ItemSeedMullein extends ItemWhicherySeed {

    public ItemSeedMullein() {
        super("mullein");
    }

    @Override
    protected Block getCropBlock() {
        return ModBlocks.MULLEIN.get();
    }
}
