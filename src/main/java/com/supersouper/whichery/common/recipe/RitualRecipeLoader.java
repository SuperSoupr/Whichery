package com.supersouper.whichery.common.recipe;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.ApiStatus;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.BlockMatcherBasic;
import com.supersouper.whichery.api.rituals.BlockMatcherChalk;
import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.RitualBuilder;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.rituals.animations.TestRitualAnimation;
import com.supersouper.whichery.common.rituals.effects.TestRitualEffect;

@ApiStatus.Internal
public class RitualRecipeLoader {

    @ApiStatus.Internal
    public static void loadRecipes() {

        int[] stages = new int[60];
        Arrays.fill(stages, 10);

        // spotless:off
        RitualRegistry.registerRitual(new Ritual("banana1", RitualBuilder.buildRecipe((byte) 3, (byte) 0, (byte) 3,
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
            new BlockMatcherChalk(ModBlocks.CHALK_BLOCK.get(), 1),
            'o',
            new BlockMatcherBasic(Blocks.obsidian)
        ), TestRitualEffect.class, TestRitualAnimation.class, stages));
        RitualRegistry.registerRitual(new Ritual("banana2", RitualBuilder.buildRecipe((byte) 4, (byte) 0, (byte) 4,
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
        ), TestRitualEffect.class, TestRitualAnimation.class, new int[]{10, 10}));
        RitualRegistry.registerRitual(new Ritual("banana3", RitualBuilder.buildRecipe((byte) 4, (byte) 0, (byte) 4,
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
        ), TestRitualEffect.class, TestRitualAnimation.class, new int[]{10, 10}));

        ItemStack stack = new ItemStack(Items.diamond_axe, 1, 1);
        NBTTagCompound tag = new NBTTagCompound();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            tag.setString("testbttag" + rand.nextInt(696969), "testbtvalue" + rand.nextInt(696969));
        }
        stack.setTagCompound(tag);

        String s = "";
        for (int i = 0; i < 20; i++) {
            s = s + "c";
        }
        String[] sarr = new String[20];
        for (int i = 0; i < 20; i++) {
            sarr[i] = s;
        }

        for (int i = 0; i < 10000; i++) {
            RitualRegistry.registerRitual(new Ritual("testritual" + i, RitualBuilder.buildRecipe((byte) 4, (byte) 0, (byte) 4,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                sarr,
                'c',
                new BlockMatcherChalk(ModBlocks.CHALK_BLOCK.get(), 1, stack)
            ), TestRitualEffect.class, TestRitualAnimation.class, new int[]{10, 10}));
        }
        // spotless:on
    }
}
