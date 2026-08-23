package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;

public abstract class RitualLeaderTileEntity extends TileEntity implements IRitualLeader {

    protected RunningRitual currentRitual;
    protected RunningRitual nextRitual;
    protected boolean ticking;

    @Override
    public RunningRitual getCurrentRitual() {
        return currentRitual;
    }

    @Override
    public boolean startRitual(Ritual ritual, byte rotation, ArrayList<TileEntity> tes, EntityPlayer starter) {
        if (!worldObj.isRemote) {
            if (currentRitual == null) {
                currentRitual = new RunningRitual(this, ritual, starter, rotation, tes);
                beginTicking();
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            } else {
                starter.addChatComponentMessage(new ChatComponentText("Ritual already active"));
            }
        }

        return true;
    }

    protected void beginTicking() {
        if (!ticking && worldObj != null) {
            ticking = true;
            worldObj.addTileEntity(this);
        }
    }

    @Override
    public boolean canUpdate() {
        return currentRitual != null;
    }

    @Override
    public void updateEntity() {
        if (currentRitual != null) {
            currentRitual.tick();
            markDirty();
        }
    }

    @Override
    public void completeRitual() {
        if (currentRitual != null) {
            currentRitual.complete();
        }
    }

    @Override
    public void endRitual() {
        if (currentRitual != null) {
            currentRitual.end();
            currentRitual = null;
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            if (nextRitual == null) {
                worldObj.func_147457_a(this); // Mark this TE to be unloaded
                ticking = false;
            } else {
                currentRitual = nextRitual;
                nextRitual = null;
            }
        }
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
            NBTTagCompound currentRitualTag = tag.getCompoundTag("currentRitual");
            UUID currentRitualUUID = UUID.fromString(currentRitualTag.getString("uuid"));
            if (currentRitual == null) {
                currentRitual = new RunningRitual(currentRitualUUID, this);
                currentRitual.readFromNBT(currentRitualTag, true);
            } else {
                if (currentRitual.uuid.equals(currentRitualUUID)) {
                    currentRitual.readFromNBT(tag.getCompoundTag("currentRitual"), false);
                } else {
                    nextRitual = new RunningRitual(currentRitualUUID, this);
                    nextRitual.readFromNBT(currentRitualTag, true);
                }
            }
            beginTicking();
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
