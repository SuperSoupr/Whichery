package com.supersouper.whichery.common.recipe;

import net.minecraft.init.Blocks;

import org.jetbrains.annotations.ApiStatus;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.BlockMatcherBasic;
import com.supersouper.whichery.api.rituals.BlockMatcherChalk;
import com.supersouper.whichery.api.rituals.RitualBuilder;
import com.supersouper.whichery.api.rituals.RitualRegistry;

@ApiStatus.Internal
public class RitualRecipeLoader {

    @ApiStatus.Internal
    public static void loadRecipes() {

        // spotless:off
        RitualRegistry.registerRecipe(RitualBuilder.build("banana1", 0, 3, 3,
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
        ));
        RitualRegistry.registerRecipe(RitualBuilder.build("banana2", 0, 4, 4,
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
        ));
        RitualRegistry.registerRecipe(RitualBuilder.build("banana3", 0, 4, 4,
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
        ));
        // spotless:on
    }
}
