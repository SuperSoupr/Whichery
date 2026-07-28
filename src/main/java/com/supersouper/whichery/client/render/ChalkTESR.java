package com.supersouper.whichery.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.supersouper.whichery.common.tileentities.ChalkTileEntity;

public class ChalkTESR extends TileEntitySpecialRenderer implements IItemRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        render((ChalkTileEntity) tileEntity, x, y, z, partialTicks);
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
        render(null, 0, 0, 0, 0);
        GL11.glPopMatrix();
    }

    private static void render(ChalkTileEntity tileEntity, double x, double y, double z, float partialTicks) {
        Tessellator t = Tessellator.instance;
        Minecraft mc = Minecraft.getMinecraft();
        int previousTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        IIcon icon = Blocks.stained_hardened_clay.getIcon(0, tileEntity != null ? tileEntity.getType() : 0);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(90, 1, 0, 0);
        ItemRenderer.renderItemIn2D(
            t,
            icon.getMinU(),
            icon.getMinV(),
            icon.getMaxU(),
            icon.getMaxV(),
            icon.getIconWidth(),
            icon.getIconHeight(),
            1f / 16f);
        GL11.glPopMatrix();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, previousTex);
    }
}
