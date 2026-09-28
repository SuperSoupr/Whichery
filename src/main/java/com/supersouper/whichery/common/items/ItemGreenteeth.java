package com.supersouper.whichery.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

import com.supersouper.whichery.ModBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGreenteeth extends Item {

    // Greenteeth uses up multiple items to plant a new growth; this is designed to make farming without
    // nutrient absorption from other plants inefficient, as most of the yield of a "default" growth
    // will go back into replanting it.
    private static final int PLANT_COST = 8;

    public ItemGreenteeth() {
        setUnlocalizedName("greenteeth");
        setTextureName("whichery:greenteeth");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> toolTip, boolean advTooltips) {
        String line = StatCollector.translateToLocalFormatted("tooltip.greenteeth.plant", PLANT_COST);
        toolTip.add(line);
    }

    // Planting logic for greenteeth.
    // Similar to vanilla lilypad - raycast to find water surface, must be still water source with air above.
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        MovingObjectPosition target = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (target == null || (!player.capabilities.isCreativeMode && itemStack.stackSize < PLANT_COST)) {
            return itemStack;
        }

        if (target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = target.blockX;
            int y = target.blockY;
            int z = target.blockZ;
            if (!world.canMineBlock(player, x, y, z) || !player.canPlayerEdit(x, y, z, target.sideHit, itemStack)) {
                return itemStack;
            }

            if (ModBlocks.GREENTEETH.get()
                .canBlockStay(world, x, y + 1, z) && world.isAirBlock(x, y + 1, z)) {
                // Because we're not placing blocks in a standard method, need an extra guard to check
                // if the placement actually happens
                BlockSnapshot snapshot = BlockSnapshot.getBlockSnapshot(world, x, y + 1, z);
                world.setBlock(x, y + 1, z, ModBlocks.GREENTEETH.get());
                if (ForgeEventFactory.onPlayerBlockPlace(player, snapshot, ForgeDirection.UP)
                    .isCanceled()) {
                    // Placement prevented, cancel it
                    snapshot.restore(true, false);
                    return itemStack;
                }

                if (!player.capabilities.isCreativeMode) {
                    // We don't need to guard stack size, it was already checked at the start for survival players
                    itemStack.stackSize -= PLANT_COST;
                }
            }
        }

        return itemStack;
    }
}
