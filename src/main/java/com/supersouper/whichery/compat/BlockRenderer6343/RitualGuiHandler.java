package com.supersouper.whichery.compat.BlockRenderer6343;

import net.minecraft.tileentity.TileEntity;

import org.jetbrains.annotations.NotNull;

import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.common.tileentities.ChalkSmallTileEntity;

import blockrenderer6343.client.renderer.WorldSceneRenderer;
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
            ritual.recipe.construct(renderer.world, MB_PLACE_POS.x, MB_PLACE_POS.y, MB_PLACE_POS.z);
        }
        renderingController.construct(getBuildTriggerStack(), false);
    }

    @Override
    protected @NotNull String getMultiblockName() {
        if (renderingController instanceof Ritual ritual) {
            return "whichery.ritual." + ritual.name + ".name";
        }
        return super.getMultiblockName();
    }

    @Override
    public void onRendererRender(WorldSceneRenderer renderer) {
        super.onRendererRender(renderer);
        for (TileEntity te : renderer.world.tileMap.values()) {
            if (te.getClass() == ChalkSmallTileEntity.class) {
                ((ChalkSmallTileEntity) te).tryCycleRune();
            }
        }
    }
}
