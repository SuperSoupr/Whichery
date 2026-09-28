package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCropGreenteeth extends BlockBush implements IGrowable {

    // Below are two limits that define how greenteeth spreads. The per-block values are stored in the metadata.
    // The limits must be set such that (MAX_DISTANCE * (MAX_SPREADS + 1) + MAX_SPREADS ) < 16.

    // How many blocks a greenteeth bloom can grow away from its "seed" point. Greenteeth starts at 0 distance,
    // and every growth outward gains 1 point; at MAX_DISTANCE, it is starved and cannot grow further.
    // Distance can be reset to 0 by stealing nutrients from nearby crops.
    public static final int MAX_DISTANCE = 2;
    // How many times a single greenteeth block can spread to a neighbor. This is designed so that you cannot
    // infinitely remove the algae around a single seed point and have it spread outwards forever.
    public static final int MAX_SPREADS = 4;
    private static final int[][] OFFSETS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    @SideOnly(Side.CLIENT)
    private IIcon sparseIcon;
    @SideOnly(Side.CLIENT)
    private IIcon solidIcon;

    public BlockCropGreenteeth() {
        setBlockName("crop_greenteeth");
        setStepSound(soundTypeGrass);
        setBlockBounds(0, 0, 0, 1, 0.015625f, 1);
    }

    // Only stays on still source water.
    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Block below = world.getBlock(x, y - 1, z);
        int meta = world.getBlockMetadata(x, y - 1, z);
        return below.getMaterial() == Material.water && meta == 0;
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.water;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        if (world.getBlock(x, y, z) != this) {
            return;
        }
        tryGrow(world, x, y, z, random);
    }

    // Tries to grow into a free neighboring block. Returns true if it successfully grew.
    private boolean tryGrow(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z);
        // If the greenteeth can't grow, stop
        if (!canGrow(meta)) {
            return false;
        }
        int distance = getDistance(meta);
        int spread = getSpread(meta);
        ArrayList<int[]> freeNeighbors = new ArrayList<int[]>();
        for (int[] offset : OFFSETS) {
            if (canBlockStay(world, x + offset[0], y, z + offset[1])
                && world.isAirBlock(x + offset[0], y, z + offset[1])) {
                freeNeighbors.add(offset);
            }
        }

        if (freeNeighbors.isEmpty()) {
            // If there are no free neighbors, the greenteeth "dies"
            world.setBlockMetadataWithNotify(x, y, z, toMeta(MAX_SPREADS, distance), 2);
        } else {
            // Spread to a random neighbor
            int[] offset = freeNeighbors.get(random.nextInt(freeNeighbors.size()));
            // Look for nutrients
            boolean hasNutrients = tryTakeNutrients(world, x + offset[0], y, z + offset[1]);
            world.setBlock(x + offset[0], y, z + offset[1], this, toMeta(0, hasNutrients ? 0 : distance + 1), 3);
            // Update this block's spread count
            world.setBlockMetadataWithNotify(x, y, z, toMeta(spread + 1, distance), 2);
            return true;
        }
        return false;
    }

    // Can bone meal be used on this block? If false, the bone meal isn't used up.
    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {
        return canGrow(world.getBlockMetadata(x, y, z));
    }

    // Bone meal always succeeds
    @Override
    public boolean func_149852_a(World world, Random random, int x, int y, int z) {
        return true;
    }

    // Bone meal effect
    @Override
    public void func_149853_b(World world, Random random, int x, int y, int z) {
        tryGrow(world, x, y, z, random);
        tryGrow(world, x, y, z, random);
    }

    // Checks for nutrients around a new greenteeth. Returns true if nutrients were found & taken.
    private boolean tryTakeNutrients(World world, int x, int y, int z) {
        for (int[] offset : OFFSETS) {
            if (world.getBlock(x + offset[0], y - 1, z + offset[1]) == ModBlocks.WATERLEAF_STEM.get()) {
                if (BlockCropWaterleafStem.trySpendNutrient(world, x + offset[0], y - 1, z + offset[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    // Every block drops one Greenteeth, alive or dead.
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(ModItems.GREENTEETH.newItemStack(1));
        return drops;
    }

    // Returns the Greenteeth item for pick-block.
    @Override
    public Item getItem(World world, int x, int y, int z) {
        return ModItems.GREENTEETH.get();
    }

    @Override
    public int getRenderType() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        solidIcon = reg.registerIcon("whichery:crops/greenteeth/solid");
        sparseIcon = reg.registerIcon("whichery:crops/greenteeth/sparse");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return getSpread(meta) < MAX_SPREADS ? sparseIcon : solidIcon;
    }

    // Mimics grass by matching biome color.
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                // Average color over a 3x3
                int color = world.getBiomeGenForCoords(x + dx, z + dz)
                    .getBiomeGrassColor(x + dx, y, z + dz);
                rSum += color >> 16 & 255;
                gSum += color >> 8 & 255;
                bSum += color & 255;
            }
        }
        // Re-combine RGB
        return rSum / 9 << 16 | gSum / 9 << 8 | bSum / 9;
    }

    // Get distance level
    public static int getDistance(int metadata) {
        return metadata / (MAX_SPREADS + 1);
    }

    // Get spread level
    public static int getSpread(int metadata) {
        return metadata % (MAX_SPREADS + 1);
    }

    // Helper function; determines if greenteeth can keep growing, or
    // if it is starved (either via distance to nutrients, or having
    // already grown too many neighbors).
    public static boolean canGrow(int metadata) {
        return getDistance(metadata) < MAX_DISTANCE && getSpread(metadata) < MAX_SPREADS;
    }

    // Converts spread and distance levels to metadata.
    public static int toMeta(int spread, int distance) {
        return distance * (MAX_SPREADS + 1) + spread;
    }
}
