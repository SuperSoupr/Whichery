package com.supersouper.whichery.common.network;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.common.network.s2c.VampireStatsPacket;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Whichery.MODID);

    public static void init() {
        int packetId = 0;

        INSTANCE.registerMessage(VampireStatsPacket.Handler.class, VampireStatsPacket.class, packetId++, Side.CLIENT);
    }
}
