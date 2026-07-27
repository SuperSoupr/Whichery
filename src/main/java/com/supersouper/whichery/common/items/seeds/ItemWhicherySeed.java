package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import com.supersouper.whichery.common.blocks.crops.BlockWhicheryCrop;

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
        if (world.isAirBlock(x, y + 1, z)) {
            world.setBlock(x, y + 1, z, getCropBlock(), 0, 2);
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }
            return true;
        }
        return false;
    }

    protected abstract Block getCropBlock();

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
