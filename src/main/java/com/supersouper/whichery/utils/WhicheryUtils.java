package com.supersouper.whichery.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WhicheryUtils {

    public static boolean matchIngredient(ItemStack target, ItemStack input, boolean matchNBT) {
        if (target == null || input == null) return target == input;
        if (!OreDictionary.itemMatches(target, input, false)) return false;
        return !matchNBT || ItemStack.areItemStackTagsEqual(target, input);
    }
}
