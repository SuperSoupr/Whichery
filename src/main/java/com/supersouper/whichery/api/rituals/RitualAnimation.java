package com.supersouper.whichery.api.rituals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public abstract class RitualAnimation {

    public final RunningRitual currentRitual;
    public final TileEntity leader;

    public RitualAnimation(TileEntity leader, RunningRitual currentRitual) {
        this.currentRitual = currentRitual;
        this.leader = leader;
    }

    public void render(RenderWorldLastEvent event) {}

    public void onTick() {}

    public void transitionToStage(int stage) {}

    public void complete(int stage) {}

    public void end(int stage) {}

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {}
}
