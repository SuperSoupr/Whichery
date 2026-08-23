package com.supersouper.whichery.common.recipe;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.ApiStatus;

import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.RitualBuilder;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.matching.BlockMatcherBasic;
import com.supersouper.whichery.api.rituals.matching.ISecondaryMatcher;
import com.supersouper.whichery.common.rituals.animations.FlyingSmallRunesAnimation;
import com.supersouper.whichery.common.rituals.animations.TestRitualAnimation;
import com.supersouper.whichery.common.rituals.effects.ChalkItemConsumeEffect;
import com.supersouper.whichery.common.rituals.effects.TestRitualEffect;
import com.supersouper.whichery.common.rituals.matching.BlockMatcherChalk;
import com.supersouper.whichery.common.rituals.matching.BlockMatcherChalkSmall;
import com.supersouper.whichery.common.rituals.matching.ChalkItemSecondaryMatcher;
import com.supersouper.whichery.common.rituals.matching.TestSecondaryMatcher;

@ApiStatus.Internal
public class RitualRecipeLoader {

    @ApiStatus.Internal
    public static void loadRecipes() {

        int[] stages = new int[30];
        Arrays.fill(stages, 10);

        // spotless:off
        RitualRegistry.registerRitual(new Ritual("banana1", RitualBuilder.buildRecipe((byte) 3, (byte) 0, (byte) 3, new ISecondaryMatcher[]{
        new ChalkItemSecondaryMatcher(new ItemStack(Items.stick))},
            new String[]{
                "  ccc  ",
                " c   c ",
                "c   o c",
                "c  c  c",
                "c     c",
                " c   c ",
                "  ccc  ",
            },
            'c',
            new BlockMatcherChalk("spiritual"),
            'o',
            new BlockMatcherBasic(Blocks.obsidian)
        ), TestRitualEffect.class, TestRitualAnimation.class, stages));

        RitualRegistry.registerRitual(new Ritual("banana2", RitualBuilder.buildRecipe((byte) 4, (byte) 0, (byte) 4, new ISecondaryMatcher[]{
                new TestSecondaryMatcher(), new ChalkItemSecondaryMatcher(new ItemStack(Items.stick), new ItemStack(Items.gold_ingot)), new ChalkItemSecondaryMatcher(new ItemStack(Items.diamond), new ItemStack(Items.emerald), true)},
            new String[]{
                "g       g",
                "   ccc   ",
                "  c   c  ",
                " c s s c ",
                " c  c  c ",
                " c s s c ",
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
            new BlockMatcherChalk("spiritual"),
            's',
            new BlockMatcherChalkSmall("spiritual"),
            'g',
            new BlockMatcherBasic(Blocks.glowstone)
        ), new Class[] {ChalkItemConsumeEffect.class, TestRitualEffect.class}, new Class[] {FlyingSmallRunesAnimation.class}, new int[]{10, 10, 10, 10, 10, 10, 10, 10}));


        RitualRegistry.registerRitual(new Ritual("banana3", RitualBuilder.buildRecipe((byte) 4, (byte) 0, (byte) 4, null,
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
            new BlockMatcherChalk("spiritual")
        ), TestRitualEffect.class, TestRitualAnimation.class, new int[]{10, 10}));
        // spotless:on
    }
}
