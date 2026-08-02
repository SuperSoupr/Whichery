package com.supersouper.whichery.common.dimensions;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.supersouper.whichery.common.config.OtherConfig;

public class ItemDebugTeleporter extends Item {

    public ItemDebugTeleporter() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && player instanceof EntityPlayerMP playerMP) {
            if (playerMP.dimension == OtherConfig.spiritRealmDimID) {
                playerMP.mcServer.getConfigurationManager()
                    .transferPlayerToDimension(
                        playerMP,
                        0,
                        new WhicheryTeleporter(playerMP.mcServer.worldServerForDimension(0)));
            } else {
                playerMP.mcServer.getConfigurationManager()
                    .transferPlayerToDimension(
                        playerMP,
                        OtherConfig.spiritRealmDimID,
                        new WhicheryTeleporter(
                            playerMP.mcServer.worldServerForDimension(OtherConfig.spiritRealmDimID)));
            }
        }

        return stack;
    }
}
