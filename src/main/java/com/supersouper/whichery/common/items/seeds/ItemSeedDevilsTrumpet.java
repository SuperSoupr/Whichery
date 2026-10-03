package com.supersouper.whichery.common.items.seeds;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.common.blocks.crops.BlockCropDevilsTrumpet;

public class ItemSeedDevilsTrumpet extends ItemWhicherySeed {

    public ItemSeedDevilsTrumpet() {
        super("devils_trumpet");
    }

    @Override
    protected Block getCropBlock() {
        return ModBlocks.DEVILS_TRUMPET.get();
    }

    /**
     * Plants on the underside of a block with a solid bottom face, near an active Nether portal.
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        // Only the bottom face of the clicked block
        if (side != 0) return false;
        if (!world.isSideSolid(x, y, z, ForgeDirection.DOWN)) return false;
        if (!player.canPlayerEdit(x, y - 1, z, side, stack)) return false;
        if (world.isAirBlock(x, y - 1, z) && canPlantAt(world, x, y - 1, z)) {
            world.setBlock(x, y - 1, z, getCropBlock(), BlockCropDevilsTrumpet.META_BUD, 2);
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }
            return true;
        }
        return false;
    }

    /**
     * Can only be planted near an active Nether portal.
     */
    @Override
    protected boolean canPlantAt(World world, int x, int y, int z) {
        return BlockCropDevilsTrumpet.isNearPortal(world, x, y, z);
    }
}
