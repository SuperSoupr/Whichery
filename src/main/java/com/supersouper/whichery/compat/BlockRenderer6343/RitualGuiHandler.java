package com.supersouper.whichery.compat.BlockRenderer6343;

import org.jetbrains.annotations.NotNull;

import com.supersouper.whichery.api.rituals.Ritual;

import blockrenderer6343.integration.nei.GuiMultiblockHandler;

public class RitualGuiHandler extends GuiMultiblockHandler {

    @Override
    protected void initGui() {
        super.initGui();
        allButtons.remove(tierSlider);
    }

    @Override
    protected void placeMultiblock() {
        if (renderingController instanceof Ritual ritual) {
            ritual.getRecipe()
                .construct(renderer.world, MB_PLACE_POS.x, MB_PLACE_POS.y, MB_PLACE_POS.z);
        }
        renderingController.construct(getBuildTriggerStack(), false);
    }

    @Override
    protected @NotNull String getMultiblockName() {
        if (renderingController instanceof Ritual ritual) {
            return "whichery.ritual." + ritual.getName() + ".name";
        }
        return super.getMultiblockName();
    }
}
