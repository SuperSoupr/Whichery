package com.supersouper.whichery.api.rituals;

import net.minecraft.entity.player.EntityPlayer;

public interface IRitualLeader {

    RunningRitual getCurrentRitual();

    boolean startRitual(EntityPlayer player);

    void endRitual();
}
