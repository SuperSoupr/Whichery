package com.supersouper.whichery.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChalk extends Item {

    public ItemChalk() {
        setUnlocalizedName("type");
        setMaxDamage(256);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i <= 16; i++) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("type", i);
            ItemStack itemStack = new ItemStack(item);
            itemStack.setTagCompound(tagCompound);
            list.add(itemStack);
        }
    }

    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            return this.getUnlocalizedName() + "." + tag.getInteger("type");
        }
        return this.getUnlocalizedName();
    }

    private int getChalkType(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null) {
            return tag.getInteger("type");
        }
        return 0;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float clickX, float clickY, float clickZ) {
        if (!world.isAirBlock(x, y + 1, z)) return false;
        if (side != ForgeDirection.UP.ordinal()) return false;
        if (world.isRemote) return true;

        int type = getChalkType(stack);
        world.setBlock(x, y + 1, z, ModBlocks.CHALK_BLOCK.get(), type, 3);
        stack.damageItem(1, player);
        ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y + 1, z);
        if (te != null) {
            te.setType(type);
        }

        return true;
    }
}
