package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class RitualRecipe {

    public final int[] matcherPositions;
    public final IBlockMatcher[] matchers;
    public final IBlockMatcher[] matchersRaw;
    public final IBlockMatcher centerMatcher;
    public final ISecondaryMatcher[] secondaryMatchers;
    public final Int2ObjectOpenHashMap<ArrayList<ISecondaryMatcher>> secondaryMatchersByClass = new Int2ObjectOpenHashMap<>();
    public final byte centerX, centerZ, centerY;

    /**
     * IBlockMatcher 3d array formatted as: [y][z][x]
     * This is because most rituals are expected to only require one y level.
     */
    public RitualRecipe(int[] matcherPositions, IBlockMatcher[] matchers, IBlockMatcher[] matchersRaw,
        ISecondaryMatcher[] secondaryMatchers, byte centerX, byte centerY, byte centerZ) {
        IBlockMatcher centerMatcherTmp = null;
        this.matcherPositions = matcherPositions;
        this.matchers = matchers;
        this.matchersRaw = matchersRaw;
        this.secondaryMatchers = secondaryMatchers;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;

        for (ISecondaryMatcher secondaryMatcher : secondaryMatchers) {
            secondaryMatchersByClass.computeIfAbsent(
                secondaryMatcher.getClass()
                    .hashCode(),
                k -> new ArrayList<>())
                .add(secondaryMatcher);
        }

        int packedCenter = RitualUtils.packCoords(this.centerX, this.centerY, this.centerZ);
        for (int i = 0;  i < matchers.length; i++) {
            if (matcherPositions[i] == packedCenter) {
                centerMatcherTmp = matchers[i];
                break;
            }
        }
        centerMatcher = centerMatcherTmp;
        if (centerMatcher == null) {
            throw new IllegalArgumentException("Center matcher cannot be null");
        }
    }

    /**
     * Called with the coords of the "center" block to validate ritual placement
     */
    public boolean match(IBlockAccess world, int x, int y, int z, byte[] rotationBuffer, ArrayList<TileEntity> tes) {
        byte[] coords = new byte[3];
        int[] pos2d = new int[2];
        rotations: for (byte i = 0; i < 4; i++) {
            tes.clear();
            for (int j = 0;  j < matchers.length; j++) {
                IBlockMatcher matcher = matchers[j];

                if (matcher != null) {
                    RitualUtils.unpackCoords(matcherPositions[j], coords);

                    pos2d[0] = coords[0];
                    pos2d[1] = coords[2];

                    for (int j2 = 0; j2 < i; j2++) {
                        rotate(pos2d, centerX, centerZ);
                    }

                    if (!matcher
                        .match(world, x + pos2d[0] - centerX, y + coords[1] - centerY, z + pos2d[1] - centerZ, tes)) {
                        continue rotations;
                    }
                }
            }
            for (ISecondaryMatcher secondaryMatcher : secondaryMatchers) {
                if (!secondaryMatcher.match(world, x, y, z, tes)) {
                    return false;
                }
            }
            rotationBuffer[0] = i;
            return true;
        }

        return false;
    }

    private static void rotate(int[] point, int pivotX, int pivotZ) {
        int dx = point[0] - pivotX;
        int dz = point[1] - pivotZ;
        point[0] = pivotX - dz;
        point[1] = pivotZ + dx;
    }

    public void construct(World world, int x, int y, int z) {
        byte[] coords = new byte[3];
        for (int i = 0;  i < matchers.length; i++) {
            IBlockMatcher matcher = matchers[i];
            if (matcher != null) {
                RitualUtils.unpackCoords(matcherPositions[i], coords);
                matcher.place(world, x + coords[0] - centerX, y + coords[1] - centerY, z + coords[2] - centerZ);
            }
        }
    }
}
