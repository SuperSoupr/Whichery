package com.supersouper.whichery.common.rituals.matching;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.matching.BlockMatcherBasic;
import com.supersouper.whichery.common.tileentities.ChalkSmallTileEntity;

public class BlockMatcherChalkSmall extends BlockMatcherBasic {

    private ItemStack stack = null;
    private String type = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;

    public BlockMatcherChalkSmall(String type) {
        super(ModBlocks.CHALK_RUNE_BLOCK_SMALL.get());
        this.type = type;
    }

    public BlockMatcherChalkSmall(String type, ItemStack stack) {
        super(ModBlocks.CHALK_RUNE_BLOCK_SMALL.get());
        this.type = type;
        this.stack = stack;
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof ChalkSmallTileEntity cte)) return false;
        if (tes != null) tes.add(cte);
        return true;

        // boolean match = cte.getType()
        // .equals(type)
        // && (getStack() == null || WhicheryUtils.matchIngredient(getStack(), cte.getStackInSlot(0), true));
        // if (match) {
        // tes.add(te);
        // }
        // return match;
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
    public int itemStackHashCode() {
        return item.hashCode() + type.hashCode();
    }

    @Override
    public void place(World world, int x, int y, int z) {
        world.setBlock(x, y, z, block);
        ChalkSmallTileEntity te = (ChalkSmallTileEntity) world.getTileEntity(x, y, z);
        if (te != null) {
            // te.setType(type);
            // te.setRune(WhicheryUtils.rand.nextInt(12));
            // te.setRotation(WhicheryUtils.rand.nextInt(4));
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
