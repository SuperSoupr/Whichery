package com.supersouper.whichery.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.client.gui.RitualPreviewRenderer;

public class ItemRitualPreview extends Item {

    public ItemRitualPreview() {
        setUnlocalizedName("ritual_preview");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float clickX, float clickY, float clickZ) {
        if (!world.isAirBlock(x, y + 1, z)) return false;
        if (side != ForgeDirection.UP.ordinal()) return false;

        if (world.isRemote) {
            RitualPreviewRenderer.addPreview(RitualRegistry.getRitual("banana2"), x, y + 1, z);
        }
        return true;
    }
}
