package com.supersouper.whichery;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.supersouper.whichery.api.rituals.BlockMatcherChalk;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.client.gui.BloodMeterRenderer;
import com.supersouper.whichery.client.render.ChalkTESR;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new BloodMeterRenderer(Minecraft.getMinecraft()));
        if (ModItems.CHALK.isEnabled()) {
            ChalkTESR renderer = new ChalkTESR();
            ClientRegistry.bindTileEntitySpecialRenderer(ModTileEntities.CHALK.getTileEntityClass(), renderer);
            MinecraftForgeClient.registerItemRenderer(ModBlocks.CHALK_BLOCK.getItem(), renderer);

            RitualRegistry.registerItemHasher(ModBlocks.CHALK_BLOCK.getItem(), BlockMatcherChalk::itemStackToHashCode);
        }
    }
}
