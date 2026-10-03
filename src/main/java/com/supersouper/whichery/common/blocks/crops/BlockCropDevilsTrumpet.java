package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Devil's trumpet hangs down from the underside of a block near an active Nether portal, growing a short column whose
 * tip buds and opens into a flower at night.
 */
public class BlockCropDevilsTrumpet extends BlockBush implements IGrowable {

    // How close an active Nether portal has to be for devil's trumpet to grow (it must "hear hell's passage").
    private static final int PORTAL_RANGE_HORIZONTAL = 4;
    private static final int PORTAL_RANGE_VERTICAL = 3;
    // The most blocks a column can hang down
    public static final int MAX_LENGTH = 3;

    // Metadata: stems hold the column up; the lowest block is always a bud, a mature bud, or a flower.
    public static final int META_STEM = 0;
    public static final int META_BUD = 1;
    public static final int META_BUD_MATURE = 2;
    public static final int META_FLOWER = 3;
    // A bud grows on 1 in this many random ticks
    private static final int GROWTH_CHANCE = 5;
    // An open flower lets out a particle on 1 in this many display ticks (every few seconds when you're close)
    private static final int PARTICLE_CHANCE = 12;

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockCropDevilsTrumpet() {
        super(Material.plants);
        setBlockName("crop_devils_trumpet");
        setBlockTextureName("whichery:crops/devils_trumpet");
        setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
        setStepSound(soundTypeGrass);
        setHardness(0.0f);
        setCreativeTab(null);
    }

