package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;

import com.supersouper.whichery.ModBlocks;

public class ItemSeedWitchesThimble extends ItemWhicherySeed {

    public ItemSeedWitchesThimble(String id) {
        super(id);
    }

    @Override
    protected Block getCropBlock() {
        return ModBlocks.WITCHES_THIMBLE.get();
    }
}
