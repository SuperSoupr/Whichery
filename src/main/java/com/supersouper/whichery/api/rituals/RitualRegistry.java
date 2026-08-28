package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.ApiStatus;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.client.render.ChalkRuneISBRH;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RitualRegistry {

    // Rituals
    private static final HashMap<String, Ritual> RITUALS = new HashMap<>();
    static final HashMap<Item, Function<ItemStack, Integer>> ITEM_HASHERS = new HashMap<>();

    public static void registerRitual(Ritual ritual) {
        RITUALS.put(ritual.name, ritual);
    }

    public static Collection<Ritual> rituals() {
        return RITUALS.values();
    }

    public static Ritual getRitual(String name) {
        return RITUALS.get(name);
    }

    public static void registerItemHasher(Item item, Function<ItemStack, Integer> hasher) {
        ITEM_HASHERS.put(item, hasher);
    }

    // Chalk Types

    public static final HashMap<String, ChalkType> CHALK_TYPES = new HashMap<>();
    private static List<String> CHALK_TYPES_LIST;
    public static final String DEFAULT_CHALK_TYPE_NAME = "basic";

    public static void registerChalkType(ChalkType chalkType) {
        CHALK_TYPES.put(chalkType.name, chalkType);
    }

    public static boolean chalkExists(String name) {
        return CHALK_TYPES.containsKey(name);
    }

    @ApiStatus.Internal
    public static void finalizeChalkTypes() {
        if (CHALK_TYPES_LIST == null) {
            CHALK_TYPES_LIST = Collections.unmodifiableList(new ArrayList<>(CHALK_TYPES.keySet()));
        }
    }

    public static List<String> getChalkTypeList() {
        return CHALK_TYPES_LIST;
    }

    // Rune textures
    public static final int CHALK_STICK_ICON_COUNT = 6;

    @SideOnly(Side.CLIENT)
    @ApiStatus.Internal
    public static IIcon registerRuneIcons(IIconRegister reg) {
        ChalkRuneISBRH.storageIcon = reg.registerIcon(Whichery.MODID + ":chalk");

        ChalkType type = CHALK_TYPES.get(DEFAULT_CHALK_TYPE_NAME);
        int baseCount = type.runeCount;
        IIcon[] baseIcons = new IIcon[baseCount];
        IIcon[] baseIconsSmall = new IIcon[baseCount];
        for (int i = 0; i < baseIcons.length; i++) {
            baseIcons[i] = reg.registerIcon(Whichery.MODID + ":runes/" + DEFAULT_CHALK_TYPE_NAME + "/large/rune_" + i);
            baseIconsSmall[i] = reg
                .registerIcon(Whichery.MODID + ":runes/" + DEFAULT_CHALK_TYPE_NAME + "/small/rune_" + i);
        }
        boolean[] baseUniques = new boolean[baseCount];
        Arrays.fill(baseUniques, true);
        type.icons = baseIcons;
        type.uniques = baseUniques;
        type.iconsSmall = baseIconsSmall;
        type.uniquesSmall = baseUniques;

        for (Map.Entry<String, ChalkType> entry : CHALK_TYPES.entrySet()) {

            String name = entry.getKey();
            if (name.equals(DEFAULT_CHALK_TYPE_NAME)) continue;
            type = entry.getValue();

            IIcon[] icons = type.icons;
            boolean[] uniques = type.uniques;
            if (icons == null) {
                icons = new IIcon[type.runeCount];
                uniques = new boolean[type.runeCount];
            }
            IIcon[] iconsSmall = type.iconsSmall;
            boolean[] iconsSmallUniques = type.uniquesSmall;
            if (iconsSmall == null) {
                iconsSmall = new IIcon[type.runeCount];
                iconsSmallUniques = new boolean[type.runeCount];
            }

            for (int i = 0; i < icons.length; i++) {
                ResourceLocation iconLocation = new ResourceLocation(
                    type.domain + ":textures/blocks/runes/" + name + "/large/rune_" + i + ".png");
                if (WhicheryUtils.resourceExists(iconLocation)) {
                    icons[i] = reg.registerIcon(type.domain + ":runes/" + name + "/large/rune_" + i);
                    uniques[i] = true;
                } else {
                    icons[i] = baseIcons[i];
                }

                ResourceLocation iconLocationSmall = new ResourceLocation(
                    type.domain + ":textures/blocks/runes/" + name + "/small/rune_" + i + ".png");
                if (WhicheryUtils.resourceExists(iconLocationSmall)) {
                    iconsSmall[i] = reg.registerIcon(type.domain + ":runes/" + name + "/small/rune_" + i);
                    iconsSmallUniques[i] = true;
                } else {
                    iconsSmall[i] = baseIconsSmall[i];
                }
            }

            type.icons = icons;
            type.uniques = uniques;
            type.iconsSmall = iconsSmall;
            type.uniquesSmall = iconsSmallUniques;

        }
        return baseIcons[0];
    }

    @SideOnly(Side.CLIENT)
    @ApiStatus.Internal
    public static IIcon registerChalkStickIcons(IIconRegister reg) {

        ChalkType type = CHALK_TYPES.get(DEFAULT_CHALK_TYPE_NAME);
        IIcon[] baseIcons = new IIcon[CHALK_STICK_ICON_COUNT];
        for (int i = 0; i < CHALK_STICK_ICON_COUNT; i++) {
            baseIcons[i] = reg
                .registerIcon(Whichery.MODID + ":chalk_stick/" + DEFAULT_CHALK_TYPE_NAME + "/chalk_stick_" + i);
        }
        boolean[] baseUniques = new boolean[CHALK_STICK_ICON_COUNT];
        Arrays.fill(baseUniques, true);
        type.iconsStick = baseIcons;
        type.uniquesStick = baseUniques;

        for (Map.Entry<String, ChalkType> entry : CHALK_TYPES.entrySet()) {

            String name = entry.getKey();
            if (name.equals(DEFAULT_CHALK_TYPE_NAME)) continue;
            type = entry.getValue();

            IIcon[] icons = new IIcon[CHALK_STICK_ICON_COUNT];
            boolean[] uniques = new boolean[CHALK_STICK_ICON_COUNT];
            for (int i = 0; i < CHALK_STICK_ICON_COUNT; i++) {
                ResourceLocation iconLocation = new ResourceLocation(
                    type.domain + ":textures/items/chalk_stick/" + name + "/chalk_stick_" + i + ".png");
                if (WhicheryUtils.resourceExists(iconLocation)) {
                    icons[i] = reg.registerIcon(type.domain + ":chalk_stick/" + name + "/chalk_stick_" + i);
                    uniques[i] = true;
                } else {
                    icons[i] = baseIcons[i];
                }
            }
            type.iconsStick = icons;
            type.uniquesStick = uniques;
        }

        return baseIcons[0];
    }
}
