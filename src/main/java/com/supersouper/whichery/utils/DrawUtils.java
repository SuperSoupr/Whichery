package com.supersouper.whichery.utils;

import net.minecraft.client.renderer.Tessellator;

public class DrawUtils {

    public static void drawRect(Tessellator t, int x, int y, double z, int width, int height, double minU, double minV,
        double maxU, double maxV) {
        t.addVertexWithUV(x, y + height, z, minU, maxV);
        t.addVertexWithUV(x + width, y + height, z, maxU, maxV);
        t.addVertexWithUV(x + width, y, z, maxU, minV);
        t.addVertexWithUV(x, y, z, minU, minV);
    }

    public static void drawTiledRectH(Tessellator t, int x, int y, double z, int totalWidth, int height, double minU,
        double minV, double maxU, double maxV, int tileWidthPixels) {
        double uPerPixel = (maxU - minU) / tileWidthPixels;

        for (int drawn = 0; drawn < totalWidth; drawn += tileWidthPixels) {
            int remaining = totalWidth - drawn;
            int w = Math.min(tileWidthPixels, remaining);

            // if the last tile is cut short, clip the U range to match
            double u2 = (w == tileWidthPixels) ? maxU : minU + (w * uPerPixel);

            drawRect(t, x + drawn, y, z, w, height, minU, minV, u2, maxV);
        }
    }

    public static void drawTiledRectV(Tessellator t, int x, int y, double z, int width, int totalHeight, double minU,
        double minV, double maxU, double maxV, int tileHeightPixels) {
        double vPerPixel = (maxV - minV) / tileHeightPixels;

        for (int drawn = 0; drawn < totalHeight; drawn += tileHeightPixels) {
            int remaining = totalHeight - drawn;
            int h = Math.min(tileHeightPixels, remaining);

            double v2 = (h == tileHeightPixels) ? maxV : minV + (h * vPerPixel);

            drawRect(t, x, y + drawn, z, width, h, minU, minV, maxU, v2);
        }
    }

    public static void drawTiledRect(Tessellator t, int x, int y, double z, int totalWidth, int totalHeight,
        double minU, double minV, double maxU, double maxV, int tileWidthPixels, int tileHeightPixels) {
        double uPerPixel = (maxU - minU) / tileWidthPixels;
        double vPerPixel = (maxV - minV) / tileHeightPixels;

        for (int drawnY = 0; drawnY < totalHeight; drawnY += tileHeightPixels) {
            int remainingH = totalHeight - drawnY;
            int h = Math.min(tileHeightPixels, remainingH);
            double v2 = (h == tileHeightPixels) ? maxV : minV + (h * vPerPixel);

            for (int drawnX = 0; drawnX < totalWidth; drawnX += tileWidthPixels) {
                int remainingW = totalWidth - drawnX;
                int w = Math.min(tileWidthPixels, remainingW);
                double u2 = (w == tileWidthPixels) ? maxU : minU + (w * uPerPixel);

                drawRect(t, x + drawnX, y + drawnY, z, w, h, minU, minV, u2, v2);
            }
        }
    }
}
