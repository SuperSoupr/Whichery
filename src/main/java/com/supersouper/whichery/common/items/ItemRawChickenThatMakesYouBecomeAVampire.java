package com.supersouper.whichery.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.supersouper.whichery.common.entity.extendedproperties.VampirismProperty;

public class ItemRawChickenThatMakesYouBecomeAVampire extends ItemFood {

    public ItemRawChickenThatMakesYouBecomeAVampire() {
        super(0, false);
        setAlwaysEdible();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            VampirismProperty vamp = (VampirismProperty) player.getExtendedProperties(VampirismProperty.KEY);
            if (!vamp.isVampire()) {
                vamp.gainVampirism(player);
            } else {
                vamp.cureVampirism(player);
            }
        }
        super.onFoodEaten(stack, world, player);
    }
}
