package com.supersouper.whichery;

import com.supersouper.whichery.common.blocks.trees.BlockWhicheryPlanks;
import com.supersouper.whichery.common.blocks.trees.BlockYewLeaves;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.supersouper.whichery.common.blocks.BlockChalk;
import com.supersouper.whichery.common.blocks.crops.BlockCropWitchesThimble;
import com.supersouper.whichery.common.blocks.trees.BlockAlderLeaves;
import com.supersouper.whichery.common.blocks.trees.BlockHawthornLeaves;
import com.supersouper.whichery.common.blocks.trees.BlockRowanLeaves;
import com.supersouper.whichery.common.blocks.trees.BlockWhicheryLog;

import cpw.mods.fml.common.registry.GameRegistry;

// Credit to Et Futurum (Requiem)
public enum ModBlocks {
    // spotless:off

    // make sure to leave a trailing comma
    CHALK_BLOCK(true, new BlockChalk(), "chalk_block"),
    WITCHES_THIMBLE(true, new BlockCropWitchesThimble("witches_thimble", 4), "crop_witches_thimble"),

    // Trees
    ALDER_LOG(true, new BlockWhicheryLog("alder"), "alder_log"),
    HAWTHORN_LOG(true, new BlockWhicheryLog("hawthorn"), "hawthorn_log"),
    ROWAN_LOG(true, new BlockWhicheryLog("rowan"), "rowan_log"),
    YEW_LOG(true, new BlockWhicheryLog("yew"), "yew_log"),

    ALDER_LEAVES(true, new BlockAlderLeaves(), "alder_leaves"),
    HAWTHORN_LEAVES(true, new BlockHawthornLeaves(), "hawthorn_leaves"),
    ROWAN_LEAVES(true, new BlockRowanLeaves(), "rowan_leaves"),
    YEW_LEAVES(true, new BlockYewLeaves(), "yew_leaves"),

    ALDER_PLANKS(true, new BlockWhicheryPlanks("alder"), "alder_planks"),
    HAWTHORN_PLANKS(true, new BlockWhicheryPlanks("hawthorn"), "hawthorn_planks"),
    ROWAN_PLANKS(true, new BlockWhicheryPlanks("rowan"), "rowan_planks"),
    YEW_PLANKS(true, new BlockWhicheryPlanks("yew"), "yew_planks"),

    ; // leave trailing semicolon
    // spotless:on

    public static final ModBlocks[] VALUES = values();

    public static void init() {
        for (ModBlocks block : VALUES) {
            if (block.isEnabled()) {
                block.theBlock.setCreativeTab(Whichery.whicheryTab);
                if (block.getItemBlock() != null || !block.getHasItemBlock()) {
                    GameRegistry.registerBlock(block.get(), block.getItemBlock(), block.name);
                    // This part is used if the getItemBlock() is not ItemBlock.class, so we register a custom ItemBlock
                    // class as the ItemBlock
                    // It is also used if the getItemBlock() == null and getHasItemBlock() is false, meaning we WANT to
                    // register it as null, making the block have no inventory item.
                } else {
                    GameRegistry.registerBlock(block.get(), block.name);
                    // Used if getItemBlock() == null but getHasItemBlock() is true, registering it with a default
                    // inventory item.
                }
            }
        }
    }

    private final boolean isEnabled;
    private final Block theBlock;
    /**
     * null == default ItemBlock
     */
    private final Class<? extends ItemBlock> itemBlock;
    /**
     * Determines if we should register the block with an ItemBlock.
     * Set to false when the constructor that specifies the ItemBlock is specifically set to false.
     */
    private boolean hasItemBlock;
    private final String name;

    ModBlocks(Boolean enabled, Block block, String name) {
        this(enabled, block, null, name);
        hasItemBlock = true;
    }

    ModBlocks(Boolean enabled, Block block, Class<? extends ItemBlock> iblock, String name) {
        isEnabled = enabled;
        theBlock = block;
        itemBlock = iblock;
        hasItemBlock = iblock != null;
        this.name = name;
    }

    /**
     * If this is false, the block is initialized without an inventory item, or ItemBlock.
     */
    public boolean getHasItemBlock() {
        return hasItemBlock;
    }

    public Block get() {
        return theBlock;
    }

    public Class<? extends ItemBlock> getItemBlock() {
        return itemBlock;
    }

    public Item getItem() {
        return Item.getItemFromBlock(get());
    }

    public boolean isEnabled() {
        return isEnabled;
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
