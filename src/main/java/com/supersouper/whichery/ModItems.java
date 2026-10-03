package com.supersouper.whichery;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.supersouper.whichery.common.items.ItemChalk;
import com.supersouper.whichery.common.items.ItemRawChickenThatMakesYouBecomeAVampire;
import com.supersouper.whichery.common.items.seeds.ItemSeedDevilsTrumpet;
import com.supersouper.whichery.common.items.seeds.ItemSeedHenbane;
import com.supersouper.whichery.common.items.seeds.ItemSeedLunaria;
import com.supersouper.whichery.common.items.seeds.ItemSeedMandrake;
import com.supersouper.whichery.common.items.seeds.ItemSeedMullein;
import com.supersouper.whichery.common.items.seeds.ItemSeedStJohnsWort;
import com.supersouper.whichery.common.items.seeds.ItemSeedWitchesThimble;

import cpw.mods.fml.common.registry.GameRegistry;

// Credit to Et Futurum (Requiem)
public enum ModItems {
    // spotless:off

    // make sure to leave a trailing comma
    CHALK(true, new ItemChalk(), "chalk"),
    WITCHES_THIMBLE_SEED(true, new ItemSeedWitchesThimble(), "seed_witches_thimble"),
    WITCH_PETAL(true, new Item().setTextureName("whichery:witch_petal").setUnlocalizedName("witch_petal"), "witch_petal"),
    MANDRAKE_SEED(true, new ItemSeedMandrake(), "seed_mandrake"),
    MANDRAKE_ROOT(true, new Item().setTextureName("whichery:mandrake_root").setUnlocalizedName("mandrake_root"), "mandrake_root"),
    DEVILS_TRUMPET_SEED(true, new ItemSeedDevilsTrumpet(), "seed_devils_trumpet"),
    DEVILS_TRUMPET_FLOWER(true, new Item().setTextureName("whichery:devils_trumpet").setUnlocalizedName("devils_trumpet"), "devils_trumpet"),
    HENBANE_SEED(true, new ItemSeedHenbane(), "seed_henbane"),
    HENBANE_FLOWER(true, new Item().setTextureName("whichery:henbane").setUnlocalizedName("henbane"), "henbane"),
    MULLEIN_SEED(true, new ItemSeedMullein(), "seed_mullein"),
    MULLEIN_WICK(true, new Item().setTextureName("whichery:mullein_wick").setUnlocalizedName("mullein_wick"), "mullein_wick"),
    LUNARIA_SEED_POD(true, new ItemSeedLunaria(), "lunaria_seed_pod"),
    LUNARIA_BLOOM(true, new Item().setTextureName("whichery:lunaria_bloom").setUnlocalizedName("lunaria_bloom"), "lunaria_bloom"),
    ST_JOHNS_WORT_SEED(true, new ItemSeedStJohnsWort(), "seed_st_johns_wort"),
    ST_JOHNS_WORT_FLOWER(true, new Item().setTextureName("whichery:st_johns_wort").setUnlocalizedName("st_johns_wort"), "st_johns_wort"),
    RAW_CHICKEN_THAT_MAKES_YOU_BECOME_A_VAMPIRE(true, new ItemRawChickenThatMakesYouBecomeAVampire().setUnlocalizedName("raw_chicken_that_makes_you_become_a_vampire").setTextureName("chicken_raw"), "raw_chicken_that_makes_you_become_a_vampire"),
    ; // leave trailing semicolon
    // spotless:on

    public static final ModItems[] VALUES = values();

    public static void init() {
        for (ModItems item : VALUES) {
            if (item.isEnabled()) {
                item.theItem.setCreativeTab(Whichery.whicheryTab);
                GameRegistry.registerItem(item.get(), item.name);
            }
        }
    }

    private final boolean isEnabled;
    private final Item theItem;
    private final String name;

    ModItems(boolean enabled, Item item, String name) {
        this.isEnabled = enabled;
        theItem = item;
        this.name = name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Item get() {
        return theItem;
    }

    public ItemStack newItemStack() {
        return newItemStack(1);
    }

    public ItemStack newItemStack(int count) {
        return newItemStack(count, 0);
    }

    public ItemStack newItemStack(int count, int meta) {
        return new ItemStack(this.get(), count, meta);
    }
}
