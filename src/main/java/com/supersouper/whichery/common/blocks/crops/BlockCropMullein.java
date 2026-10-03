package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCropMullein extends BlockWhicheryCrop {

    // Offsets for the four side neighbors that burning can spread to
    private static final int[][] OFFSETS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    @SideOnly(Side.CLIENT)
    private IIcon burningIcon;
    @SideOnly(Side.CLIENT)
    private IIcon burntIcon;

    public BlockCropMullein(String id, int maxStage) {
        super(id, maxStage);
    }

    @Override
    protected Item getSeedItem() {
        return ModItems.MULLEIN_SEED.get();
    }

    // Extra forms of a fully grown mullein, stored above the growth stages
    private int burningMeta() {
        return maxStage + 1;
    }

    private int burntMeta() {
        return maxStage + 2;
    }

    /**
     * Grows at vanilla speed in the light.
     */
    @Override
    protected boolean shouldGrow(World world, int x, int y, int z, Random rand) {
        return isBright(world, x, y, z) && soilHasNutrients(world, x, y, z, rand);
    }

    /**
     * Lighting a fully grown mullein with flint and steel burns it.
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || held.getItem() != Items.flint_and_steel) return false;
        if (world.getBlockMetadata(x, y, z) != maxStage) return false;
        if (!player.canPlayerEdit(x, y, z, side, held)) return false;

        if (!world.isRemote) {
            world.setBlockMetadataWithNotify(x, y, z, burningMeta(), 2);
            world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
            held.damageItem(1, player);
        }
        return true;
    }

    /**
     * A burning mullein eventually burns out; when not in the rain, will also spread to neighboring mullein.
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isRemote || world.getBlockMetadata(x, y, z) != burningMeta()) {
            super.updateTick(world, x, y, z, rand);
            return;
        }

        // Rain always burns it out, without spreading
        if (world.canLightningStrikeAt(x, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, burntMeta(), 2);
            return;
        }

        // 3 in 4 chance to light each fully grown, unlit neighbor
        for (int[] offset : OFFSETS) {
            int nx = x + offset[0];
            int nz = z + offset[1];
            if (world.getBlock(nx, y, nz) == this && world.getBlockMetadata(nx, y, nz) == maxStage
                && rand.nextInt(4) < 3) {
                world.setBlockMetadataWithNotify(nx, y, nz, burningMeta(), 2);
            }
        }

        // 1 in 2 chance to burn out
        if (rand.nextInt(2) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, burntMeta(), 2);
        }
    }

    /**
     * Drops a seed, unless it is burning or burnt.
     */
    @Override
    public void addDropsAlways(World world, int x, int y, int z, int metadata, int fortune,
        ArrayList<ItemStack> drops) {
        if (metadata > maxStage) return;
        super.addDropsAlways(world, x, y, z, metadata, fortune, drops);
    }

    /**
     * When grown, drops 1-2 more seeds; when burnt, drops a wick instead.
     */
    @Override
    public void addDropsGrown(World world, int x, int y, int z, int metadata, int fortune, ArrayList<ItemStack> drops) {
        if (metadata == maxStage) {
            drops.add(ModItems.MULLEIN_SEED.newItemStack(world.rand.nextInt(2) + 1));
        } else if (metadata == burntMeta()) {
            drops.add(ModItems.MULLEIN_WICK.newItemStack(1));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        burningIcon = reg.registerIcon("whichery:crops/" + id + "/burning");
        burntIcon = reg.registerIcon("whichery:crops/" + id + "/burnt");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == burningMeta()) return burningIcon;
        if (meta == burntMeta()) return burntIcon;
        return super.getIcon(side, meta);
    }

    /**
     * Creates flame and smoke while burning, and a bit of smoke when burnt.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        int meta = world.getBlockMetadata(x, y, z);
        double px = x + 0.4 + rand.nextDouble() * 0.2;
        double pz = z + 0.4 + rand.nextDouble() * 0.2;
        if (meta == burningMeta()) {
            double py = y + 0.8 + rand.nextDouble() * 0.2;
            world.spawnParticle("flame", px, py, pz, 0.0, 0.0, 0.0);
            world.spawnParticle("smoke", px, py, pz, 0.0, 0.0, 0.0);
        } else if (meta == burntMeta() && rand.nextInt(3) == 0) {
            // Smoke only some of the time, from lower down
            world.spawnParticle("smoke", px, y + 0.4, pz, 0.0, 0.0, 0.0);
        }
    }
}
