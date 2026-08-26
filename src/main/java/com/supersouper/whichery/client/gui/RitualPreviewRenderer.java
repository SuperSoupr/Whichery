package com.supersouper.whichery.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.MouseEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.api.rituals.Ritual;
import com.supersouper.whichery.api.rituals.RitualUtils;
import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.common.items.ItemRitualPreview;
import com.supersouper.whichery.utils.DrawUtils;
import com.supersouper.whichery.utils.WhicheryUtils;

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
    private static boolean large = false;

    private static final int[] rotationCorrections = new int[] { 2, 1, 0, 3 };
    private static final int[] rotationCorrections2 = new int[] { 0, 3, 2, 1 };

    public static void addPreview(Ritual ritual, IBlockAccess world, int worldX, int worldY, int worldZ, int rotation) {
        previews.clear();

        previews.add(new Preview(ritual, 10, 10, world, worldX, worldY, worldZ, rotationCorrections[rotation]));
    }

    public static void clearPreviews() {
        previews.clear();
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

        GL11.glEnable(GL11.GL_BLEND);

        ArrayList<Preview> toRemove = new ArrayList<>();
        byte[] pos = new byte[3];
        int[] pos2d = new int[2];
        double[] pos2dd = new double[2];
        ArrayList<TileEntity> dummy = new ArrayList<>();
        for (Preview preview : previews) {
            int margin = 2;
            int rawWidth = preview.ritual.recipe.size[0] * 8 + preview.ritual.recipe.size[1] - 1;
            int rawHeight = preview.ritual.recipe.size[2] * 8 + preview.ritual.recipe.size[1] - 1;
            int w = margin * 2 + rawWidth;
            int h = margin * 2 + rawHeight;

            pos2dd[0] = mc.thePlayer.posX - preview.worldX;
            pos2dd[1] = mc.thePlayer.posZ - preview.worldZ;
            for (int j = 0; j < preview.rotation; j++) {
                WhicheryUtils.rotated(pos2dd, 0.5, 0.5);
            }
            double playerMarkerX = 8 + margin + ((pos2dd[0] - 0.5) * 8) + (double) rawWidth / 2;
            double playerMarkerY = 8 + margin + ((pos2dd[1] - 0.5) * 8) + (double) rawHeight / 2;

            if (playerMarkerX < 8 || playerMarkerX > w + 8 || playerMarkerY < 8 || playerMarkerY > h + 8) {
                continue;
            }
            GL11.glPushMatrix();
            GL11.glTranslated(preview.x, preview.y, 0);

            mc.getTextureManager()
                .bindTexture(PREVIEW_TEX);
            float scale = large ? 2 : 1;
            GL11.glScalef(scale, scale, scale);

            drawBorder(t, w, h);
            drawBackground(t, w, h);
            drawPlayerMarker(t, preview, playerMarkerX, playerMarkerY, mc.thePlayer.rotationYaw);
            drawTargetMarker(t, preview, pos2dd, w, h, rawWidth, rawHeight, margin);

            mc.getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int amountDrawn = 0;
            for (int i = 0; i < preview.ritual.recipe.matchers.length; i++) {
                IBlockMatcher matcher = preview.ritual.recipe.matchers[i];
                RitualUtils.unpackCoords(preview.ritual.recipe.matcherPositions[i], pos);

                double alpha = Math
                    .max(0.05, Math.pow(0.2d, Math.abs(mc.thePlayer.boundingBox.minY - (preview.worldY + pos[1]))));
                GL11.glColor4d(1, 1, 1, alpha);
                t.startDrawingQuads();

                pos2d[0] = pos[0];
                pos2d[1] = pos[2];
                for (int j = 0; j < rotationCorrections2[preview.rotation]; j++) {
                    WhicheryUtils.rotate(pos2d, preview.ritual.recipe.centerX, preview.ritual.recipe.centerZ);
                }
                if (!matcher.match(
                    preview.world,
                    preview.worldX - preview.ritual.recipe.centerX + pos2d[0],
                    preview.worldY + pos[1],
                    preview.worldZ - preview.ritual.recipe.centerZ + pos2d[1],
                    dummy)) {
                    matcher.drawIcon(
                        t,
                        preview.world.getTileEntity(
                            preview.worldX - preview.ritual.recipe.centerX + pos2d[0],
                            preview.worldY + pos[1],
                            preview.worldZ - preview.ritual.recipe.centerZ + pos2d[1]),
                        8 + margin + pos[0] * 8 + pos[1],
                        8 + margin + pos[2] * 8 + pos[1],
                        pos[1],
                        8,
                        8,
                        alpha);
                    amountDrawn++;
                }
                t.draw();
                GL11.glColor4f(1, 1, 1, 1);
            }
            if (amountDrawn == 0) {
                toRemove.add(preview);
            }
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            // GL11.glScalef(1f / scale, 1f / scale, 1f / scale);
            GL11.glPopMatrix();
        }
        previews.removeAll(toRemove);
    }

    private static void drawTargetMarker(Tessellator t, Preview preview, double[] pos2dd, int w, int h, double rawWidth,
        double rawHeight, int margin) {
        MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;

        pos2dd[0] = mop.blockX - preview.worldX;
        pos2dd[1] = mop.blockZ - preview.worldZ;
        int cy = mop.blockY - preview.worldY;
        for (int j = 0; j < preview.rotation; j++) {
            WhicheryUtils.rotated(pos2dd, 0, 0);
        }
        int targetBlockX = (int) (8 + margin + ((pos2dd[0] - 0.5) * 8) + rawWidth / 2 + cy);
        int targetBlockY = (int) (8 + margin + ((pos2dd[1] - 0.5) * 8) + rawHeight / 2 + cy);
        if (targetBlockX < 8 || targetBlockX > w || targetBlockY < 8 || targetBlockY > h) return;

        t.startDrawingQuads();
        DrawUtils.drawRect(
            t,
            (int) Math.max(8, Math.min(w + 8, 8 + margin + ((pos2dd[0] - 0.5) * 8) + rawWidth / 2 + cy)),
            (int) Math.max(8, Math.min(h + 8, 8 + margin + ((pos2dd[1] - 0.5) * 8) + rawHeight / 2 + cy)),
            100,
            8,
            8,
            0.5,
            0.5,
            1,
            1);
        t.draw();
    }

    private static void drawPlayerMarker(Tessellator t, Preview preview, double playerMarkerX, double playerMarkerY,
        float rotationYaw) {
        GL11.glPushMatrix();
        GL11.glTranslated(playerMarkerX, playerMarkerY, 0);
        GL11.glRotatef(rotationYaw + 90 * preview.rotation + 180, 0, 0, 1);
        t.startDrawingQuads();
        DrawUtils.drawRect(t, -4, -4, 10, 8, 8, 0.5, 0, 1, 0.5);
        t.draw();
        GL11.glPopMatrix();
    }

    private static void drawBackground(Tessellator t, int w, int h) {
        t.startDrawingQuads();
        DrawUtils.drawTiledRect(t, 8, 8, 0, w, h, 0, 16 / 32.0, 16 / 32f, 1, 16, 16);
        t.draw();
    }

    private static void drawBorder(Tessellator t, int w, int h) {
        GL11.glPushMatrix();

        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, 8, 0, 0, w, 8, 8 / 32.0, 0 / 32.0, 16 / 32.0, 8 / 32.0, 8);
        DrawUtils.drawRect(t, 0, 0, 0, 8, 8, 0, 0, 8d / 32d, 8d / 32d);
        t.draw();

        GL11.glTranslatef(8, 8, 0);
        GL11.glRotatef(-90, 0, 0, 1f);
        GL11.glTranslatef(-8, -8, 0);
        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, -h + 8, 0, 0, h, 8, 8 / 32.0, 0 / 32.0, 16 / 32.0, 8 / 32.0, 8);
        DrawUtils.drawRect(t, -h, 0, 0, 8, 8, 0, 0, 8d / 32d, 8d / 32d);
        t.draw();
        GL11.glPopMatrix();

        GL11.glPushMatrix();

        float pivotX = (w + 16) / 2f;
        float pivotY = (h + 16) / 2f;
        GL11.glTranslatef(pivotX, pivotY, 0);
        GL11.glRotatef(180, 0, 0, 1f);
        GL11.glTranslatef(-pivotX, -pivotY, 0);

        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, 8, 0, 0, w, 8, 8 / 32.0, 0 / 32.0, 16 / 32.0, 8 / 32.0, 8);
        DrawUtils.drawRect(t, 0, 0, 0, 8, 8, 0, 0, 8d / 32d, 8d / 32d);
        t.draw();

        GL11.glTranslatef(8, 8, 0);
        GL11.glRotatef(-90, 0, 0, 1f);
        GL11.glTranslatef(-8, -8, 0);
        t.startDrawingQuads();
        DrawUtils.drawTiledRectH(t, -h + 8, 0, 0, h, 8, 8 / 32.0, 0 / 32.0, 16 / 32.0, 8 / 32.0, 8);
        DrawUtils.drawRect(t, -h, 0, 0, 8, 8, 0, 0, 8d / 32d, 8d / 32d);
        t.draw();

        GL11.glPopMatrix();

    }

    private static class Preview {

        public final Ritual ritual;
        public final int x, y;
        public final IBlockAccess world;
        public final int worldX, worldY, worldZ;
        public final int rotation;

        Preview(Ritual ritual, int x, int y, IBlockAccess world, int worldX, int worldY, int worldZ, int rotation) {
            this.ritual = ritual;
            this.x = x;
            this.y = y;
            this.world = world;
            this.worldX = worldX;
            this.worldY = worldY - ritual.recipe.centerY;
            this.worldZ = worldZ;
            this.rotation = rotation;
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

        @SubscribeEvent
        public static void onMouseEvent(MouseEvent event) {
            if (event.button == 0 && event.buttonstate) {
                Minecraft mc = Minecraft.getMinecraft();

                if (mc.thePlayer != null && mc.currentScreen == null) {

                    if (mc.objectMouseOver == null
                        || mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
                        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem()
                            .getItem() != null
                            && mc.thePlayer.getHeldItem()
                                .getItem()
                                .getClass() == ItemRitualPreview.class) {
                            large = !large;
                        }
                    }
                }
            }
        }
    }
}
