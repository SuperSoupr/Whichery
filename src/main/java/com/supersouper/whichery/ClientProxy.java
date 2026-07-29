package com.supersouper.whichery;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.supersouper.whichery.client.gui.BloodMeterRenderer;
import com.supersouper.whichery.client.render.ChalkTESR;
import com.supersouper.whichery.client.render.entity.RenderMandrakeRoot;
import com.supersouper.whichery.common.entity.EntityMandrakeRoot;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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
        RenderingRegistry.registerEntityRenderingHandler(EntityMandrakeRoot.class, new RenderMandrakeRoot());
    }
}
