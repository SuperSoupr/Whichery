package com.supersouper.whichery.common.worldgen.trees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import com.supersouper.whichery.ModBlocks;

public class WorldGenHawthornTree extends WorldGenAbstractTree {

    public WorldGenHawthornTree(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        int height = 3 + rand.nextInt(2);
        int top = y + height;

        if (y < 1 || top + 1 >= world.getHeight()) return false;

        for (int ty = y; ty <= top; ty++) {
            Block b = world.getBlock(x, ty, z);
            if (!b.isAir(world, x, ty, z) && !(b instanceof BlockLeaves)) return false;
        }

        for (int ty = y; ty <= top; ty++) {
            setBlockAndNotifyAdequately(world, x, ty, z, ModBlocks.HAWTHORN_LOG.get(), 0);
        }

        int[][] layers = { { -2, 2 }, { -1, 2 }, { 0, 1 }, { 1, 1 }, };

        for (int[] layer : layers) {
            int ly = top + layer[0];
            int r = layer[1];
            for (int lx = x - r; lx <= x + r; lx++) {
                for (int lz = z - r; lz <= z + r; lz++) {
                    int dx = lx - x;
                    int dz = lz - z;
                    if (r > 1 && Math.abs(dx) == r && Math.abs(dz) == r) continue;
                    Block existing = world.getBlock(lx, ly, lz);
                    if (existing.isAir(world, lx, ly, lz) || existing instanceof BlockLeaves) {
                        setBlockAndNotifyAdequately(world, lx, ly, lz, ModBlocks.HAWTHORN_LEAVES.get(), 0);
                    }
                }
            }
        }

        return true;
    }
}
