package com.supersouper.whichery.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMatcherBasic implements IBlockMatcher {

    protected final Block block;
    protected final int meta;
    protected final Item item;
    protected ItemStack asItemStack;

    public BlockMatcherBasic(Block block) {
        this(block, 0);
    }

    public BlockMatcherBasic(Block block, int meta) {
        this.block = block;
        this.meta = meta;
        this.item = Item.getItemFromBlock(block);
        this.asItemStack = toItemStack();
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z) {
        return world.getBlock(x, y, z) == block && world.getBlockMetadata(x, y, z) == meta;
    }

    @Override
    public ItemStack toItemStack() {
        return new ItemStack(item, 1, meta);
    }

    @Override
    public ItemStack getItemStack() {
        return asItemStack;
    }

    @Override
    public int itemStackHashCode() {
        return item.hashCode() + meta;
    }

    @Override
    public void place(World world, int x, int y, int z) {
        world.setBlock(x, y, z, block);
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }
}
