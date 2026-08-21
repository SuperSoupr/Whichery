package com.supersouper.whichery;

import com.supersouper.whichery.api.rituals.ChalkType;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.client.render.ChalkRuneISBRH;
import com.supersouper.whichery.client.render.ChalkRuneSmallISBRH;
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
    public static int chalkRuneSmallRenderID;

    public void preInit(FMLPreInitializationEvent event) {

        FamilyRegistry.initFamilies();

        RitualRegistry.registerChalkType(new ChalkType(Whichery.MODID, RitualRegistry.DEFAULT_CHALK_TYPE_NAME));
        RitualRegistry.registerChalkType(new ChalkType(Whichery.MODID, "spiritual"));
        RitualRegistry.registerChalkType(new ChalkType(Whichery.MODID, "bloody"));
        if (ModItems.CHALK.isEnabled()) {
            chalkRuneRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(ChalkRuneISBRH.INSTANCE);

            chalkRuneSmallRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(ChalkRuneSmallISBRH.INSTANCE);
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
