package com.supersouper.whichery;

import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.network.PacketHandler;
import com.supersouper.whichery.common.recipe.RitualRecipeLoader;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {

        RitualRegistry.registerChalkType(RitualRegistry.DEFAULT_CHALK_TYPE_NAME, 0xFFFFFF);
        RitualRegistry.registerChalkType("spiritual", 0x0088FF);
        RitualRegistry.registerChalkType("bloody", 0xAA0000);
    }

    public void init(FMLInitializationEvent event) {
        ModItems.init();
        ModBlocks.init();
        RitualRecipeLoader.loadRecipes();
        PacketHandler.init();
        ModTileEntities.init();
        ModKeybindings.init();
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {}
}
