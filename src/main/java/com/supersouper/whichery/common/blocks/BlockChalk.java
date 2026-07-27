package com.supersouper.whichery.common.blocks;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.supersouper.whichery.common.tileentities.ChalkTileEntity;

public class BlockChalk extends BlockCarpet implements ITileEntityProvider {

    public BlockChalk() {
        setBlockName("chalk_block");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ChalkTileEntity();
    }
}
