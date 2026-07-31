package com.supersouper.whichery.common.blocks;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.api.rituals.RitualLeaderBlock;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.items.ItemChalk;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockChalkRune extends RitualLeaderBlock {

    public BlockChalkRune() {
        super(Material.ground);
        setBlockName("chalk_block");
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof ChalkTileEntity cte) {
            if (cte.onRightClicked(player)) return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, subX, subY, subZ);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ChalkTileEntity(world);
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
        for (Map.Entry<String, Integer> type : RitualRegistry.CHALK_TYPES.entrySet()) {
            ItemStack result = new ItemStack(ModBlocks.CHALK_RUNE_BLOCK.get());
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", type.getKey());
            result.setTagCompound(tag);
            list.add(result);
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        ItemStack result = super.getPickBlock(target, world, x, y, z);
        ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
        if (te != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", te.getType());
            result.setTagCompound(tag);
        }
        return result;
    }

    public static class ItemBlockChalkRune extends ItemBlock {

        public ItemBlockChalkRune(Block block) {
            super(block);
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

        public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ, int metadata) {
            if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
                ChalkTileEntity te = (ChalkTileEntity) world.getTileEntity(x, y, z);
                if (te != null) {
                    te.setType(ItemChalk.getChalkType(stack));
                    te.markDirty();
                }
                return true;
            }
            return false;
        }
    }
}
