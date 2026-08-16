package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.ApiStatus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RitualRegistry {

    // Rituals
    private static final HashMap<String, Ritual> RITUALS = new HashMap<>();
    static final HashMap<Item, Function<ItemStack, Integer>> ITEM_HASHERS = new HashMap<>();

    public static void registerRitual(Ritual ritual) {
        RITUALS.put(ritual.name, ritual);
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

    // Chalk Types

    public static final HashMap<String, Integer> CHALK_TYPES = new HashMap<>();
    private static List<String> CHALK_TYPES_LIST;
    public static final String DEFAULT_CHALK_TYPE_NAME = "basic";

    public static void registerChalkType(String name, int color) {
        CHALK_TYPES.put(name, color);
    }

    public static boolean chalkExists(String name) {
        return CHALK_TYPES.containsKey(name);
    }

    @ApiStatus.Internal
    public static void finalizeChalkTypes() {
        if (CHALK_TYPES_LIST == null) {
            CHALK_TYPES_LIST = Collections.unmodifiableList(new ArrayList<>(CHALK_TYPES.keySet()));
        }
    }

    public static List<String> getChalkTypeList() {
        return CHALK_TYPES_LIST;
    }

    // Rune textures
    public static final int RUNE_COUNT = 12;
    @SideOnly(Side.CLIENT)
    public static HashMap<String, IIcon[]> RUNE_ICONS;
}
