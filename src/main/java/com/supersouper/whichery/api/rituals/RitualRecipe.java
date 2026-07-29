package com.supersouper.whichery.api.rituals;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.api.IBlockMatcher;

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
        return false;
    }

    public void construct(World world, int x, int y, int z) {
        IBlockMatcher[][][] matchers = matcherPositions();

        for (int cy = 0; cy < matchers.length; cy++) {
            for (int cz = 0; cz < matchers[cy].length; cz++) {
                for (int cx = 0; cx < matchers[cy][cz].length; cx++) {
                    IBlockMatcher matcher = matchers[cy][cz][cx];
                    if (matcher != null) {
                        matcher.place(world, x + cx - centerX(), y + cy - centerY(), z + cz - centerZ());
                    }
                }
            }
        }
    }
}
