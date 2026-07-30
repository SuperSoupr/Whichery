package com.supersouper.whichery.common.logic;

public enum IngredientFamily {

    FIRE("fire"),
    WATER("water"),
    AIR("air"),
    EARTH("earth"),
    LIFE("life"),
    DEATH("death"),
    SPIRIT("spirit"),
    HELL("hell");

    public final String id;

    IngredientFamily(String id) {
        this.id = id;
    }

    public static class IngredientFamilyStack {

        public final IngredientFamily family;
        public final int amount;

        public IngredientFamilyStack(IngredientFamily family, int amount) {
            this.family = family;
            this.amount = amount;
        }
    }
}
