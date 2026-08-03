package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;

public class RitualRecipe {

    private final Int2ObjectMap<IBlockMatcher> matcherPositions;
    private final IBlockMatcher[] matchers;
    private final ISecondaryMatcher[] secondaryMatchers;
    private final byte centerX, centerZ, centerY;

    /**
     * IBlockMatcher 3d array formatted as: [y][z][x]
     * This is because most rituals are expected to only require one y level.
     */
    public RitualRecipe(Int2ObjectMap<IBlockMatcher> matcherPositions, IBlockMatcher[] matchers,
        ISecondaryMatcher[] secondaryMatchers, byte centerX, byte centerY, byte centerZ) {
        this.matcherPositions = matcherPositions;
        this.matchers = matchers;
        this.secondaryMatchers = secondaryMatchers;
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
        byte[] coords = new byte[3];
        int[] pos2d = new int[2];
        ArrayList<TileEntity> tes = new ArrayList<>();
        rotations: for (byte i = 0; i < 4; i++) {
            tes.clear();
            for (Int2ObjectMap.Entry<IBlockMatcher> e : Int2ObjectMaps.fastIterable(matcherPositions())) {
                IBlockMatcher matcher = e.getValue();

                if (matcher != null) {
                    RitualUtils.unpackCoords(e.getIntKey(), coords);

                    pos2d[0] = coords[0];
                    pos2d[1] = coords[2];

                    for (int j = 0; j < i; j++) {
                        rotate(pos2d, centerX(), centerZ());
                    }

                    if (!matcher.match(
                        world,
                        x + pos2d[0] - centerX(),
                        y + coords[1] - centerY(),
                        z + pos2d[1] - centerZ(),
                        tes)) {
                        continue rotations;
                    }
                }
            }
            for (ISecondaryMatcher secondaryMatcher : secondaryMatchers) {
                if (secondaryMatcher.match(world, x, y, z, tes)) {
                    rotationBuffer[0] = i;
                    return true;
                }
            }
            return false;
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
        for (Int2ObjectMap.Entry<IBlockMatcher> e : Int2ObjectMaps.fastIterable(matcherPositions())) {
            IBlockMatcher matcher = e.getValue();
            if (matcher != null) {
                RitualUtils.unpackCoords(e.getIntKey(), coords);
                matcher.place(world, x + coords[0] - centerX(), y + coords[1] - centerY(), z + coords[2] - centerZ());
            }
        }
    }
}
