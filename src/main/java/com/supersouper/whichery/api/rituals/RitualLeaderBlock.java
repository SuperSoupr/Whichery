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

            if (player.isSneaking() && player.capabilities.isCreativeMode) {
                // TODO Remove
                RitualRegistry.getRitual("banana1")
                    .getRecipe()
                    .construct(world, x, y, z);
                return true;
            }

            byte[] rotationBuffer = new byte[] { 0 };
            Ritual ritual = RitualUtils.findRitualAt(world, x, y, z, rotationBuffer);
            if (ritual != null) {
                return rlte.startRitual(ritual, rotationBuffer[0], player);
            }
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

}
