package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCropStJohnsWort extends BlockWhicheryCrop {

    private static final int DAY_LENGTH = 24000;
    // Daytime runs from just after sunrise to just before sunset (when world.isDaytime() is true)
    private static final int DAY_START = 23460;
    private static final int DAY_END = 12540;
    private static final int NOON = 6000;
    // How much closer to noon each extra flower needs (3 flowers within 2000 of noon, 2 within 4000)
    private static final int NOON_STEP = 2000;
    // Offsets for the four side neighbors it can spread to
    private static final int[][] OFFSETS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    @SideOnly(Side.CLIENT)
    private IIcon bloomIcon;

    public BlockCropStJohnsWort(String id, int maxStage) {
        super(id, maxStage);
    }

    @Override
    protected Item getSeedItem() {
        return ModItems.ST_JOHNS_WORT_SEED.get();
    }

    // Blooming stage, stored above the growth stages
    private int bloomMeta() {
        return maxStage + 1;
    }

    /**
     * Grows at vanilla speed, but only during the day with a view of the sky.
     */
    @Override
    protected boolean shouldGrow(World world, int x, int y, int z, Random rand) {
        return isSunlit(world, x, y, z) && soilHasNutrients(world, x, y, z, rand);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);

        // If fully grown, bloom in sunlight and close otherwise
        int meta = world.getBlockMetadata(x, y, z);
        if (meta >= maxStage) {
            boolean sunlit = isSunlit(world, x, y, z);
            if (sunlit) {
                if (meta != bloomMeta()) {
                    world.setBlockMetadataWithNotify(x, y, z, bloomMeta(), 2);
                }
                // 1 in 4 chance to spread to a random side neighbor
                if (rand.nextInt(4) == 0) {
                    trySpread(world, x, y, z, rand);
                }
            } else if (!sunlit && meta == bloomMeta()) {
                world.setBlockMetadataWithNotify(x, y, z, maxStage, 2);
            }
        }
    }

    /**
     * Helper function; plants a new St John's Wort on one random side neighbor, if it's empty farmland that can see the
     * sky.
     */
    private void trySpread(World world, int x, int y, int z, Random rand) {
        int[] offset = OFFSETS[rand.nextInt(OFFSETS.length)];
        int nx = x + offset[0];
        int nz = z + offset[1];

        // Don't load a neighboring chunk just to spread into it
        if (!world.blockExists(nx, y, nz)) return;

        if (world.isAirBlock(nx, y, nz) && world.getBlock(nx, y - 1, nz) == Blocks.farmland
            && world.canBlockSeeTheSky(nx, y, nz)) {
            world.setBlock(nx, y, nz, this, 0, 2);
        }
    }

    /**
     * Can only bone meal during the day with a view of the sky.
     */
    @Override
    protected boolean canUseBonemeal(World world, int x, int y, int z) {
        return super.canUseBonemeal(world, x, y, z) && isSunlit(world, x, y, z);
    }

    /**
     * Bone meal only has a 1 in 2 chance to work.
     */
    @Override
    protected boolean canFertilize(World world, Random random, int x, int y, int z) {
        return super.canFertilize(world, random, x, y, z) && random.nextInt(2) == 0;
    }

    /**
     * Returns no seeds.
     */
    @Override
    public void addDropsAlways(World world, int x, int y, int z, int metadata, int fortune,
        ArrayList<ItemStack> drops) {
        return;
    }

    /**
     * Drops 1-3 flowers when in bloom during the day; drops more the closer it is to noon.
     */
    @Override
    public void addDropsGrown(World world, int x, int y, int z, int metadata, int fortune, ArrayList<ItemStack> drops) {
        int time = timeOfDay(world);
        if (metadata != bloomMeta() || !isDay(time)) return;

        // One more flower for each step closer to noon
        int fromNoon = Math.abs(time - NOON);
        int flowers = 1;
        if (fromNoon < 2 * NOON_STEP) flowers = 2;
        if (fromNoon < NOON_STEP) flowers = 3;
        drops.add(ModItems.ST_JOHNS_WORT_FLOWER.newItemStack(flowers));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        bloomIcon = reg.registerIcon("whichery:crops/" + id + "/bloom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == bloomMeta()) return bloomIcon;
        return super.getIcon(side, meta);
    }

    /**
     * Helper function; returns the time of day.
     */
    private static int timeOfDay(World world) {
        return (int) (world.getWorldTime() % DAY_LENGTH);
    }

    /**
     * Helper function; checks if it's daytime (the day wraps around midnight of the world time).
     */
    private static boolean isDay(int time) {
        return time >= DAY_START || time < DAY_END;
    }

    /**
     * Helper function; checks if it's daytime and the crop can see the sky.
     */
    private static boolean isSunlit(World world, int x, int y, int z) {
        return isDay(timeOfDay(world)) && world.canBlockSeeTheSky(x, y, z);
    }
}
