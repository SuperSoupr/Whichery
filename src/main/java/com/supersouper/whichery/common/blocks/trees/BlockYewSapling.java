package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.world.World;

import com.supersouper.whichery.common.worldgen.trees.WorldGenYewTree;

public class BlockYewSapling extends BlockWhicherySapling {

    public BlockYewSapling() {
        super("yew");
    }

    @Override
    public boolean growTree(World world, int x, int y, int z, Random rand) {
        return (new WorldGenYewTree(true)).generate(world, rand, x, y, z);
    }
}
