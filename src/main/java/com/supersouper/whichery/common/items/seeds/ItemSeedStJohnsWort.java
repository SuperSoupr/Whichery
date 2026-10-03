package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;

import com.supersouper.whichery.ModBlocks;

public class ItemSeedStJohnsWort extends ItemWhicherySeed {

    public ItemSeedStJohnsWort() {
        super("st_johns_wort");
    }

    @Override
    protected Block getCropBlock() {
        return ModBlocks.ST_JOHNS_WORT.get();
    }
}
