package com.supersouper.whichery.compat.BlockRenderer6343;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.supersouper.whichery.api.rituals.RitualUtils;

import blockrenderer6343.integration.nei.MultiblockHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

public class RitualMultiblockHandler extends MultiblockHandler {

    private static final RitualGuiHandler baseHandler = new RitualGuiHandler();
    private static Int2ObjectMap<ObjectSet<IConstructable>> multiBlockComponents;
    private static Object2ObjectMap<IConstructable, ItemStack> stacks;

    static {
        new Thread(new RitualInfoScan(e -> multiBlockComponents = e, s -> stacks = s)).start();
    }

    public RitualMultiblockHandler() {
        super(baseHandler);
    }

    @Override
    public @NotNull ItemStack getConstructableStack(IConstructable multiblock) {
        return stacks.get(multiblock);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new RitualMultiblockHandler();
    }

    @Override
    protected @NotNull ObjectSet<IConstructable> tryLoadingMultiblocks(ItemStack candidate) {
        if (multiBlockComponents == null) return ObjectSets.emptySet();

        return multiBlockComponents.getOrDefault(RitualUtils.hashItemStack(candidate), ObjectSets.emptySet());
    }
}
