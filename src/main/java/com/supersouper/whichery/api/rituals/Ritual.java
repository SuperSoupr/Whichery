package com.supersouper.whichery.api.rituals;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;

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

    public Ritual(String name, RitualRecipe recipe, Class<? extends RitualEffect>[] effectClasses,
        Class<? extends RitualAnimation>[] animationClasses, int[] stages) {
        this.name = name;
        this.recipe = recipe;
        this.effectClasses = effectClasses;
        this.animationClasses = animationClasses;
        this.stages = stages;
    }

    /**
     * Convenience overload for the common single-effect, single-animation ritual.
     */
    public Ritual(String name, RitualRecipe recipe, Class<? extends RitualEffect> effectClass,
        Class<? extends RitualAnimation> animationClass, int[] stages) {
        this(name, recipe, effects(effectClass), animations(animationClass), stages);
    }

    /**
     * Builds an effect class array. Exists so callers can write {@code Ritual.effects(A.class, B.class)}
     * instead of an unchecked {@code new Class[]{...}} — a generic array cannot be created directly.
     */
    @SafeVarargs
    public static Class<? extends RitualEffect>[] effects(Class<? extends RitualEffect>... classes) {
        return classes;
    }

    /**
     * Builds an animation class array. See {@link #effects}.
     */
    @SafeVarargs
    public static Class<? extends RitualAnimation>[] animations(Class<? extends RitualAnimation>... classes) {
        return classes;
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
