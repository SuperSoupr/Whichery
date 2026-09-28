package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCropWaterleafStem extends BlockBush implements IGrowable {

    // Offsets to check horizontally to spread water
    private static final int[][] OFFSETS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
    // Since the stem mimics some properties of water,
    // the metadata must be 8-15 to avoid overlapping with 0 (bucketable water) or 1-7 (non-full water).
    public static final int METADATA_BASE = 8;
    // The max amount of nutrients a waterleaf can store (which greenteeth takes from). Metadata caps this at 7.
    public static final int MAX_NUTRIENTS = 4;
    // The chance every random tick that waterleaf regains a nutrient. 1 in X.
    public static final int NUTRIENT_REGROWTH_CHANCE = 4;

    public static int renderID = 1;

    public BlockCropWaterleafStem() {
        super(Material.water);
        setBlockName("crop_waterleaf_stem");
        setBlockTextureName("whichery:crops/waterleaf/stem");
        setLightOpacity(Blocks.water.getLightOpacity());
        setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 1.0f, 0.7f);
        setStepSound(soundTypeGrass);
        // Add a little bit of hardness to the stem so you don't accidentally break it when harvesting the top.
        setHardness(0.5f);
        // Broken faster with an axe
        setHarvestLevel("axe", 0);
    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return pass <= 1;
    }

    // Helper function; grow the crop atop this stem (if there is room).
    // x, y, z is the position of the stem.
    public void growTop(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y + 1, z)) {
            world.setBlock(x, y + 1, z, ModBlocks.WATERLEAF.get(), 0, 3);
        }
    }

    // Returns the seed for pick-block.
    @Override
    public Item getItem(World worldIn, int x, int y, int z) {
        return ModItems.WATERLEAF_SEED.get();
    }

    // Check if the block below is valid (blocks found in riverbeds).
    @Override
    public boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.grass || ground == Blocks.dirt
            || ground == Blocks.sand
            || ground == Blocks.clay
            || ground == Blocks.gravel;
    }

    // Check if the stem is on a valid block, and drop it if not, replacing with a water source.
    @Override
    protected void checkAndDropBlock(World world, int x, int y, int z) {
        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, Blocks.water, 0, 3);
        }
    }

    // Unlike normal water, you can't place a block here.
    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    // Replace with water source when broken. Uses the 5-argument version that both client and server run
    // to avoid visual bugs.
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        return world.setBlock(x, y, z, Blocks.water, 0, 3);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(ModItems.WATERLEAF_SEED.newItemStack(1));
        return drops;
    }

    // Helper function; determines if a block is something water can flow into.
    // Things that do not hinder movement, but are not water/lava
    private boolean canFlowInto(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        Material material = block.getMaterial();
        if (material == Material.water || material == Material.lava) {
            return false;
        }
        if (block == Blocks.ladder || block == Blocks.reeds || block == Blocks.portal) {
            return false;
        }
        return !material.blocksMovement();
    }

    // Add the ability for the water in the stem to flow to nearby valid space.
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(world, x, y, z, neighbor);
        if (world.getBlock(x, y, z) != this) {
            return; // stem popped off, water handles itself
        }
        for (int[] offset : OFFSETS) {
            int nx = x + offset[0];
            int nz = z + offset[1];
            Block target = world.getBlock(nx, y, nz);
            if (canFlowInto(world, nx, y, nz)) {
                target.dropBlockAsItem(world, nx, y, nz, world.getBlockMetadata(nx, y, nz), 0);
                world.setBlock(nx, y, nz, Blocks.flowing_water, 1, 3);
            }
        }
    }

    // When the stem explodes, replace with water.
    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        world.setBlock(x, y, z, Blocks.water, 0, 3);
        onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        if (world.getBlock(x, y, z) != this) {
            return;
        }
        // Regrow the crop above if there is free space
        growTop(world, x, y, z);

        // Chance to regain lost nutrients
        int meta = world.getBlockMetadata(x, y, z);
        if (meta < METADATA_BASE + MAX_NUTRIENTS && random.nextInt(NUTRIENT_REGROWTH_CHANCE) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
        }
    }

    // Tries to spend a nutrient from a stem.
    // Returns true if a nutrient was successfully spent.
    public static boolean trySpendNutrient(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (world.getBlock(x, y, z) == ModBlocks.WATERLEAF_STEM.get() && meta > METADATA_BASE) {
            world.setBlockMetadataWithNotify(x, y, z, meta - 1, 2);
            return true;
        }
        return false;
    }

    // Can bone meal if block above stem is air
    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {
        return world.isAirBlock(x, y + 1, z);
    }

    // Bone meal always succeeds
    @Override
    public boolean func_149852_a(World world, Random random, int x, int y, int z) {
        return true;
    }

    // Bone meal places crop above
    // Intentionally does not return nutrients to the stem, to make infinite greenteeth harder
    @Override
    public void func_149853_b(World world, Random random, int x, int y, int z) {
        growTop(world, x, y, z);
    }

    // Because the stem is underwater, the part of the stem that connects into the main crop is much darker
    // than the crop. To avoid this, we take the brighter of this block or the block above.
    // Compares both sky & block light.
    @Override
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
        int here = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int above = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
        int hereBlock = here & 255;
        int aboveBlock = above & 255;
        int hereSky = here >> 16 & 255;
        int aboveSky = above >> 16 & 255;
        return Math.max(hereBlock, aboveBlock) | Math.max(hereSky, aboveSky) << 16;
    }

    // Helper functions to let other classes access nutrient data.
    public static int getNutrients(int meta) {
        return meta - METADATA_BASE;
    }

    public static int toMeta(int nutrients) {
        return nutrients + METADATA_BASE;
    }
}
