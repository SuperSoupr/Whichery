package com.supersouper.whichery;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.client.gui.BloodMeterRenderer;
import com.supersouper.whichery.client.render.ChalkRuneISBRH;
import com.supersouper.whichery.client.render.ChalkRuneSmallISBRH;
import com.supersouper.whichery.client.render.ChalkStickItemRender;
import com.supersouper.whichery.common.entity.PlacedEntityItem;
import com.supersouper.whichery.common.rituals.matching.BlockMatcherChalk;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        RitualRegistry.RUNE_ICONS = new HashMap<>();
        RitualRegistry.RUNE_ICONS_SMALL = new HashMap<>();
        RitualRegistry.CHALK_STICK_ICONS = new HashMap<>();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new BloodMeterRenderer(Minecraft.getMinecraft()));
        if (ModItems.CHALK_STICK.isEnabled()) {
            MinecraftForgeClient.registerItemRenderer(ModBlocks.CHALK_RUNE_BLOCK.getItem(), ChalkRuneISBRH.INSTANCE);
            MinecraftForgeClient
                .registerItemRenderer(ModBlocks.CHALK_RUNE_BLOCK_SMALL.getItem(), ChalkRuneSmallISBRH.INSTANCE);
            MinecraftForgeClient.registerItemRenderer(ModItems.CHALK_STICK.get(), new ChalkStickItemRender());

            RitualRegistry
                .registerItemHasher(ModBlocks.CHALK_RUNE_BLOCK.getItem(), BlockMatcherChalk::chalkItemStackToHashCode);
            RitualRegistry.registerItemHasher(ModItems.CHALK_STICK.get(), BlockMatcherChalk::chalkItemStackToHashCode);
            RitualRegistry
                .registerItemHasher(ModItems.CHALK_STICK_SMALL.get(), BlockMatcherChalk::chalkItemStackToHashCode);

            RenderingRegistry.registerEntityRenderingHandler(
                PlacedEntityItem.class,
                RenderManager.instance.getEntityClassRenderObject(EntityItem.class));
        }
    }
}
