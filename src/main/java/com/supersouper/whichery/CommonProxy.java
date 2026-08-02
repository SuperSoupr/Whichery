package com.supersouper.whichery;

import net.minecraftforge.common.DimensionManager;

import com.supersouper.whichery.common.config.OtherConfig;
import com.supersouper.whichery.common.dimensions.ItemDebugTeleporter;
import com.supersouper.whichery.common.dimensions.spirit.BiomeGenSpiritRealm;
import com.supersouper.whichery.common.dimensions.spirit.WorldProviderSpiritRealm;
import com.supersouper.whichery.common.network.PacketHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ModItems.init();
        ModBlocks.init();

        GameRegistry.registerItem(
            new ItemDebugTeleporter().setUnlocalizedName("debugTeleporter")
                .setTextureName("stick"),
            "debugTeleporter");

        DimensionManager.registerProviderType(OtherConfig.spiritRealmDimID, WorldProviderSpiritRealm.class, false);
        DimensionManager.registerDimension(OtherConfig.spiritRealmDimID, OtherConfig.spiritRealmDimID);

        new BiomeGenSpiritRealm();
    }

    public void init(FMLInitializationEvent event) {
        PacketHandler.init();
        ModTileEntities.init();
        ModKeybindings.init();
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {}
}
