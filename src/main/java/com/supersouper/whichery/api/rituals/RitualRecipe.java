package com.supersouper.whichery.api.rituals;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.api.IBlockMatcher;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;

public class RitualRecipe {

    private final Int2ObjectMap<IBlockMatcher> matcherPositions;
    private final IBlockMatcher[] matchers;
    private final byte centerX, centerZ, centerY;

    /**
     * IBlockMatcher 3d array formatted as: [y][z][x]
     * This is because most rituals are expected to only require one y level.
     */
    public RitualRecipe(Int2ObjectMap<IBlockMatcher> matcherPositions, IBlockMatcher[] matchers, byte centerX,
        byte centerY, byte centerZ) {
        this.matcherPositions = matcherPositions;
        this.matchers = matchers;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
    }

    public Int2ObjectMap<IBlockMatcher> matcherPositions() {
        return matcherPositions;
    }

    public IBlockMatcher[] matchers() {
        return matchers;
    }

    public byte centerZ() {
        return centerZ;
    }

    public byte centerX() {
        return centerX;
    }

    public byte centerY() {
        return centerY;
    }

    /**
     * Called with the coords of the "center" block to validate ritual placement
     */
    public boolean match(IBlockAccess world, int x, int y, int z, byte[] rotationBuffer) {
        rotations: for (byte i = 0; i < 4; i++) {
            for (Int2ObjectMap.Entry<IBlockMatcher> e : Int2ObjectMaps.fastIterable(matcherPositions())) {
                IBlockMatcher matcher = e.getValue();

                if (matcher != null) {
                    byte[] coords = RitualUtils.unpackCoords(e.getIntKey());

                    int[] pos2d = { coords[0], coords[2] };

                    for (int j = 0; j < i; j++) {
                        pos2d = rotate(pos2d, centerX(), centerZ());
                    }

                    if (!matcher
                        .match(world, x + pos2d[0] - centerX(), y + coords[1] - centerY(), z + pos2d[1] - centerZ())) {
                        continue rotations;
                    }
                }
            }
            rotationBuffer[0] = i;
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
        for (Int2ObjectMap.Entry<IBlockMatcher> e : Int2ObjectMaps.fastIterable(matcherPositions())) {
            IBlockMatcher matcher = e.getValue();
            if (matcher != null) {
                byte[] coords = RitualUtils.unpackCoords(e.getIntKey());
                matcher.place(world, x + coords[0] - centerX(), y + coords[1] - centerY(), z + coords[2] - centerZ());
            }
        }
    }
}
