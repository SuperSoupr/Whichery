package com.supersouper.whichery.compat.nei;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.Tags;
import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.compat.BlockRenderer6343.RitualMultiblockHandler;
import com.supersouper.whichery.compat.Mods;

import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.HandlerInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
@SuppressWarnings("unused")
public class NEIConfig implements IConfigureNEI {

    @SubscribeEvent
    public static void registerHandler(NEIRegisterHandlerInfosEvent event) {
        if (Mods.BlockRenderer6343.isLoaded()) {
            event.registerHandlerInfo(
                new HandlerInfo.Builder(RitualMultiblockHandler.class, Whichery.MODNAME, Whichery.MODID)
                    .setDisplayStack(new ItemStack(ModBlocks.CHALK_BLOCK.getItem()))
                    .setHeight(168)
                    .setUseCustomScroll(true)
                    .setMultipleWidgetsAllowed(false)
                    .setShiftY(6)
                    .build());
        }
    }

    @Override
    public void loadConfig() {
        if (Mods.BlockRenderer6343.isLoaded()) {
            addHandler(new RitualMultiblockHandler());
        }
    }

    @Optional.Method(modid = "blockrenderer6343")
    private void addHandler(TemplateRecipeHandler handler) {
        GuiCraftingRecipe.craftinghandlers.add(handler);
        GuiUsageRecipe.usagehandlers.add(handler);
    }

    @Override
    public String getName() {
        return Whichery.MODNAME + " BlockRenderer6343";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }
}
