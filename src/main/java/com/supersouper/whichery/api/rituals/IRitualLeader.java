package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public interface IRitualLeader {

    RunningRitual getCurrentRitual();

    boolean startRitual(Ritual ritual, byte rotation, ArrayList<TileEntity> tes, EntityPlayer starter);

    void endRitual();

    ArrayList<TileEntity> getCapturedTileEntities();
}
