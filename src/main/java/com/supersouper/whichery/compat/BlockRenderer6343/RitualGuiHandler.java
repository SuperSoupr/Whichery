package com.supersouper.whichery.compat.BlockRenderer6343;

import com.supersouper.whichery.api.rituals.RitualRecipe;

import blockrenderer6343.integration.nei.GuiMultiblockHandler;

public class RitualGuiHandler extends GuiMultiblockHandler {

    @Override
    protected void placeMultiblock() {
        if (renderingController instanceof RitualRecipe ritual) {
            ritual.construct(renderer.world, MB_PLACE_POS.x, MB_PLACE_POS.y, MB_PLACE_POS.z);
        }
        renderingController.construct(getBuildTriggerStack(), false);
    }
}
