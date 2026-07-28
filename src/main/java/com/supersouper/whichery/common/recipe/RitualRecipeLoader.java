package com.supersouper.whichery.common.recipe;

import net.minecraft.init.Blocks;

import org.jetbrains.annotations.ApiStatus;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.BlockMatcherBasic;
import com.supersouper.whichery.api.IBlockMatcher;
import com.supersouper.whichery.api.rituals.BlockMatcherChalk;
import com.supersouper.whichery.api.rituals.RitualBuilder;
import com.supersouper.whichery.api.rituals.RitualRecipe;
import com.supersouper.whichery.api.rituals.RitualRegistry;

@ApiStatus.Internal
public class RitualRecipeLoader {

    public static void loadRecipes() {

        // spotless:off
        BlockMatcherBasic g = new BlockMatcherBasic(ModBlocks.CHALK_BLOCK.get());
        IBlockMatcher n = null;
        RitualRegistry.registerRecipe(new RitualRecipe(new IBlockMatcher[][][]{
            {
                {n, n, g, g, g, n, n},
                {n, g, n, n, n, g, n},
                {g, n, n, n, n, n, g},
                {g, n, n, g, n, n, g},
                {g, n, n, n, n, n, g},
                {n, g, n, n, n, g, n},
                {n, n, g, g, g, n, n},
            }}, 0, 3, 3));
        RitualRegistry.registerRecipe(new RitualRecipe(RitualBuilder.build(
            new String[]{
                "g       g",
                "   ccc   ",
                "  c   c  ",
                " c     c ",
                " c  c  c ",
                " c     c ",
                "  c   c  ",
                "   ccc   ",
                "g       g",
            },
            new String[]{
                "g       g",
                "         ",
                "         ",
                "         ",
                "         ",
                "         ",
                "         ",
                "         ",
                "g       g",
            },
            'c',
            new BlockMatcherChalk(ModBlocks.CHALK_BLOCK.get(), 1),
            'g',
            new BlockMatcherBasic(Blocks.glowstone)
        ), 0, 4, 4));
        // spotless:on
    }
}
