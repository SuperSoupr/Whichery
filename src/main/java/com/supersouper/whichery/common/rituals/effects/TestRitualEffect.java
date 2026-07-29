package com.supersouper.whichery.common.rituals.effects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;

import com.supersouper.whichery.api.rituals.RitualEffect;
import com.supersouper.whichery.api.rituals.RunningRitual;

public class TestRitualEffect extends RitualEffect {

    public TestRitualEffect(TileEntity leader, RunningRitual currentRitual) {
        super(leader, currentRitual);
    }

    @Override
    public void onTick() {

    }

    @Override
    public void transitionToStage(int stage) {
        EntityPlayer player = currentRitual.getStarter();
        if (player != null) {
            player.addChatComponentMessage(new ChatComponentText("transitioned to stage " + stage));
        }
    }

    @Override
    public void end(int stage) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }
}
