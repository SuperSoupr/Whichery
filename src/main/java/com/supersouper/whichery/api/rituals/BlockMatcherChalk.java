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

    public BlockMatcherChalk(Block block) {
        super(block);
    }

    public BlockMatcherChalk(Block block, int type) {
        super(block, type);
    }

    public BlockMatcherChalk(Block block, int type, ItemStack stack) {
        super(block, type);
        this.stack = stack;
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z) {
        ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
        if (te == null) return false;

        return te.getType() == meta
            && (getStack() == null || WhicheryUtils.matchIngredient(getStack(), te.getStackInSlot(0), true));
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack result = new ItemStack(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("type", meta);
        result.setTagCompound(tag);
        return result;
    }

    @Override
    public void place(World world, int x, int y, int z) {
        world.setBlock(x, y, z, block);
        ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
        if (te != null) {
            te.setType(meta);
            te.markDirty();
        }
    }

    public static int itemStackToHashCode(ItemStack stack) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(stack.getItem());

        int type = 0;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            type = tag.getInteger("type");
        }
        return stack.getItem()
            .hashCode() + type;
    }

    public ItemStack getStack() {
        return stack;
    }
}
