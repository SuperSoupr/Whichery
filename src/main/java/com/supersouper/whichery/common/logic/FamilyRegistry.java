package com.supersouper.whichery.common.logic;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.util.data.ItemId;
import com.supersouper.whichery.ModItems;
import com.supersouper.whichery.common.logic.IngredientFamily.IngredientFamilyStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

public class FamilyRegistry {

    private static final Object2ObjectOpenCustomHashMap<ItemStack, Set<IngredientFamilyStack>> INGREDIENT_REGISTRY = new Object2ObjectOpenCustomHashMap<>(
        ItemId.STACK_ITEM_META_STRATEGY);

    public static void registerIngredient(ItemStack ingredient, IngredientFamilyStack... families) {
        INGREDIENT_REGISTRY.put(
            ingredient,
            Arrays.stream(families)
                .collect(Collectors.toSet()));
    }

    @Nullable
    public static Set<IngredientFamilyStack> getIngredientFamilies(ItemStack ingredient) {
        return INGREDIENT_REGISTRY.get(ingredient);
    }

    public static void initRegistry() {
        registerIngredient(
            new ItemStack(Items.nether_wart),
            new IngredientFamilyStack(IngredientFamily.FIRE, 2),
            new IngredientFamilyStack(IngredientFamily.EARTH, 1));
        registerIngredient(
            new ItemStack(Items.nether_star),
            new IngredientFamilyStack(IngredientFamily.FIRE, 2),
            new IngredientFamilyStack(IngredientFamily.WATER, 2),
            new IngredientFamilyStack(IngredientFamily.EARTH, 2),
            new IngredientFamilyStack(IngredientFamily.AIR, 2),
            new IngredientFamilyStack(IngredientFamily.LIFE, 2),
            new IngredientFamilyStack(IngredientFamily.DEATH, 2),
            new IngredientFamilyStack(IngredientFamily.SPIRIT, 2),
            new IngredientFamilyStack(IngredientFamily.HELL, 2));
        registerIngredient(ModItems.WITCH_PETAL.newItemStack(), new IngredientFamilyStack(IngredientFamily.DEATH, 2));
    }
}
