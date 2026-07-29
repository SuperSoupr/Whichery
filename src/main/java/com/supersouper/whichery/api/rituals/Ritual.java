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

    protected final String name;
    protected final RitualRecipe recipe;
    protected final Class<? extends RitualAnimation> animationClass;
    protected final Class<? extends RitualEffect> effectClass;
    protected final int[] stages;

    public Ritual(String name, RitualRecipe recipe, Class<? extends RitualEffect> effectClass,
        Class<? extends RitualAnimation> animationClass, int[] stages) {
        this.name = name;
        this.recipe = recipe;
        this.effectClass = effectClass;
        this.animationClass = animationClass;
        this.stages = stages;
    }

    public String getName() {
        return name;
    }

    public RitualRecipe getRecipe() {
        return recipe;
    }

    public Class<? extends RitualAnimation> getAnimationClass() {
        return animationClass;
    }

    public Class<? extends RitualEffect> getEffectClass() {
        return effectClass;
    }

    public int[] getStages() {
        return stages;
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
