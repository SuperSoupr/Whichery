package com.supersouper.whichery.common.items;

import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.rituals.ChalkType;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.RitualUtils;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChalk extends Item {

    public ItemChalk() {
        setUnlocalizedName("chalk");
        setMaxDamage(256);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Map.Entry<String, ChalkType> type : RitualRegistry.CHALK_TYPES.entrySet()) {
            list.add(RitualUtils.createChalkItem(item, type.getKey()));
        }
    }

    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            return this.getUnlocalizedName() + "." + tag.getString("type");
        }
        return this.getUnlocalizedName();
    }

    public static String getChalkType(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null) {
            return tag.getString("type");
        }
        return RitualRegistry.DEFAULT_CHALK_TYPE_NAME;
    }

    public static int getChalkRune(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null) {
            return tag.getByte("rune");
        }
        return 0;
    }

    public static int getChalkRotation(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null) {
            return tag.getByte("rotation");
        }
        return 0;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float clickX, float clickY, float clickZ) {
        if (!world.isAirBlock(x, y + 1, z)) return false;
        if (side != ForgeDirection.UP.ordinal()) return false;

        world.setBlock(x, y + 1, z, ModBlocks.CHALK_RUNE_BLOCK.get(), 0, 3);
        if (!world.isRemote) {
            stack.damageItem(1, player);
        }
        ChalkRuneTileEntity te = (ChalkRuneTileEntity) world.getTileEntity(x, y + 1, z);
        if (te != null) {
            String type = getChalkType(stack);
            int rotation = (int) ((((player.rotationYaw % 360) + 22.5f) / 45f + 8f) % 8f);
            te.setType(type);
            te.setRotation(rotation);
            te.setRune(
                stack.getTagCompound()
                    .getByte("nextRune"));
            if (!world.isRemote) {
                // stack.getTagCompound()
                // .setByte(
                // "nextRune",
                // (byte) WhicheryUtils.rand.nextInt(RitualRegistry.CHALK_TYPES.get(type).runeCount));
                int newr = te.getRune();
                newr = newr < 11 ? newr + 1 : 0;
                stack.getTagCompound()
                    .setByte("nextRune", (byte) newr);
            }
            te.markDirty();
        }

        return true;
    }
}
