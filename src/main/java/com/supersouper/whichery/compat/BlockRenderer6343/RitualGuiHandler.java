package com.supersouper.whichery.compat.BlockRenderer6343;

import net.minecraft.tileentity.TileEntity;

import org.jetbrains.annotations.NotNull;

import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.common.tileentities.ChalkRuneSmallTileEntity;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;

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
            return ritual.getDisplayName();
        }
        return super.getMultiblockName();
    }

    private long lastUpdate;

    @Override
    public void onRendererRender(WorldSceneRenderer renderer) {
        super.onRendererRender(renderer);

        if (System.currentTimeMillis() - this.lastUpdate < 1000) return;
        this.lastUpdate = System.currentTimeMillis();

        for (TileEntity te : renderer.world.tileMap.values()) {
            if (te.getClass() == ChalkRuneSmallTileEntity.class) {
                ((ChalkRuneSmallTileEntity) te).tryCycleRune();
            } else if (te.getClass() == ChalkRuneTileEntity.class) {
                ((ChalkRuneTileEntity) te).tryCycleRune();
            }
        }
    }
}
