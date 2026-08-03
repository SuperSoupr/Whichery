package com.supersouper.whichery.api.rituals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class RitualEffect {

    protected final RunningRitual currentRitual;
    protected final TileEntity leader;

    public RitualEffect(TileEntity leader, RunningRitual currentRitual) {
        this.currentRitual = currentRitual;
        this.leader = leader;
    }

    public abstract void onTick();

    public abstract void transitionToStage(int stage);

    public abstract void end(int stage);

    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);

    public abstract void readFromNBT(NBTTagCompound tag);
}
