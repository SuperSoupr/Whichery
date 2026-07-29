package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;

import com.supersouper.whichery.ModBlocks;

public class ItemSeedMandrake extends ItemWhicherySeed {

    public ItemSeedMandrake() {
        super("mandrake");
    }

    @Override
    protected Block getCropBlock() {
        return ModBlocks.MANDRAKE.get();
    }
}
