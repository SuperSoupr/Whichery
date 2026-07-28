package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class RitualRegistry {

    private static final ArrayList<RitualRecipe> RECIPES = new ArrayList<>();
    private static final Object2ObjectOpenHashMap<Item, Function<ItemStack, Integer>> ITEM_HASHERS = new Object2ObjectOpenHashMap<>();

    public static void registerRecipe(RitualRecipe recipe) {
        RECIPES.add(recipe);
    }

    public static ArrayList<RitualRecipe> recipes() {
        return RECIPES;
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
