package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;
import com.supersouper.whichery.common.rituals.matching.ChalkItemSecondaryMatcher;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(
    iface = "com.gtnewhorizon.structurelib.alignment.constructable.IConstructable",
    modid = "structurelib")
public class Ritual implements IConstructable {

    public final String name;
    public final RitualRecipe recipe;
    public final Class<? extends RitualAnimation>[] animationClasses;
    public final Class<? extends RitualEffect>[] effectClasses;
    public final int[] stages;
    public final boolean infiniteLastStage;

    public Ritual(String name, RitualRecipe recipe, Class<? extends RitualEffect>[] effectClasses,
        Class<? extends RitualAnimation>[] animationClasses, int[] stages) {
        this.name = name;
        this.recipe = recipe;
        this.effectClasses = effectClasses;
        this.animationClasses = animationClasses;

        if (stages[stages.length - 1] == -1) {
            infiniteLastStage = true;
            this.stages = Arrays.copyOf(stages, stages.length - 1);
        } else {
            infiniteLastStage = false;
            this.stages = stages;
        }

        ArrayList<ISecondaryMatcher> requiredItems = recipe.secondaryMatchersByClass
            .get(ChalkItemSecondaryMatcher.class.hashCode());
        if (requiredItems != null && requiredItems.size() > stages.length) {
            throw new IllegalArgumentException(
                "Ritual '" + name
                    + "'s recipe requires "
                    + requiredItems
                    + " items but it only has "
                    + stages.length
                    + " stages.");
        }

        for (ISecondaryMatcher matcher : recipe.secondaryMatchers) {
            matcher.onRitualConstructed(this);
        }
    }

    public Ritual(String name, RitualRecipe recipe, Class<? extends RitualEffect> effectClass,
        Class<? extends RitualAnimation> animationClass, int[] stages) {
        this(name, recipe, effects(effectClass), animations(animationClass), stages);
    }

    @SafeVarargs
    public static Class<? extends RitualEffect>[] effects(Class<? extends RitualEffect>... classes) {
        return classes;
    }

    @SafeVarargs
    public static Class<? extends RitualAnimation>[] animations(Class<? extends RitualAnimation>... classes) {
        return classes;
    }

    public String getDisplayName() {
        return getDisplayName(name);
    }

    public static String getDisplayName(String ritualName) {
        return StatCollector.translateToLocal("whichery.ritual." + ritualName + ".name");
    }

    @Optional.Method(modid = "structurelib")
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Optional.Method(modid = "structurelib")
    @SideOnly(Side.CLIENT)
    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[0];
    }
}
