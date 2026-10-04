package com.supersouper.whichery.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.supersouper.whichery.common.entity.extendedproperties.DemonologyProperty;

public class ItemAppleThatMakesYouSeeDemons extends ItemFood {

    public ItemAppleThatMakesYouSeeDemons() {
        super(0, false);
        setAlwaysEdible();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            DemonologyProperty props = DemonologyProperty.get(player);
            props.setCanSeeDemonsPossessingHosts(!props.isCanSeeDemonsPossessingHosts(), player);
        }

        super.onFoodEaten(stack, world, player);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list,
        boolean showAdvancedInformation) {
        list.add(StatCollector.translateToLocal("tooltip.item.apple_that_makes_you_see_demons"));
    }
}
