package com.supersouper.whichery.api.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class RitualLeaderTileEntity extends TileEntity implements IRitualLeader {

    private RunningRitual currentRitual;

    @Override
    public RunningRitual getCurrentRitual() {
        return currentRitual;
    }

    @Override
    public boolean startRitual(EntityPlayer player) {
        // TODO Remove
        currentRitual = new RunningRitual(this, RitualRegistry.getRitual("banana1"), player);
        markDirty();
        return true;
    }

    @Override
    public void updateEntity() {
        if (currentRitual != null) {
            currentRitual.tick();
            markDirty();
        }
    }

    @Override
    public void endRitual() {
        currentRitual.end();
        currentRitual = null;
        markDirty();
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("currentRitual")) {
            currentRitual = new RunningRitual(this);
            currentRitual.readFromNBT(tag.getCompoundTag("currentRitual"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (currentRitual != null) {
            tag.setTag("currentRitual", currentRitual.writeToNBT(new NBTTagCompound()));
        }
    }
}
