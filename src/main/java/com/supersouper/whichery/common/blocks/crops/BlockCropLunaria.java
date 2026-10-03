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

public class BlockCropLunaria extends BlockWhicheryCrop {

    private static final int DAY_LENGTH = 24000;
    // Night runs from just after sunset to just before sunrise (when world.isDaytime() is false)
    private static final int NIGHT_START = 12540;
    private static final int NIGHT_END = 23460;

    @SideOnly(Side.CLIENT)
    private IIcon podsIcon;
    @SideOnly(Side.CLIENT)
    private IIcon bloomIcon;

    public BlockCropLunaria(String id, int maxStage) {
        super(id, maxStage);
    }

    @Override
    protected Item getSeedItem() {
        return ModItems.LUNARIA_SEED_POD.get();
    }

    /**
     * Renders like vanilla crops (a # shape) instead of a cross.
     */
    @Override
    public int getRenderType() {
        return 6;
    }

    // Extra forms of a fully grown lunaria, stored above the growth stages
    private int podsMeta() {
        return maxStage + 1;
    }

    private int bloomMeta() {
        return maxStage + 2;
    }

    /**
     * Grows at vanilla speed, no light requirement.
     */
    @Override
    protected boolean shouldGrow(World world, int x, int y, int z, Random rand) {
        return soilHasNutrients(world, x, y, z, rand);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);

        // If fully grown, grow pods near the full moon and flowers near the new moon (at night);
        // otherwise (during the day, or on half moons) go back to plain fully grown
        int meta = world.getBlockMetadata(x, y, z);
        if (meta < maxStage) return;
        int form = maxStage;
        if (isNight(world)) {
            if (isNearFullMoon(world)) {
                form = podsMeta();
            } else if (isNearNewMoon(world)) {
                form = bloomMeta();
            }
        }
        if (meta != form) {
            world.setBlockMetadataWithNotify(x, y, z, form, 2);
        }
    }

    /**
     * Drops a seed pod, unless it's flowering and harvested at night.
     */
    @Override
    public void addDropsAlways(World world, int x, int y, int z, int metadata, int fortune,
        ArrayList<ItemStack> drops) {
        if (metadata == bloomMeta() && isNight(world)) return;
        super.addDropsAlways(world, x, y, z, metadata, fortune, drops);
    }

    /**
     * When harvested at night, drops 1-2 more seed pods if it has pods, or 1-2 blooms if it's flowering.
     */
    @Override
    public void addDropsGrown(World world, int x, int y, int z, int metadata, int fortune, ArrayList<ItemStack> drops) {
        if (!isNight(world)) return;
        if (metadata == podsMeta()) {
            drops.add(ModItems.LUNARIA_SEED_POD.newItemStack(world.rand.nextInt(2) + 1));
        } else if (metadata == bloomMeta()) {
            drops.add(ModItems.LUNARIA_BLOOM.newItemStack(world.rand.nextInt(2) + 1));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        podsIcon = reg.registerIcon("whichery:crops/" + id + "/pods");
        bloomIcon = reg.registerIcon("whichery:crops/" + id + "/bloom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == podsMeta()) return podsIcon;
        if (meta == bloomMeta()) return bloomIcon;
        return super.getIcon(side, meta);
    }

    /**
     * Helper function; checks if it's night.
     */
    private static boolean isNight(World world) {
        int time = (int) (world.getWorldTime() % DAY_LENGTH);
        return time >= NIGHT_START && time < NIGHT_END;
    }

    /**
     * Helper function; checks if the moon is full, or one phase away from full.
     */
    private static boolean isNearFullMoon(World world) {
        int phase = world.getMoonPhase();
        return phase == 7 || phase == 0 || phase == 1;
    }

    /**
     * Helper function; checks if the moon is new, or one phase away from new.
     */
    private static boolean isNearNewMoon(World world) {
        int phase = world.getMoonPhase();
        return phase == 3 || phase == 4 || phase == 5;
    }
}
