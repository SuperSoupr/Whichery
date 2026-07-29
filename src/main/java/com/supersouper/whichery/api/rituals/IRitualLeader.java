package com.supersouper.whichery.api.rituals;

import net.minecraft.entity.player.EntityPlayer;

public interface IRitualLeader {

    RunningRitual getCurrentRitual();

    boolean startRitual(Ritual ritual, EntityPlayer player);

    void endRitual();
}
