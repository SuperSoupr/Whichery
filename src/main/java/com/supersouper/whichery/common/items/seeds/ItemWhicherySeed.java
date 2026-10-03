package com.supersouper.whichery.common.items.seeds;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import com.supersouper.whichery.common.blocks.crops.BlockWhicheryCrop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemWhicherySeed extends Item implements IPlantable {

    private final String id;

    public ItemWhicherySeed(String id) {
        super();
        this.id = id;
        this.setUnlocalizedName("seed_" + id);
        this.setTextureName("whichery:seed_" + id);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, net.minecraft.world.World world, int x, int y, int z,
        int side, float hitX, float hitY, float hitZ) {
        if (side != 1) return false;
        Block block = world.getBlock(x, y, z);
        if (!((BlockWhicheryCrop) getCropBlock()).canPlaceBlockOn(block)) {
            return false;
        }
        if (world.isAirBlock(x, y + 1, z) && canPlantAt(world, x, y + 1, z)) {
            world.setBlock(x, y + 1, z, getCropBlock(), 0, 2);
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }
            return true;
        }
        return false;
    }

    /**
     * Shows a short description of how to grow the crop, if the lang file has one: one tooltip line per key,
     * tooltip.seed_<id>.1, tooltip.seed_<id>.2, and so on.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> toolTip, boolean advTooltips) {
        for (int line = 1; StatCollector.canTranslate("tooltip.seed_" + id + "." + line); line++) {
            toolTip.add(StatCollector.translateToLocal("tooltip.seed_" + id + "." + line));
        }
    }

    protected abstract Block getCropBlock();

    /**
     * Extra conditions for planting, on top of the soil and air checks.
     * x, y, z is the position the crop would be planted at.
     */
    protected boolean canPlantAt(net.minecraft.world.World world, int x, int y, int z) {
        return true;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return getCropBlock();
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }
}
