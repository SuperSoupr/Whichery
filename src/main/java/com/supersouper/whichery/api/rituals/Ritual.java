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

    protected final RitualAnimation animation;
    protected final int stageCount;

    public Ritual(String name, RitualRecipe recipe, RitualAnimation animation, int stageCount) {
        this.name = name;
        this.recipe = recipe;
        this.animation = animation;
        this.stageCount = stageCount;
    }

    public String getName() {
        return name;
    }

    public RitualRecipe getRecipe() {
        return recipe;
    }

    public RitualAnimation getAnimation() {
        return animation;
    }

    public int getStageCount() {
        return stageCount;
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
