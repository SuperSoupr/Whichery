package com.supersouper.whichery.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.supersouper.whichery.CommonProxy;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.items.ItemChalk;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ChalkRuneISBRH implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final ChalkRuneISBRH INSTANCE = new ChalkRuneISBRH();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        if (!(world.getTileEntity(x, y, z) instanceof ChalkRuneTileEntity cte)) return false;

        render(cte.getType(), x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return CommonProxy.chalkRuneRenderID;
    }

    private static void render(String type, int x, int y, int z) {

        Tessellator t = Tessellator.instance;

        IIcon icon = Blocks.rail.getIcon(0, 0);
        // TODO custom chalk textures for each type
        int color = RitualRegistry.CHALK_TYPES.get(type);
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        // GL11.glColor3f(r / 255f, g / 255f, b / 255f);
        // GL11.glPushMatrix();
        // GL11.glTranslated(x, y, z);
        // GL11.glRotatef(90, 1, 0, 0);
        // t.addVertexWithUV(0, 1, (double)(0 - icon.getIconHeight()), (double)icon.getMinU(), (double)icon.getMaxV());
        // t.addVertexWithUV(1, 1, (double)(0 - icon.getIconHeight()), (double)icon.getMaxU(), (double)icon.getMaxV());
        // t.addVertexWithUV(1, 0, (double)(0 - icon.getIconHeight()), (double)icon.getMaxU(), (double)icon.getMinV());
        // t.addVertexWithUV(0, 0, (double)(0 - icon.getIconHeight()), (double)icon.getMinU(), (double)icon.getMinV());
        // ItemRenderer.renderItemIn2D(
        // new NoStartTessellator(t),
        // icon.getMinU(),
        // icon.getMinV(),
        // icon.getMaxU(),
        // icon.getMaxV(),
        // icon.getIconWidth(),
        // icon.getIconHeight(),
        // 1f / 16f);
        icon = RitualRegistry.RUNE_ICONS.get(type)[0];
        t.setColorOpaque(r, g, b);
        t.addTranslation(x, y, z);
        renderIconIn2D(t, icon, 1f / 16f);
        t.addTranslation(-x, -y, -z);
        // GL11.glPopMatrix();
        // GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public static void renderIconIn2D(Tessellator t, IIcon icon, float width) {
        float minU = icon.getMinU();
        float minV = icon.getMinV();
        float maxU = icon.getMaxU();
        float maxV = icon.getMaxV();
        float iconWidth = icon.getIconWidth() / 2;
        float iconHeight = icon.getIconHeight() / 2;

        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, 0, 0, minU, maxV);
        t.addVertexWithUV(1, 0, 0, maxU, maxV);
        t.addVertexWithUV(1, 0, 1, maxU, minV);
        t.addVertexWithUV(0, 0, 1, minU, minV);

        t.setNormal(0, -1, 0);
        t.addVertexWithUV(0, width, 1, minU, minV);
        t.addVertexWithUV(1, width, 1, maxU, minV);
        t.addVertexWithUV(1, width, 0, maxU, maxV);
        t.addVertexWithUV(0, width, 0, minU, maxV);

        float f5 = 0.5f * (minU - maxU) / (float) iconWidth;
        float f6 = 0.5f * (maxV - minV) / (float) iconHeight;

        t.setBrightness(160);
        t.setNormal(-1, 0, 0);
        int k;
        float f7;
        float f8;

        for (k = 0; k < iconWidth; ++k) {
            f7 = (float) k / (float) iconWidth;
            f8 = minU + (maxU - minU) * f7 - f5;
            t.addVertexWithUV(f7, width, 0, f8, maxV);
            t.addVertexWithUV(f7, 0, 0, f8, maxV);
            t.addVertexWithUV(f7, 0, 1, f8, minV);
            t.addVertexWithUV(f7, width, 1, f8, minV);
        }

        t.setNormal(1, 0, 0);
        float f9;

        for (k = 0; k < iconWidth; ++k) {
            f7 = (float) k / (float) iconWidth;
            f8 = minU + (maxU - minU) * f7 - f5;
            f9 = (float) ((f7 + 1 / (float) iconWidth));
            t.addVertexWithUV(f9, width, 1, f8, minV);
            t.addVertexWithUV(f9, 0, 1, f8, minV);
            t.addVertexWithUV(f9, 0, 0, f8, maxV);
            t.addVertexWithUV(f9, width, 0, f8, maxV);
        }

        t.setBrightness(170);
        t.setNormal(0, 0, 1);

        for (k = 0; k < iconHeight; ++k) {
            f7 = (float) k / (float) iconHeight;
            f8 = maxV + (minV - maxV) * f7 - f6;
            f9 = f7 + 1 / (float) iconHeight;

            t.addVertexWithUV(0, 0, f9, minU, f8);
            t.addVertexWithUV(1, 0, f9, maxU, f8);
            t.addVertexWithUV(1, width, f9, maxU, f8);
            t.addVertexWithUV(0, width, f9, minU, f8);
        }

        t.setNormal(0, 0, -1);

        for (k = 0; k < iconHeight; ++k) {
            f7 = (float) k / (float) iconHeight;
            f8 = maxV + (minV - maxV) * f7 - f6;
            t.addVertexWithUV(1, 0, f7, maxU, f8);
            t.addVertexWithUV(0, 0, f7, minU, f8);
            t.addVertexWithUV(0, width, f7, minU, f8);
            t.addVertexWithUV(1, width, f7, maxU, f8);
        }
        t.setBrightness(240);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return switch (helper) {
            case ENTITY_ROTATION, ENTITY_BOBBING, INVENTORY_BLOCK -> true;
            default -> false;
        };
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glRotatef(-90, 1, 0, 0);
        GL11.glTranslatef(-0.5f, 0, -0.5f);
        render(ItemChalk.getChalkType(item), 0, 0, 0);
        GL11.glPopMatrix();
    }
}
