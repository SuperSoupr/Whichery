package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.world.World;

import com.supersouper.whichery.common.worldgen.trees.WorldGenRowanTree;

public class BlockRowanSapling extends BlockWhicherySapling {

    public BlockRowanSapling() {
        super("rowan");
    }

    @Override
    public boolean growTree(World world, int x, int y, int z, Random rand) {
        return (new WorldGenRowanTree(true)).generate(world, rand, x, y, z);
    }
}
