package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.HashMap;

import com.supersouper.whichery.api.IBlockMatcher;

public class RitualBuilder {

    public static IBlockMatcher[][][] build(Object... o) {
        ArrayList<String[]> levels = new ArrayList<>();
        HashMap<Character, IBlockMatcher> definitions = new HashMap<>();
        definitions.put(' ', null);
        int maxX = 0;
        int maxZ = 0;

        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof String[]lines) {
                levels.add(lines);
                for (String line : lines) {
                    maxZ = Math.max(maxZ, line.length());
                }

                maxX = Math.max(maxX, lines.length);
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

        IBlockMatcher[][][] result = new IBlockMatcher[maxY][maxX][maxZ];

        for (int y = 0; y < result.length; y++) {
            for (int x = 0; x < result[y].length; x++) {
                for (int z = 0; z < result[y][x].length; z++) {
                    char c = ' ';

                    try {
                        c = levels.get(y)[z].charAt(x);
                    } catch (Exception ignore) {}

                    result[y][x][z] = definitions.get(c);
                }
            }
        }

        return result;
    }
}
