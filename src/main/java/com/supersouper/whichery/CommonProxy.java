package com.supersouper.whichery;

import com.supersouper.whichery.api.ingredientfamilies.FamilyRegistry;
import com.supersouper.whichery.common.network.PacketHandler;

import cpw.mods.fml.common.event.*;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModItems.init();
        ModBlocks.init();
        FamilyRegistry.initFamilies();
    }

    public void init(FMLInitializationEvent event) {
        PacketHandler.init();
        ModTileEntities.init();
        ModKeybindings.init();
        ModEntities.init();
        FamilyRegistry.initIngredients();

        FMLInterModComms
            .sendMessage("Waila", "register", "com.supersouper.whichery.common.compat.waila.WailaCompat.load");
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {}
}
