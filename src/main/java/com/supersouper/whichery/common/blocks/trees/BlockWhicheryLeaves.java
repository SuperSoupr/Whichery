package com.supersouper.whichery.common.blocks.trees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockWhicheryLeaves extends BlockLeaves {

    IIcon fastTex;
    IIcon fancyTex;
    String treeName;

    public BlockWhicheryLeaves(String treeName) {
        super();
        this.treeName = treeName;
        setBlockName(treeName + "_leaves");
    }

    /**
     * Add sapling
     */
    @Override
    public abstract Item getItemDropped(int meta, Random rand, int fortune);

    @Override
    public final String[] func_150125_e() {
        return new String[] { getTextureName(), getTextureName() };
    }

    @Override
    protected void func_150124_c(World world, int x, int y, int z, int meta, int chance) {
        getAdditionalDrops(world, x, y, z, meta, chance);
    }

    /**
     * Wrapper for {@link #func_150124_c(World, int, int, int, int, int)}. Add additional drops like apples
     */
    protected void getAdditionalDrops(World world, int x, int y, int z, int meta, int chance) {}

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        fastTex = reg.registerIcon("whichery:trees/" + treeName + "_leaves_fast");
        fancyTex = reg.registerIcon("whichery:trees/" + treeName + "_leaves");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.isOpaqueCube() ? fastTex : fancyTex;
    }

    @Override
    public boolean isOpaqueCube() {
        return Blocks.leaves.isOpaqueCube();
    }

    // Have to basically reimplement super from both BlockLeaves and Block without calling super due to how the
    // stupid fast/fancy boolean field works
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side) {
        Block block = worldIn.getBlock(x, y, z);
        return (!isOpaqueCube() || block instanceof BlockLeaves) && (side == 0 && this.minY > 0.0D
            || (side == 1 && this.maxY < 1.0D || (side == 2 && this.minZ > 0.0D || (side == 3 && this.maxZ < 1.0D
                || (side == 4 && this.minX > 0.0D || (side == 5 && this.maxX < 1.0D || !worldIn.getBlock(x, y, z)
                    .isOpaqueCube()))))));
    }

    // Disable biome blend
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return Integer.MAX_VALUE;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return Integer.MAX_VALUE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
        return Integer.MAX_VALUE;
    }
}
