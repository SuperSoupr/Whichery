package com.supersouper.whichery.api.rituals;

import net.minecraft.util.IIcon;

public class ChalkType {

    public final String domain;
    public final String name;
    public final int runeCount;
    private final int drawColor;
    public IIcon[] icons;
    public boolean[] uniques;
    public IIcon[] iconsSmall;
    public boolean[] uniquesSmall;
    public IIcon[] iconsStick;
    public boolean[] uniquesStick;

    public ChalkType(String domain, String name) {
        this(domain, name, 12, 0xFFFFFF);
    }

    public ChalkType(String domain, String name, int runeCount, int drawColor) {
        this.domain = domain;
        this.name = name;
        this.runeCount = runeCount;
        this.drawColor = drawColor;
    }

    // 0: large runes, 1: small runes, 2: chalk sticks
    public int getRGB(int type, int iconIndex) {

        boolean unique = switch (type) {
            case 0 -> uniques[iconIndex];
            case 1 -> uniquesSmall[iconIndex];
            case 2 -> uniquesStick[iconIndex];
            default -> throw new IllegalArgumentException("icon type " + type + "undefined");
        };
        return unique ? 0xFFFFFF : drawColor;
    }
}
