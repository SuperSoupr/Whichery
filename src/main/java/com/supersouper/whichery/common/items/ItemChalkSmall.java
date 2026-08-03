package com.supersouper.whichery.common.items;

import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.blocks.BlockChalkRuneSmall;
import com.supersouper.whichery.common.tileentities.ChalkSmallTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChalkSmall extends Item {

    // TODO merge with regular chalk?
    public ItemChalkSmall() {
        setUnlocalizedName("chalk_small");
        setMaxDamage(256);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Map.Entry<String, Integer> type : RitualRegistry.CHALK_TYPES.entrySet()) {
            ItemStack result = new ItemStack(item);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", type.getKey());
            result.setTagCompound(tag);
            list.add(result);
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

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float clickX, float clickY, float clickZ) {
        if (side != ForgeDirection.UP.ordinal()) return false;

        String type = getChalkType(stack);
        TileEntity te;
        if (world.getBlock(x, y, z) != ModBlocks.CHALK_RUNE_BLOCK_SMALL.get()) {
            world.setBlock(x, y + 1, z, ModBlocks.CHALK_RUNE_BLOCK_SMALL.get(), 0, 3);
            te = world.getTileEntity(x, y + 1, z);
        } else {
            te = world.getTileEntity(x, y, z);
        }
        if (!world.isRemote) {
            stack.damageItem(1, player);
        }
        int pos = BlockChalkRuneSmall.clickPosToOrdinal(clickX, clickZ);
        if (te instanceof ChalkSmallTileEntity cste) {
            if (cste.getType(pos) != null) return false;
            cste.setType(pos, type);
            cste.markDirty();
        }

        return true;
    }

}
