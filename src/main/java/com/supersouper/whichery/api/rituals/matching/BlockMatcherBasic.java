package com.supersouper.whichery.api.rituals.matching;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.utils.DrawUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMatcherBasic implements IBlockMatcher {

    public final Block block;
    public final int meta;
    public final Item item;
    protected ItemStack asItemStack;

    public BlockMatcherBasic(Block block) {
        this(block, 0);
    }

    public BlockMatcherBasic(Block block, int meta) {
        this.block = block;
        this.meta = meta;
        this.item = Item.getItemFromBlock(block);
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        return world.getBlock(x, y, z) == block && world.getBlockMetadata(x, y, z) == meta;
    }

    public ItemStack toItemStack() {
        return new ItemStack(item, 1, meta);
    }

    @Override
    public ItemStack getItemStack() {
        if (asItemStack == null) {
            asItemStack = toItemStack();
        }
        return asItemStack;
    }

    @Override
    public int[] itemStackHashCodes() {
        return new int[] { item.hashCode() + meta };
    }

    @Override
    public void place(World world, int x, int y, int z) {
        world.setBlock(x, y, z, block);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawIcon(Tessellator t, TileEntity te, int x, int y, int z, int w, int h, double alpha) {
        IIcon icon = block.getIcon(1, meta);
        // RitualPreviewRenderer.setUniformsFromIcon(icon);
        DrawUtils.drawRect(t, x, y, z, w, h, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV());
    }
}
