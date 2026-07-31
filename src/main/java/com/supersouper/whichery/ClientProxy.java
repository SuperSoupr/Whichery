package com.supersouper.whichery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.supersouper.whichery.api.rituals.BlockMatcherChalk;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.client.gui.BloodMeterRenderer;
import com.supersouper.whichery.client.render.ChalkRuneTESR;
import com.supersouper.whichery.common.entity.PlacedEntityItem;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new BloodMeterRenderer(Minecraft.getMinecraft()));
        if (ModItems.CHALK.isEnabled()) {
            ChalkRuneTESR renderer = new ChalkRuneTESR();
            ClientRegistry.bindTileEntitySpecialRenderer(ModTileEntities.CHALK.getTileEntityClass(), renderer);
            MinecraftForgeClient.registerItemRenderer(ModBlocks.CHALK_RUNE_BLOCK.getItem(), renderer);

            RitualRegistry
                .registerItemHasher(ModBlocks.CHALK_RUNE_BLOCK.getItem(), BlockMatcherChalk::itemStackToHashCode);

            RenderingRegistry.registerEntityRenderingHandler(
                PlacedEntityItem.class,
                RenderManager.instance.getEntityClassRenderObject(EntityItem.class));
        }
    }
}
