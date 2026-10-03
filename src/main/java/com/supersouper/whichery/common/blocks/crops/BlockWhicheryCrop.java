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
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockWhicheryCrop extends BlockCrops {

    // The threshold between "bright" and "dark" environments.
    private static final int MIN_LIGHT = 9;

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    protected final String id;
    // Growth stages 0 to maxStage, with one texture each (stage_0 to stage_<maxStage>).
    // Crops can use metadata above maxStage for extra forms of a fully grown crop.
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
        if (!shouldGrow(world, x, y, z, rand)) return;

        world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    }

    /**
     * Returns whether the crop should grow a stage on this random tick. Always true by default;
     * crops override this to combine growth conditions (e.g. soilHasNutrients, isBright).
     */
    protected boolean shouldGrow(World world, int x, int y, int z, Random rand) {
        return true;
    }

    /**
     * Checks if the crop is in a bright area.
     */
    protected boolean isBright(World world, int x, int y, int z) {
        return world.getBlockLightValue(x, y + 1, z) >= MIN_LIGHT;
    }

    /**
     * Returns whether the soil lets the crop grow on this tick (vanilla's growth chance).
     * This is the default BlockCrops updateTick logic, but as a boolean
     * so it can be combined with other growth requirements.
     * Improved by wet farmland & mixed crops.
     */
    protected boolean soilHasNutrients(World world, int x, int y, int z, Random rand) {
        return rand.nextInt((int) (25.0F / soilQuality(world, x, y, z)) + 1) == 0;
    }

    /**
     * Soil/crop quality, from BlockCrops.
     * Wet farmland counts more than dry farmland.
     * Planting the same crop diagonally, or on both sides, halves it.
     */
    protected float soilQuality(World world, int x, int y, int z) {
        float factor = 1.0F;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int sx = x + dx;
                int sz = z + dz;
                Block soil = world.getBlock(sx, y - 1, sz);
                float value = 0.0F;
                if (soil.canSustainPlant(world, sx, y - 1, sz, ForgeDirection.UP, this)) {
                    value = soil.isFertile(world, sx, y - 1, sz) ? 3.0F : 1.0F;
                }
                // Soil around the crop only counts a quarter
                if (dx != 0 || dz != 0) {
                    value /= 4.0F;
                }
                factor += value;
            }
        }

        // Check for the same crop next to this one
        boolean sameAlongX = world.getBlock(x - 1, y, z) == this || world.getBlock(x + 1, y, z) == this;
        boolean sameAlongZ = world.getBlock(x, y, z - 1) == this || world.getBlock(x, y, z + 1) == this;
        boolean sameDiagonal = world.getBlock(x - 1, y, z - 1) == this || world.getBlock(x + 1, y, z - 1) == this
            || world.getBlock(x - 1, y, z + 1) == this
            || world.getBlock(x + 1, y, z + 1) == this;
        if (sameDiagonal || (sameAlongX && sameAlongZ)) {
            factor /= 2.0F;
        }
        return factor;
    }

    // Wrappers for deobf methods, for the sake of sanity
    protected abstract Item getSeedItem();

    protected boolean canUseBonemeal(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) < maxStage;
    }

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
    public final boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {
        return canUseBonemeal(world, x, y, z);
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
