package com.supersouper.whichery.api.ingredientfamilies;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.util.data.ItemId;
import com.supersouper.whichery.ModItems;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

public final class FamilyRegistry {

    private static final Map<String, IngredientFamily> FAMILIES = new LinkedHashMap<>();

    private static final Object2ObjectOpenCustomHashMap<ItemStack, Set<IngredientFamily.FamilyStack>> INGREDIENT_REGISTRY = new Object2ObjectOpenCustomHashMap<>(
        ItemId.STACK_ITEM_META_STRATEGY);

    private static int order = 1000;

    /**
     * Register a pair of opposing families by String id. Families must always be registed in pairs through this method.
     */
    public static IngredientFamily[] registerPair(String idA, String idB) {
        if (FAMILIES.containsKey(idA)) throw new IllegalArgumentException("Family already registered: " + idA);
        if (FAMILIES.containsKey(idB)) throw new IllegalArgumentException("Family already registered: " + idB);

        IngredientFamily a = new IngredientFamily(idA);
        IngredientFamily b = new IngredientFamily(idB);

        a.setOpposite(b);
        a.setOrder(order);
        order += 1000;

        b.setOpposite(a);
        b.setOrder(order);
        order += 1000;

        FAMILIES.put(idA, a);
        FAMILIES.put(idB, b);
        return new IngredientFamily[] { a, b };
    }

    @Nullable
    public static IngredientFamily getFamily(String id) {
        return FAMILIES.get(id);
    }

    public static Map<String, IngredientFamily> getAllFamilies() {
        return Collections.unmodifiableMap(FAMILIES);
    }

    public static void registerIngredient(ItemStack ingredient, IngredientFamily.FamilyStack... families) {
        INGREDIENT_REGISTRY.put(
            ingredient,
            Arrays.stream(families)
                .collect(Collectors.toSet()));
    }

    @Nullable
    public static Set<IngredientFamily.FamilyStack> getIngredientFamilies(ItemStack ingredient) {
        return INGREDIENT_REGISTRY.get(ingredient);
    }

    public static IngredientFamily FIRE, WATER, AIR, EARTH, LIFE, DEATH, SPIRIT, HELL;

    public static void initFamilies() {
        IngredientFamily[] f;

        f = registerPair("fire", "water");
        FIRE = f[0];
        WATER = f[1];

        f = registerPair("air", "earth");
        AIR = f[0];
        EARTH = f[1];

        f = registerPair("life", "death");
        LIFE = f[0];
        DEATH = f[1];

        f = registerPair("spirit", "hell");
        SPIRIT = f[0];
        HELL = f[1];
    }

    public static void initIngredients() {
        registerIngredient(
            new ItemStack(Items.nether_wart),
            new IngredientFamily.FamilyStack(FIRE, 2),
            new IngredientFamily.FamilyStack(EARTH, 1));
        registerIngredient(
            new ItemStack(Items.nether_star),
            new IngredientFamily.FamilyStack(FIRE, 2),
            new IngredientFamily.FamilyStack(WATER, 2),
            new IngredientFamily.FamilyStack(EARTH, 2),
            new IngredientFamily.FamilyStack(AIR, 2),
            new IngredientFamily.FamilyStack(LIFE, 2),
            new IngredientFamily.FamilyStack(DEATH, 2),
            new IngredientFamily.FamilyStack(SPIRIT, 2),
            new IngredientFamily.FamilyStack(HELL, 2));
        registerIngredient(ModItems.WITCH_PETAL.newItemStack(), new IngredientFamily.FamilyStack(DEATH, 2));
    }
}