    /**
     * Hangs from a devil's trumpet stem or a block with a solid underside.
     */
    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlock(x, y + 1, z) == this || world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN);
    }

    @Override
    protected void checkAndDropBlock(World world, int x, int y, int z) {
        if (!canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    /**
     * If the blocks below were removed, a stem becomes the new lowest block, so it turns back into a bud.
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(world, x, y, z, neighbor);
        if (world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) == META_STEM
            && world.getBlock(x, y - 1, z) != this) {
            world.setBlockMetadataWithNotify(x, y, z, META_BUD, 2);
        }
    }

    /**
     * Near a portal, a bud matures, and a mature bud grows the column down. Once the column can't grow, a mature bud
     * opens into a flower at night, and the flower closes again by day.
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        if (world.getBlock(x, y, z) != this) return;

        int meta = world.getBlockMetadata(x, y, z);
        if (meta == META_BUD || canGrowDown(world, x, y, z)) {
            if (random.nextInt(GROWTH_CHANCE) == 0 && isNearPortal(world, x, y, z)) {
                grow(world, x, y, z);
            }
        } else if (meta == META_BUD_MATURE && !world.isDaytime()) {
            // Bloom
            setMetaAndRelight(world, x, y, z, META_FLOWER);
        } else if (meta == META_FLOWER && world.isDaytime()) {
            // Close
            setMetaAndRelight(world, x, y, z, META_BUD_MATURE);
        }
    }

    /**
     * Picking an open flower drops 1-2 flowers, and the flower goes back to a young bud.
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        if (world.getBlockMetadata(x, y, z) == META_FLOWER) {
            dropBlockAsItem(world, x, y, z, ModItems.DEVILS_TRUMPET_FLOWER.newItemStack(world.rand.nextInt(2) + 1));
            if (!world.isRemote) {
                setMetaAndRelight(world, x, y, z, META_BUD);
            }
            return true;
        }
        return false;
    }

    /**
     * The hit box follows the texture. */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        float bottom = 0.0f;
        if (meta == META_BUD) bottom = 7.0f / 16.0f;
        else if (meta == META_BUD_MATURE) bottom = 3.0f / 16.0f;
        else if (meta == META_FLOWER) bottom = 1.0f / 16.0f;
        setBlockBounds(0.25f, bottom, 0.25f, 0.75f, 1.0f, 0.75f);
    }

    /**
     * An open flower glows faintly.
     */
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == META_FLOWER ? 3 : 0;
    }

    /**
     * Helper function; changes the metadata and recalculates block light.
     */
    private static void setMetaAndRelight(World world, int x, int y, int z, int meta) {
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
        world.updateLightByType(EnumSkyBlock.Block, x, y, z);
    }

    /**
     * Every few seconds, an open flower lets out a portal particle that drifts slowly down.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (world.getBlockMetadata(x, y, z) != META_FLOWER || rand.nextInt(PARTICLE_CHANCE) != 0) return;

        double px = x + 0.3 + rand.nextDouble() * 0.4;
        double py = y - 0.6 + rand.nextDouble() * 0.3;
        double pz = z + 0.3 + rand.nextDouble() * 0.4;
        double driftX = (rand.nextDouble() - 0.5) * 0.3;
        double driftZ = (rand.nextDouble() - 0.5) * 0.3;
        world.spawnParticle("portal", px, py, pz, driftX, 0.0, driftZ);
    }

    /**
     * Each segment drops a seed; an open flower also drops 1-2 flowers.
     */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(ModItems.DEVILS_TRUMPET_SEED.newItemStack(1));
        if (metadata == META_FLOWER) {
            drops.add(ModItems.DEVILS_TRUMPET_FLOWER.newItemStack(world.rand.nextInt(2) + 1));
        }
        return drops;
    }

    /**
     * Returns the seed for pick-block.
     */
    @Override
    public Item getItem(World world, int x, int y, int z) {
        return ModItems.DEVILS_TRUMPET_SEED.get();
    }

    /**
     * Can bone meal be used here at all? If false, the bone meal isn't used up.
     */
    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {
        return (world.getBlockMetadata(x, y, z) == META_BUD || canGrowDown(world, x, y, z))
            && isNearPortal(world, x, y, z);
    }

    /**
     * Does this use succeed? The bone meal is used up either way.
     */
    @Override
    public boolean func_149852_a(World world, Random random, int x, int y, int z) {
        return true;
    }

    /**
     * Effect of bone meal growth.
     */
    @Override
    public void func_149853_b(World world, Random random, int x, int y, int z) {
        grow(world, x, y, z);
    }

    /**
     * Helper function; one growth step.
     */
    public void grow(World world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) == META_BUD) {
            world.setBlockMetadataWithNotify(x, y, z, META_BUD_MATURE, 2);
        } else if (canGrowDown(world, x, y, z)) {
            growDown(world, x, y, z);
        }
    }

    /**
     * Helper function; counts how many devil's trumpet blocks hang in this column, from this one up.
     */
    public int getColumnLength(World world, int x, int y, int z) {
        int length = 0;
        while (world.getBlock(x, y + length, z) == this) {
            length++;
        }
        return length;
    }

    /**
     * Helper function; checks that this is a mature bud with air below and the column isn't at its full length yet.
     */
    public boolean canGrowDown(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == META_BUD_MATURE && world.isAirBlock(x, y - 1, z)
            && getColumnLength(world, x, y, z) < MAX_LENGTH;
    }

    /**
     * Helper function; grows a new bud below this one, which becomes a stem.
     */
    public void growDown(World world, int x, int y, int z) {
        world.setBlockMetadataWithNotify(x, y, z, META_STEM, 2);
        world.setBlock(x, y - 1, z, this, META_BUD, 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        icons = new IIcon[4];
        icons[META_STEM] = reg.registerIcon(getTextureName() + "/stem");
        icons[META_BUD] = reg.registerIcon(getTextureName() + "/bud");
        icons[META_BUD_MATURE] = reg.registerIcon(getTextureName() + "/bud_mature");
        icons[META_FLOWER] = reg.registerIcon(getTextureName() + "/flower");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta >= icons.length) meta = META_STEM;
        return icons[meta];
    }

    /**
     * Helper function; checks for an active Nether portal within range.
     */
    public static boolean isNearPortal(World world, int x, int y, int z) {
        for (int dx = -PORTAL_RANGE_HORIZONTAL; dx <= PORTAL_RANGE_HORIZONTAL; dx++) {
            for (int dy = -PORTAL_RANGE_VERTICAL; dy <= PORTAL_RANGE_VERTICAL; dy++) {
                for (int dz = -PORTAL_RANGE_HORIZONTAL; dz <= PORTAL_RANGE_HORIZONTAL; dz++) {
                    if (world.getBlock(x + dx, y + dy, z + dz) == Blocks.portal) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
