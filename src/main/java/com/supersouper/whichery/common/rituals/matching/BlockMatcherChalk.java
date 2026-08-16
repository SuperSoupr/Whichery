package com.supersouper.whichery.common.rituals.matching;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.ModItems;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.RitualUtils;
import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

public class BlockMatcherChalk implements IBlockMatcher {

    private final String type;

    public BlockMatcherChalk(String type) {
        this.type = type;
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof ChalkRuneTileEntity cte)) return false;

        boolean match = cte.getType()
            .equals(type);
        if (match) {
            tes.add(te);
        }
        return match;
    }

    @Override
    public ItemStack getItemStack() {
        return RitualUtils.createChalkItem(ModItems.CHALK.get(), type);
    }

    @Override
    public int[] itemStackHashCodes() {
        return new int[] { ModItems.CHALK.get()
            .hashCode() + type.hashCode() };
    }

    @Override
    public void place(World world, int x, int y, int z) {
        world.setBlock(x, y, z, ModBlocks.CHALK_RUNE_BLOCK.get());
        ChalkRuneTileEntity te = (ChalkRuneTileEntity) world.getTileEntity(x, y, z);
        if (te != null) {
            te.setType(type);
            te.setRune(WhicheryUtils.rand.nextInt(RitualRegistry.RUNE_COUNT));
            te.setRotation(WhicheryUtils.rand.nextInt(4));
            te.markDirty();
        }
    }

    public static int chalkItemStackToHashCode(ItemStack stack) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(stack.getItem());

        String type;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            type = tag.getString("type");
        } else {
            type = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;
        }
        return stack.getItem()
            .hashCode() + type.hashCode();
    }
}
