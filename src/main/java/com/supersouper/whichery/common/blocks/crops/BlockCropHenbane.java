package com.supersouper.whichery.common.blocks.crops;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCropHenbane extends BlockWhicheryCrop {

    private static final int DAY_LENGTH = 24000;
    private static final int BLOOM_START = 22000;
    private static final int BLOOM_END = 23150;

    @SideOnly(Side.CLIENT)
    private IIcon bloomIcon;

    public BlockCropHenbane(String id, int maxStage) {
        super(id, maxStage);
    }

    @Override
    protected Item getSeedItem() {
        return ModItems.HENBANE_SEED.get();
    }

    /**
     * Renders like vanilla crops (a # shape) instead of a cross.
     */
    @Override
    public int getRenderType() {
        return 6;
    }

    // Extra form of a blooming henbane, stored above the growth stages
    private int bloomMeta() {
        return maxStage + 1;
    }

    /**
     * Grows at vanilla speed, no light requirement.
     */
    @Override
    protected boolean shouldGrow(World world, int x, int y, int z, Random rand) {
        return soilHasNutrients(world, x, y, z, rand);
    }

    /**
     * Drops 1-2 flowers when in bloom.
     */
    @Override
    public void addDropsGrown(World world, int x, int y, int z, int metadata, int fortune, ArrayList<ItemStack> drops) {
        if (metadata == bloomMeta()) {
            drops.add(ModItems.HENBANE_FLOWER.newItemStack(world.rand.nextInt(2) + 1));
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);

        // If fully grown, check bloom status
        int meta = world.getBlockMetadata(x, y, z);
        if (meta >= maxStage) {
            int time = (int) (world.getWorldTime() % DAY_LENGTH);

            // If it's outside the blooming period, wilt
            if (time < BLOOM_START || time > BLOOM_END) {
                if (meta == bloomMeta()) {
                    world.setBlockMetadataWithNotify(x, y, z, maxStage, 2);
                }
                return;
            }
            // If it's during the blooming period, bloom
            if (meta != bloomMeta()) {
                world.setBlockMetadataWithNotify(x, y, z, bloomMeta(), 2);
            }
        }

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
}
