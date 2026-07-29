package com.supersouper.whichery.api.rituals;

import java.util.Objects;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public class RitualRegistry {

    private static final Object2ObjectOpenHashMap<String, Ritual> RITUALS = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<Item, Function<ItemStack, Integer>> ITEM_HASHERS = new Object2ObjectOpenHashMap<>();

    public static void registerRitual(Ritual ritual) {
        RITUALS.put(ritual.getName(), ritual);
    }

    public static ObjectCollection<Ritual> rituals() {
        return RITUALS.values();
    }

    public static Ritual getRitual(String name) {
        return RITUALS.get(name);
    }

    public static void registerItemHasher(Item item, Function<ItemStack, Integer> hasher) {
        ITEM_HASHERS.put(item, hasher);
    }

    public static int hashItemStack(ItemStack item) {
        Objects.requireNonNull(Objects.requireNonNull(item));

        return ITEM_HASHERS.getOrDefault(
            item.getItem(),
            stack -> stack.getItem()
                .hashCode() + stack.getItemDamage())
            .apply(item);
    }
}
