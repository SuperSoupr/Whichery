package com.supersouper.whichery.api.rituals;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class RitualLeaderBlock extends Block implements ITileEntityProvider {

    protected RitualLeaderBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof IRitualLeader rlte) {
            return rlte.startRitual(player);
        }
        return false;
    }

}
