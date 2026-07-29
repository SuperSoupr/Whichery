package com.supersouper.whichery.api.rituals;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.api.IBlockMatcher;
import com.supersouper.whichery.api.QuadConsumer;

public class RitualRecipe {

    private final IBlockMatcher[][][] matcherPositions;
    private final IBlockMatcher[] matchers;
    private final int centerX, centerZ, centerY;

    /**
     * IBlockMatcher 3d array formatted as: [y][z][x]
     * This is because most rituals are expected to only require one y level.
     */
    public RitualRecipe(IBlockMatcher[][][] matcherPositions, IBlockMatcher[] matchers, int centerY, int centerX,
        int centerZ) {
        this.matcherPositions = matcherPositions;
        this.matchers = matchers;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.centerX = centerX;
    }

    public IBlockMatcher[][][] matcherPositions() {
        return matcherPositions;
    }

    public IBlockMatcher[] matchers() {
        return matchers;
    }

    public int centerZ() {
        return centerZ;
    }

    public int centerX() {
        return centerX;
    }

    public int centerY() {
        return centerY;
    }

    /**
     * Called with the coords of the "center" block to validate ritual placement
     */
    public boolean match(IBlockAccess world, int x, int y, int z) {
        IBlockMatcher[][][] matchers = matcherPositions();

        rotations: for (int i = 0; i < 4; i++) {
            for (int cy = 0; cy < matchers.length; cy++) {
                for (int cz = 0; cz < matchers[cy].length; cz++) {
                    for (int cx = 0; cx < matchers[cy][cz].length; cx++) {
                        IBlockMatcher matcher = matchers[cy][cz][cx];
                        if (matcher != null) {
                            int[] pos2d = { cx, cz };
                            for (int j = 0; j < i; j++) {
                                pos2d = rotate(pos2d, centerX(), centerZ());
                            }
                            if (!matcher
                                .match(world, x + pos2d[0] - centerX(), y + cy - centerY(), z + pos2d[1] - centerZ())) {
                                continue rotations;
                            }
                        }
                    }
                }
            }
            return true;
        }

        return false;
    }

    private static int[] rotate(int[] point, int pivotX, int pivotZ) {
        int dx = point[0] - pivotX;
        int dz = point[1] - pivotZ;
        return new int[] { pivotX - dz, pivotZ + dx };
    }

    public void construct(World world, int x, int y, int z) {
        forEachMatcher((matcher, cx, cy, cz) -> {
            if (matcher != null) {
                matcher.place(world, x + cx - centerX(), y + cy - centerY(), z + cz - centerZ());
            }
        });
    }

    public void forEachMatcher(QuadConsumer<IBlockMatcher, Integer, Integer, Integer> consumer) {
        IBlockMatcher[][][] matcherPositions = matcherPositions();

        for (int y = 0; y < matcherPositions.length; y++) {
            for (int z = 0; z < matcherPositions[y].length; z++) {
                for (int x = 0; x < matcherPositions[y][z].length; x++) {
                    consumer.accept(matcherPositions[y][z][x], x, y, z);
                }
            }
        }
    }
}
