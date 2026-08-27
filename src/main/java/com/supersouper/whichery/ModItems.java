package com.supersouper.whichery;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.supersouper.whichery.common.items.ItemChalk;
import com.supersouper.whichery.common.items.ItemRawChickenThatMakesYouBecomeAVampire;
import com.supersouper.whichery.common.items.seeds.ItemSeedMandrake;
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
