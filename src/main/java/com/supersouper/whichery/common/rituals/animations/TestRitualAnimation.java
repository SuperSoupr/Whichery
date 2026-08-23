package com.supersouper.whichery.common.rituals.animations;

import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.api.rituals.RitualAnimation;
import com.supersouper.whichery.api.rituals.RunningRitual;

public class TestRitualAnimation extends RitualAnimation {

    public TestRitualAnimation(TileEntity leader, RunningRitual currentRitual) {
        super(leader, currentRitual);
    }
}
