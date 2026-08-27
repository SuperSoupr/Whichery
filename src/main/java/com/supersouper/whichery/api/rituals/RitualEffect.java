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

    public void onTick() {}

    public void transitionToStage(int stage) {}

    public void complete(int stage) {}

    public void end(int stage) {}

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {}
}
