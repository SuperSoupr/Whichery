package com.supersouper.whichery.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

// Handles rendering of the Waterleaf stem, which must render the water's surface in addition to the crop.
public class RenderWaterleafStem implements ISimpleBlockRenderingHandler {

    private final int modelID;

    public RenderWaterleafStem(int id) {
        this.modelID = id;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int id, RenderBlocks renderer) {

    }

    // Returns the crop renderer on pass 0, and the water renderer on pass 1.
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int id,
        RenderBlocks renderer) {
        if (ForgeHooksClient.getWorldRenderPass() == 1) {
            return renderer.renderBlockLiquid(Blocks.water, x, y, z);
        } else {
            return renderer.renderCrossedSquares(block, x, y, z);
        }
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return modelID;
    }
}
