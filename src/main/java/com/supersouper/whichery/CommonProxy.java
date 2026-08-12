package com.supersouper.whichery;

import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.network.PacketHandler;
import com.supersouper.whichery.common.recipe.RitualRecipeLoader;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public static int chalkRuneRenderID;

    public void preInit(FMLPreInitializationEvent event) {

        FamilyRegistry.initFamilies();

        RitualRegistry.registerChalkType(RitualRegistry.DEFAULT_CHALK_TYPE_NAME, 0xFFFFFF);
        RitualRegistry.registerChalkType("spiritual", 0x0088FF);
        RitualRegistry.registerChalkType("bloody", 0x771111);
        if (ModItems.CHALK.isEnabled()) {
            chalkRuneRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(ChalkRuneISBRH.INSTANCE);
        }
    }

    public void init(FMLInitializationEvent event) {
        ModItems.init();
        ModBlocks.init();
        PacketHandler.init();
        ModTileEntities.init();
        ModKeybindings.init();
        FamilyRegistry.initIngredients();
        RitualRecipeLoader.loadRecipes();
    }

    public void postInit(FMLPostInitializationEvent event) {
        RitualRegistry.finalizeChalkTypes();
    }

    public void loadComplete(FMLLoadCompleteEvent event) {

    }

    public void serverStarting(FMLServerStartingEvent event) {}
}
