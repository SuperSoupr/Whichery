package com.supersouper.whichery.api.rituals;

public class ChalkType {

    public final String domain;
    public final String name;
    public final int runeCount;
    public final int drawColor;

    public ChalkType(String domain, String name) {
        this(domain, name, 12, 0xFFFFFF);
    }

    public ChalkType(String domain, String name, int runeCount, int drawColor) {
        this.domain = domain;
        this.name = name;
        this.runeCount = runeCount;
        this.drawColor = drawColor;
    }
}
