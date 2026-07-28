package com.supersouper.whichery;

import com.supersouper.whichery.common.network.PacketHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModItems.init();
        ModBlocks.init();
    }

    public void init(FMLInitializationEvent event) {
        PacketHandler.init();
        ModTileEntities.init();
        ModKeybindings.init();
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {}
}
