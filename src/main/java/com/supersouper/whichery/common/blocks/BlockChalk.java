package com.supersouper.whichery.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.rituals.RitualLeaderBlock;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockChalk extends RitualLeaderBlock {

    public BlockChalk() {
        super(Material.ground);
        setBlockName("chalk_block");
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ChalkTileEntity(meta);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < 16; i++) {
            ItemStack result = new ItemStack(ModBlocks.CHALK_BLOCK.get());
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("type", i);
            result.setTagCompound(tag);
            list.add(result);
        }
    }

    public static class ItemBlockChalk extends ItemBlock {

        public ItemBlockChalk(Block block) {
            super(block);
        }

        @Override
        public String getUnlocalizedName(final ItemStack stack) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                return this.getUnlocalizedName() + "." + tag.getInteger("type");
            }
            return this.getUnlocalizedName();
        }
    }
}
