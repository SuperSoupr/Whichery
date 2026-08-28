package com.supersouper.whichery.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.RitualPreview;
import com.supersouper.whichery.api.rituals.RitualRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRitualPreview extends Item {

    public ItemRitualPreview() {
        setUnlocalizedName("ritual_preview");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Ritual ritual : RitualRegistry.rituals()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ritual", ritual.name);
            ItemStack itemStack = new ItemStack(item);
            itemStack.setTagCompound(tag);
            list.add(itemStack);
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float clickX, float clickY, float clickZ) {
        if (player.isSneaking()) {
            RitualPreview.clearPreviews();
            return true;
        }
        if (!world.isAirBlock(x, y + 1, z)) return false;
        if (side != ForgeDirection.UP.ordinal()) return false;

        String ritualName = getRitual(stack);
        if (ritualName == null) return false;
        Ritual ritual = RitualRegistry.getRitual(ritualName);
        if (ritual == null) return false;
        int direction = (int) ((((player.rotationYaw % 360) + 45f) / 90f + 4f) % 4f);
        RitualPreview.addPreview(ritual, world, player, x, y + 1, z, direction);

        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean p_77624_4_) {
        super.addInformation(stack, player, tooltip, p_77624_4_);
        String ritual = getRitual(stack);
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "whichery.desc.item.ritual_preview.0",
                ritual == null ? "INVALID RITUAL" : Ritual.getDisplayName(ritual)));
    }

    public static String getRitual(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null) {
            return tag.getString("ritual");
        }
        return null;
    }
}
