package com.supersouper.whichery;

import com.supersouper.whichery.client.render.ChalkTESR;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        if (ModTileEntities.CHALK.isEnabled()) {
            ClientRegistry.bindTileEntitySpecialRenderer(ModTileEntities.CHALK.getTileEntityClass(), new ChalkTESR());
        }
    }
}
