package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.HashMap;

import com.supersouper.whichery.api.IBlockMatcher;

public class RitualBuilder {

    public static RitualRecipe buildRecipe(int centerY, int centerX, int centerZ, Object... o) {
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

        IBlockMatcher[][][] matcherPositions = new IBlockMatcher[maxY][maxX][maxZ];

        for (int y = 0; y < matcherPositions.length; y++) {
            for (int z = 0; z < matcherPositions[y].length; z++) {
                for (int x = 0; x < matcherPositions[y][z].length; x++) {
                    char c = ' ';

                    try {
                        c = levels.get(y)[z].charAt(x);
                    } catch (Exception ignore) {}

                    matcherPositions[y][z][x] = definitions.get(c);
                }
            }
        }

        if (matcherPositions[centerY][centerZ][centerX] == null) {
            throw new IllegalArgumentException("Center matcher cannot be null");
        }

        IBlockMatcher[] matchers = definitions.values()
            .toArray(new IBlockMatcher[0]);

        return new RitualRecipe(matcherPositions, matchers, centerY, centerX, centerZ);
    }
}
