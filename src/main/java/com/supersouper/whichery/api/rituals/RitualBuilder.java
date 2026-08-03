package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.HashMap;

import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class RitualBuilder {

    public static RitualRecipe buildRecipe(byte centerX, byte centerY, byte centerZ,
        ISecondaryMatcher[] secondaryMatchers, Object... o) {
        ArrayList<String[]> levels = new ArrayList<>();
        HashMap<Character, IBlockMatcher> definitions = new HashMap<>();
        definitions.put(' ', null);
        int maxX = 0;
        int maxZ = 0;

        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof String[]lines) {
                levels.add(lines);
                for (String line : lines) {
                    maxX = Math.max(maxX, line.length());
                }

                maxZ = Math.max(maxZ, lines.length);
            } else if (o[i] instanceof Character c) {
                if (i + 1 < o.length && o[i + 1] instanceof IBlockMatcher matcher) {
                    i++;
                    definitions.put(c, matcher);
                } else {
                    throw new IllegalArgumentException("Character '" + c + "' must be followed by an IBlockMatcher");
                }
            } else {
                throw new IllegalArgumentException("Class '" + o[i].getClass() + "' is not allowed in ritual builder");
            }
        }
        int maxY = levels.size();

        IntArrayList keys = new IntArrayList();
        ArrayList<IBlockMatcher> vals = new ArrayList<>();

        for (byte y = 0; y < maxY; y++) {
            for (byte z = 0; z < maxZ; z++) {
                for (byte x = 0; x < maxX; x++) {
                    char c = ' ';

                    try {
                        c = levels.get(y)[z].charAt(x);
                    } catch (Exception ignore) {}

                    IBlockMatcher matcher = definitions.get(c);
                    if (matcher != null) {
                        keys.add(RitualUtils.packCoords(x, y, z));
                        vals.add(matcher);
                    }
                }
            }
        }

        Int2ObjectMap<IBlockMatcher> matcherPositions = Int2ObjectMaps
            .unmodifiable(new Int2ObjectArrayMap<>(keys.toIntArray(), vals.toArray(new IBlockMatcher[0])));

        IBlockMatcher centerMatcher = matcherPositions.get(RitualUtils.packCoords(centerX, centerY, centerZ));
        if (centerMatcher == null) {
            throw new IllegalArgumentException("Center matcher cannot be null");
        }

        IBlockMatcher[] matchers = definitions.values()
            .toArray(new IBlockMatcher[0]);

        return new RitualRecipe(
            matcherPositions,
            matchers,
            secondaryMatchers != null ? secondaryMatchers : new ISecondaryMatcher[0],
            centerX,
            centerY,
            centerZ);
    }
}
