package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockWhicheryCrop extends BlockCrops {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    protected final String id;
    protected final int maxStage;

    public BlockWhicheryCrop(String id, int maxStage) {
        super();
        this.id = id;
        this.maxStage = maxStage;
        this.setBlockName("crop_" + id);
    }

    @Override
    public boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.farmland;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;
        int meta = world.getBlockMetadata(x, y, z);
        if (meta >= maxStage) return;

        world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    }

    // Wrappers for deobf methods, for the sake of sanity
    protected abstract Item getSeedItem();

    protected boolean canFertilize(World world, Random random, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta < maxStage;
    }

    protected void fertilize(World world, Random rand, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta < maxStage) {
            world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
        }
    }

    @Override
    protected final Item func_149866_i() {
        return getSeedItem();
    }

    @Override
    public final boolean func_149852_a(World world, Random random, int x, int y, int z) {
        return canFertilize(world, random, x, y, z);
    }

    @Override
    public final void func_149853_b(World world, Random rand, int x, int y, int z) {
        fertilize(world, rand, x, y, z);
    }

    public void addDropsGrown(World world, int x, int y, int z, int metadata, int fortune,
        ArrayList<ItemStack> drops) {}

    public void addDropsAlways(World world, int x, int y, int z, int metadata, int fortune,
        ArrayList<ItemStack> drops) {
        drops.add(new ItemStack(getSeedItem(), 1));
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        addDropsAlways(world, x, y, z, metadata, fortune, drops);
        if (metadata >= maxStage) addDropsGrown(world, x, y, z, metadata, fortune, drops);

        return drops;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        icons = new IIcon[maxStage + 1];
        for (int i = 0; i <= maxStage; i++) {
            icons[i] = reg.registerIcon("whichery:crops/" + id + "/stage_" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0) meta = 0;
        if (meta > maxStage) meta = maxStage;
        return icons[meta];
    }

    @Override
    public int getRenderType() {
        return 1;
    }
}
