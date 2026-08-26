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
            BlockMatcherChalk.of("spiritual"),
            'o',
            new BlockMatcherBasic(Blocks.obsidian)
        ), TestRitualEffect.class, TestRitualAnimation.class, stages));

        RitualRegistry.registerRitual(new Ritual("banana2", RitualBuilder.buildRecipe((byte) 4, (byte) 1, (byte) 4, new ISecondaryMatcher[]{
                new TestSecondaryMatcher(), new ChalkItemSecondaryMatcher(new ItemStack(Items.stick), new ItemStack(Items.gold_ingot)), new ChalkItemSecondaryMatcher(new ItemStack(Items.diamond), new ItemStack(Items.emerald), true)},
            new String[]{
                "         ",
                "         ",
                "   mmm   ",
                "  m   m  ",
                "  m   m  ",
                "  m   m  ",
                "   mmm   ",
                "         ",
                "         ",
            },
            new String[]{
                "g       g",
                "   012   ",
                "  3   3  ",
                " 2 S s 4 ",
                " 1  4  5 ",
                " 0 s S 6 ",
                "  B   7  ",
                "   A98   ",
                "g       g",
            },
            new String[]{
                "b       g",
                "         ",
                "         ",
                "         ",
                "         ",
                "         ",
                "         ",
                "         ",
                "g       b",
            },
            'm',
            new BlockMatcherBasic(Blocks.stone),
            '0',
            BlockMatcherChalk.of("spiritual", 0),
            '1',
            BlockMatcherChalk.of("spiritual", 1),
            '2',
            BlockMatcherChalk.of("spiritual", 2),
            '3',
            BlockMatcherChalk.of("spiritual", 3),
            '4',
            BlockMatcherChalk.of("spiritual", 4),
            '5',
            BlockMatcherChalk.of("spiritual", 5),
            '6',
            BlockMatcherChalk.of("spiritual", 6),
            '7',
            BlockMatcherChalk.of("spiritual", 7),
            '8',
            BlockMatcherChalk.of("spiritual", 8),
            '9',
            BlockMatcherChalk.of("spiritual", 9),
            'A',
            BlockMatcherChalk.of("spiritual", 10),
            'B',
            BlockMatcherChalk.of("spiritual", 11),
            'S',
            BlockMatcherChalkSmall.of("spiritual", 0, "bloody", 0),
            's',
            BlockMatcherChalkSmall.of("spiritual", "bloody"),
            'g',
            new BlockMatcherBasic(Blocks.glowstone),
            'b',
            new BlockMatcherBasic(Blocks.obsidian)
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
            BlockMatcherChalk.of("spiritual")
        ), TestRitualEffect.class, TestRitualAnimation.class, new int[]{10, 10}));
        // spotless:on
    }
}
