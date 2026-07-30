package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.world.World;

import com.supersouper.whichery.common.worldgen.trees.WorldGenAlderTree;

public class BlockAlderSapling extends BlockWhicherySapling {

    public BlockAlderSapling() {
        super("alder");
    }

    @Override
    public boolean growTree(World world, int x, int y, int z, Random rand) {
        return (new WorldGenAlderTree(true)).generate(world, rand, x, y, z);
    }
}
