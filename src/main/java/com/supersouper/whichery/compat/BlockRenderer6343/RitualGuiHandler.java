package com.supersouper.whichery.compat.BlockRenderer6343;

import com.supersouper.whichery.api.rituals.RitualRecipe;

import blockrenderer6343.integration.nei.GuiMultiblockHandler;

public class RitualGuiHandler extends GuiMultiblockHandler {

    @Override
    protected void placeMultiblock() {

        // Block stackBlock = ((ItemBlock) stackForm.getItem()).field_150939_a;
        // renderer.world
        // .setBlock(MB_PLACE_POS.x, MB_PLACE_POS.y, MB_PLACE_POS.z, stackBlock, stackForm.getItemDamage(), 3);

        // TileEntity tTileEntity = renderer.world.getTileEntity(MB_PLACE_POS.x, MB_PLACE_POS.y, MB_PLACE_POS.z);
        // IMultiblockInfoContainer<TileEntity> t = IMultiblockInfoContainer.get(tTileEntity.getClass());
        // ISurvivalConstructable multi = t.toConstructable(tTileEntity, ExtendedFacing.DEFAULT);
        //
        // int result, iterations = 0;
        // boolean tryConstruct = false;
        // do {
        // result = multi.survivalConstruct(
        // getBuildTriggerStack(),
        // Integer.MAX_VALUE,
        // ISurvivalBuildEnvironment.create(CreativeItemSource.instance, FAKE_PLAYER));
        // iterations++;
        // if (result == -2) {
        // tryConstruct = true;
        // break;
        // }
        // } while (renderer.world.hasChanged() && iterations < MAX_PLACE_ROUNDS);

        // if (tryConstruct) {
        if (renderingController instanceof RitualRecipe ritual) {
            ritual.construct(renderer.world, MB_PLACE_POS.x, MB_PLACE_POS.y, MB_PLACE_POS.z);
        }
        renderingController.construct(getBuildTriggerStack(), false);
        // }
        //
        // // A single tick is needed for some non GT multiblocks to complete
        // renderer.world.updateEntitiesForNEI();
    }
}
