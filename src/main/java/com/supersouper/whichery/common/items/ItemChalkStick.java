package com.supersouper.whichery.common.items;

import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.rituals.ChalkType;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.RitualUtils;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChalkStick extends Item {

    public ItemChalkStick() {
        setUnlocalizedName("chalk_stick");
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

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.itemIcon = RitualRegistry.registerChalkStickIcons(reg);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return RitualRegistry.CHALK_STICK_ICONS.get(getChalkType(stack))[5
            - Math.min(5, (stack.getMaxDamage() - stack.getItemDamage()) * 6 / stack.getMaxDamage())];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
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
        int workingY = y + 1;
        if (world.getBlock(x, y, z) == ModBlocks.CHALK_RUNE_BLOCK.get()) {
            workingY = y;
        } else {
            if (!world.isAirBlock(x, workingY, z)) return false;
            if (side != ForgeDirection.UP.ordinal()) return false;
        }

        world.setBlock(x, workingY, z, ModBlocks.CHALK_RUNE_BLOCK.get(), 0, 3);
        if (!world.isRemote) {
            stack.damageItem(1, player);
        }
        ChalkRuneTileEntity te = (ChalkRuneTileEntity) world.getTileEntity(x, workingY, z);
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
            world.markBlockForUpdate(x, workingY, z);
            te.markDirty();
        }

        return true;
    }
}
