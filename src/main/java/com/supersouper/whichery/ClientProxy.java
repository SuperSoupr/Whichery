package com.supersouper.whichery;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.supersouper.whichery.client.gui.BloodMeterRenderer;
import com.supersouper.whichery.client.render.ChalkTESR;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new BloodMeterRenderer(Minecraft.getMinecraft()));
        if (ModTileEntities.CHALK.isEnabled()) {
            ClientRegistry.bindTileEntitySpecialRenderer(ModTileEntities.CHALK.getTileEntityClass(), new ChalkTESR());
        }
        ModEntities.initClient();
    }
}
