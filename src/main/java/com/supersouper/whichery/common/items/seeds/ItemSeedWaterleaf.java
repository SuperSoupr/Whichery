package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.common.blocks.crops.BlockCropWaterleafStem;

public class ItemSeedWaterleaf extends ItemWhicherySeed {

    public ItemSeedWaterleaf() {
        super("waterleaf");
    }

    @Override
    protected Block getCropBlock() {
        return ModBlocks.WATERLEAF_STEM.get();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        int tx = x + dir.offsetX;
        int ty = y + dir.offsetY;
        int tz = z + dir.offsetZ;
        Block soil = world.getBlock(tx, ty - 1, tz);
        Block water = world.getBlock(tx, ty, tz);

        // Check the player has permission to place
        if (!player.canPlayerEdit(tx, ty, tz, side, stack)) return false;
        // Check the soil is valid
        if (!((BlockCropWaterleafStem) getCropBlock()).canPlaceBlockOn(soil)) {
            return false;
        }
        // Check that there is a 1-block-deep water source
        if (water == Blocks.water && world.getBlockMetadata(tx, ty, tz) == 0 && world.isAirBlock(tx, ty + 1, tz)) {
            world.setBlock(tx, ty, tz, getCropBlock(), BlockCropWaterleafStem.METADATA, 3);
            ((BlockCropWaterleafStem) getCropBlock()).growTop(world, tx, ty, tz);
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }
            return true;
        }
        return false;
    }
}
