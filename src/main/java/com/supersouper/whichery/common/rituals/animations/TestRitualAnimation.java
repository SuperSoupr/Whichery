package com.supersouper.whichery.common.rituals.animations;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.api.rituals.RitualAnimation;
import com.supersouper.whichery.api.rituals.RunningRitual;

public class TestRitualAnimation extends RitualAnimation {

    public TestRitualAnimation(TileEntity leader, RunningRitual currentRitual) {
        super(leader, currentRitual);
    }

    @Override
    public void onTick() {

    }

    @Override
    public void transitionToStage(int stage) {

    }

    @Override
    public void end(int stage) {

    }

    @Override
    public void render() {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }
}
