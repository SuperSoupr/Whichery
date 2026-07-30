package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.world.World;

import com.supersouper.whichery.common.worldgen.trees.WorldGenHawthornTree;

public class BlockHawthornSapling extends BlockWhicherySapling {

    public BlockHawthornSapling() {
        super("hawthorn");
    }

    @Override
    public boolean growTree(World world, int x, int y, int z, Random rand) {
        return (new WorldGenHawthornTree(true)).generate(world, rand, x, y, z);
    }
}
