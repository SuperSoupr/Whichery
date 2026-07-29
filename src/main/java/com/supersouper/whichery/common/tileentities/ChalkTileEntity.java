package com.supersouper.whichery.common.tileentities;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;

import com.supersouper.whichery.api.rituals.IRitualParticipator;
import com.supersouper.whichery.api.rituals.RitualLeaderTileEntity;

public class ChalkTileEntity extends RitualLeaderTileEntity implements IRitualParticipator {

    private int type = 0;

    public ChalkTileEntity() {

    }

    public ChalkTileEntity(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        type = compound.getInteger("type");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("type", type);
    }

    @Override
    public void ritualTick() {
        if (worldObj.isRemote) {
            worldObj.spawnEntityInWorld(new EntityLightningBolt(worldObj, xCoord, yCoord, zCoord));
        }
    }

    @Override
    public void transitionToStage(int stage) {

    }
}
