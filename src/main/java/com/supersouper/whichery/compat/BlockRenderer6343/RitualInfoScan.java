package com.supersouper.whichery.compat.BlockRenderer6343;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.RitualRecipe;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public class RitualInfoScan implements Runnable {

    private final Consumer<Int2ObjectMap<ObjectSet<IConstructable>>> resultCallback;
    private final Consumer<Object2ObjectMap<IConstructable, ItemStack>> stackCallback;

    public RitualInfoScan(Consumer<Int2ObjectMap<ObjectSet<IConstructable>>> resultCallback,
        Consumer<Object2ObjectMap<IConstructable, ItemStack>> stackCallback) {
        this.resultCallback = resultCallback;
        this.stackCallback = stackCallback;
    }

    @Override
    public void run() {
        Int2ObjectMap<ObjectSet<IConstructable>> result = new Int2ObjectOpenHashMap<>();
        Object2ObjectMap<IConstructable, ItemStack> stacks = new Object2ObjectOpenHashMap<>();

        for (Ritual ritual : RitualRegistry.rituals()) {
            RitualRecipe recipe = ritual.recipe;

            IBlockMatcher[] matchers = recipe.matchersRaw;
            for (IBlockMatcher matcher : matchers) {
                if (matcher != null) {
                    for (int hash : matcher.itemStackHashCodes()) {
                        result.computeIfAbsent(hash, k -> new ObjectOpenHashSet<>())
                            .add(ritual);
                    }
                }
            }

            stacks.put(ritual, recipe.centerMatcher.getItemStack());

        }

        resultCallback.accept(result);
        stackCallback.accept(stacks);
    }
}
