package com.supersouper.whichery.api.rituals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class RitualAnimation {

    protected final RunningRitual currentRitual;
    protected final TileEntity leader;

    public RitualAnimation(TileEntity leader, RunningRitual currentRitual) {
        this.currentRitual = currentRitual;
        this.leader = leader;
    }

    public abstract void onTick();

    public abstract void transitionToStage(int stage);

    public abstract void end(int stage);

    @SideOnly(Side.CLIENT)
    public abstract void render();

    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);

    public abstract void readFromNBT(NBTTagCompound tag);
}
