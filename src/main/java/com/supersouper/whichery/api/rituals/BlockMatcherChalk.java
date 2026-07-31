package com.supersouper.whichery.api.rituals;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.api.BlockMatcherBasic;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

public class BlockMatcherChalk extends BlockMatcherBasic {

    private ItemStack stack = null;
    private String type = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;

    public BlockMatcherChalk(Block block, String type) {
        super(block);
        this.type = type;
    }

    public BlockMatcherChalk(Block block, String type, ItemStack stack) {
        super(block);
        this.type = type;
        this.stack = stack;
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z) {
        ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
        if (te == null) return false;

        return te.getType()
            .equals(type)
            && (getStack() == null || WhicheryUtils.matchIngredient(getStack(), te.getStackInSlot(0), true));
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack result = new ItemStack(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", type);
        result.setTagCompound(tag);
        return result;
    }

    @Override
    public void place(World world, int x, int y, int z) {
        world.setBlock(x, y, z, block);
        ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
        if (te != null) {
            te.setType(type);
            te.markDirty();
        }
    }

    public static int itemStackToHashCode(ItemStack stack) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(stack.getItem());

        String type = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            type = tag.getString("type");
        }
        return stack.getItem()
            .hashCode() + type.hashCode();
    }

    public ItemStack getStack() {
        return stack;
    }
}
