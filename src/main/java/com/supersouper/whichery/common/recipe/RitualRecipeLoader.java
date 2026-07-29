package com.supersouper.whichery.common.recipe;

import net.minecraft.init.Blocks;

import org.jetbrains.annotations.ApiStatus;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.BlockMatcherBasic;
import com.supersouper.whichery.api.rituals.BlockMatcherChalk;
import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.RitualAnimation;
import com.supersouper.whichery.api.rituals.RitualBuilder;
import com.supersouper.whichery.api.rituals.RitualRegistry;

@ApiStatus.Internal
public class RitualRecipeLoader {

    @ApiStatus.Internal
    public static void loadRecipes() {

        // spotless:off
        RitualRegistry.registerRitual(new Ritual("banana1", RitualBuilder.buildRecipe(0, 3, 3,
            new String[]{
                "  ccc  ",
                " c   c ",
                "c     c",
                "c  c  c",
                "c     c",
                " c   c ",
                "  ccc  ",
            },
            'c',
            new BlockMatcherChalk(ModBlocks.CHALK_BLOCK.get(), 1)
        ), new RitualAnimation(), 1));
        RitualRegistry.registerRitual(new Ritual("banana1", RitualBuilder.buildRecipe(0, 4, 4,
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
        ), new RitualAnimation(), 1));
        RitualRegistry.registerRitual(new Ritual("banana1", RitualBuilder.buildRecipe(0, 4, 4,
            new String[]{
                "   ccc   ",
                "  c   c  ",
                " c     c ",
                "c       c",
                "c   c   c",
                "c       c",
                " c     c ",
                "  c   c  ",
                "   ccc   ",
            },
            'c',
            new BlockMatcherChalk(ModBlocks.CHALK_BLOCK.get(), 1)
        ), new RitualAnimation(), 1));
        // spotless:on
    }
}
