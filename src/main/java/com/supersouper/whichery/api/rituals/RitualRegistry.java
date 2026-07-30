package com.supersouper.whichery.api.rituals;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RitualRegistry {

    private static final HashMap<String, Ritual> RITUALS = new HashMap<>();
    static final HashMap<Item, Function<ItemStack, Integer>> ITEM_HASHERS = new HashMap<>();

    public static void registerRitual(Ritual ritual) {
        RITUALS.put(ritual.getName(), ritual);
    }

    public static Collection<Ritual> rituals() {
        return RITUALS.values();
    }

    public static Ritual getRitual(String name) {
        return RITUALS.get(name);
    }

    public static void registerItemHasher(Item item, Function<ItemStack, Integer> hasher) {
        ITEM_HASHERS.put(item, hasher);
    }
}
