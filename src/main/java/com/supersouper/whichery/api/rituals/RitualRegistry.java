package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
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
    @SideOnly(Side.CLIENT)
    public static HashMap<String, IIcon[]> RUNE_ICONS;
    @SideOnly(Side.CLIENT)
    public static HashMap<String, IIcon[]> RUNE_ICONS_SMALL;

    @SideOnly(Side.CLIENT)
    @ApiStatus.Internal
    public static IIcon registerRuneIcons(IIconRegister reg) {
        int baseCount = CHALK_TYPES.get(DEFAULT_CHALK_TYPE_NAME).runeCount;
        IIcon[] baseIcons = new IIcon[baseCount];
        IIcon[] baseIconsSmall = new IIcon[baseCount];
        for (int i = 0; i < baseIcons.length; i++) {
            baseIcons[i] = reg
                .registerIcon(Whichery.MODID + ":runes/" + RitualRegistry.DEFAULT_CHALK_TYPE_NAME + "/large/rune_" + i);
            baseIconsSmall[i] = reg
                .registerIcon(Whichery.MODID + ":runes/" + RitualRegistry.DEFAULT_CHALK_TYPE_NAME + "/small/rune_" + i);
        }
        RitualRegistry.RUNE_ICONS.put(RitualRegistry.DEFAULT_CHALK_TYPE_NAME, baseIcons);
        RitualRegistry.RUNE_ICONS_SMALL.put(RitualRegistry.DEFAULT_CHALK_TYPE_NAME, baseIconsSmall);

        for (Map.Entry<String, ChalkType> enty : RitualRegistry.CHALK_TYPES.entrySet()) {

            String name = enty.getKey();
            if (name.equals(RitualRegistry.DEFAULT_CHALK_TYPE_NAME)) continue;
            ChalkType type = enty.getValue();

            IIcon[] icons = RitualRegistry.RUNE_ICONS.get(name);
            if (icons == null) {
                icons = new IIcon[type.runeCount];
            }
            IIcon[] iconsSmall = RitualRegistry.RUNE_ICONS_SMALL.get(name);
            if (iconsSmall == null) {
                iconsSmall = new IIcon[type.runeCount];
            }

            for (int i = 0; i < icons.length; i++) {
                ResourceLocation iconLocation = new ResourceLocation(
                    type.domain + ":textures/blocks/runes/" + name + "/large/rune_" + i + ".png");
                if (WhicheryUtils.resourceExists(iconLocation)) {
                    icons[i] = reg.registerIcon(type.domain + ":runes/" + name + "/large/rune_" + i);
                } else {
                    icons[i] = baseIcons[i];
                }

                ResourceLocation iconLocationSmall = new ResourceLocation(
                    type.domain + ":textures/blocks/runes/" + name + "/small/rune_" + i + ".png");
                if (WhicheryUtils.resourceExists(iconLocationSmall)) {
                    iconsSmall[i] = reg.registerIcon(type.domain + ":runes/" + name + "/small/rune_" + i);
                } else {
                    iconsSmall[i] = baseIconsSmall[i];
                }
            }

            RitualRegistry.RUNE_ICONS.put(name, icons);
            RitualRegistry.RUNE_ICONS_SMALL.put(name, iconsSmall);

        }
        return baseIcons[0];
    }
}
