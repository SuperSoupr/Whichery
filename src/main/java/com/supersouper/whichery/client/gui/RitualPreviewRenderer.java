package com.supersouper.whichery.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.RitualUtils;
import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.utils.DrawUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RitualPreviewRenderer {

    private static final ResourceLocation PREVIEW_TEX = new ResourceLocation(
        "whichery",
        "textures/gui/ritual_preview.png");

    private static final ArrayList<Preview> previews = new ArrayList<>();

    // static {
    // previews = new ArrayList<>();
    // previews.add(new Preview(RitualRegistry.getRitual("banana2"), 10, 10));
    // }

    public static void addPreview(Ritual ritual, int worldX, int worldY, int worldZ) {
        previews.clear();

        previews.add(new Preview(ritual, 10, 10, worldX, worldY, worldZ));
    }

    public static void removePreview(Ritual ritual) {
        previews.removeIf(preview -> preview.ritual.equals(ritual));
    }

    private static void draw() {
        Tessellator t = Tessellator.instance;
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen == null && mc.theWorld != null
            && Minecraft.isGuiEnabled()
            && !mc.gameSettings.keyBindPlayerList.getIsKeyPressed())) return;
        for (Preview preview : previews) {
            int x = preview.x + 10;
            int y = preview.y + 10;
            int margin = 2;
            int rawWidth = preview.ritual.recipe.size[0] * 8;
            int rawHeight = preview.ritual.recipe.size[2] * 8;
            int w = margin * 2 + rawWidth;
            int h = margin * 2 + rawHeight;

            mc.getTextureManager()
                .bindTexture(PREVIEW_TEX);
            float scale = 1;
            GL11.glScalef(scale, scale, scale);
            GL11.glColor3f(1, 1, 1);
            drawBorder(t, x, y, w, h);
            drawBackground(t, x, y, w, h);

            mc.getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            for (int i = 0; i < preview.ritual.recipe.matchers.length; i++) {
                IBlockMatcher matcher = preview.ritual.recipe.matchers[i];
                byte[] pos = new byte[3];
                RitualUtils.unpackCoords(preview.ritual.recipe.matcherPositions[i], pos);

                t.startDrawingQuads();
                matcher.drawIcon(t, x + 8 + margin + pos[0] * 8, y + 8 + margin + pos[2] * 8, 8, 8);
                t.draw();
            }
            IIcon icon = Blocks.stone.getIcon(0, 0);
            t.startDrawingQuads();
            DrawUtils.drawRect(
                t,
                Math.min(
                    w + x + (8 + margin),
                    (int) (x + 8 + margin + ((mc.thePlayer.posX - preview.worldX - 0.5) * 8) + (double) rawWidth / 2)),
                (int) (y + 8 + margin + ((mc.thePlayer.posZ - preview.worldZ - 0.5) * 8) + (double) rawHeight / 2),
                0,
                2,
                2,
                icon.getMinU(),
                icon.getMinV(),
                icon.getMaxU(),
                icon.getMaxV());
            t.draw();

            GL11.glScalef(1f / scale, 1f / scale, 1f / scale);
        }
    }

    private static void drawBackground(Tessellator t, int x, int y, int w, int h) {
        t.startDrawingQuads();
        DrawUtils.drawTiledRect(t, x + 8, y + 8, 0, w, h, 0, 8 / 16.0, 1, 1, 16, 8);
        t.draw();
    }

    private static void drawBorder(Tessellator t, int x, int y, int w, int h) {
        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, 0);

        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, 8, 0, 0, w, 8, 8 / 16.0, 0 / 16.0, 16 / 16.0, 8 / 16.0, 8);
        DrawUtils.drawRect(t, 0, 0, 0, 8, 8, 0, 0, 8d / 16d, 8d / 16d);
        t.draw();

        GL11.glTranslatef(8, 8, 0);
        GL11.glRotatef(-90, 0, 0, 1f);
        GL11.glTranslatef(-8, -8, 0);
        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, -h + 8, 0, 0, h, 8, 8 / 16.0, 0 / 16.0, 16 / 16.0, 8 / 16.0, 8);
        DrawUtils.drawRect(t, -h, 0, 0, 8, 8, 0, 0, 8d / 16d, 8d / 16d);
        t.draw();
        GL11.glPopMatrix();

        GL11.glPushMatrix();

        float pivotX = (x + w + 16) / 2f;
        float pivotY = (y + h + 16) / 2f;
        GL11.glTranslatef(pivotX, pivotY, 0);
        GL11.glRotatef(180, 0, 0, 1f);
        GL11.glTranslatef(-pivotX, -pivotY, 0);

        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, 8, 0, 0, w, 8, 8 / 16.0, 0 / 16.0, 16 / 16.0, 8 / 16.0, 8);
        DrawUtils.drawRect(t, 0, 0, 0, 8, 8, 0, 0, 8d / 16d, 8d / 16d);
        t.draw();

        GL11.glTranslatef(8, 8, 0);
        GL11.glRotatef(-90, 0, 0, 1f);
        GL11.glTranslatef(-8, -8, 0);
        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, -h + 8, 0, 0, h, 8, 8 / 16.0, 0 / 16.0, 16 / 16.0, 8 / 16.0, 8);
        DrawUtils.drawRect(t, -h, 0, 0, 8, 8, 0, 0, 8d / 16d, 8d / 16d);
        t.draw();

        GL11.glPopMatrix();

    }

    private static class Preview {

        public final Ritual ritual;
        public final int x, y;
        int worldX, worldY, worldZ;

        Preview(Ritual ritual, int x, int y, int worldX, int worldY, int worldZ) {
            this.ritual = ritual;
            this.x = x;
            this.y = y;
            this.worldX = worldX;
            this.worldY = worldY;
            this.worldZ = worldZ;
        }
    }

    @SideOnly(Side.CLIENT)
    @EventBusSubscriber(side = Side.CLIENT)
    public static class Events {

        @SubscribeEvent
        public static void tickRender(TickEvent.RenderTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            draw();
        }
    }
}
