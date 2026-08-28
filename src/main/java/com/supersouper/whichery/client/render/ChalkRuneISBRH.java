package com.supersouper.whichery.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.supersouper.whichery.CommonProxy;
import com.supersouper.whichery.api.rituals.ChalkType;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.items.ItemChalkStick;
import com.supersouper.whichery.common.tileentities.ChalkRuneTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ChalkRuneISBRH implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final ChalkRuneISBRH INSTANCE = new ChalkRuneISBRH();

    public static IIcon storageIcon;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        if (!(world.getTileEntity(x, y, z) instanceof ChalkRuneTileEntity cte)) return false;

        render(cte.getType(), cte.getRune(), cte.getRotation(), x, y, z, false);
        if (cte.hasStorageUpgrade) {
            drawStorage(storageIcon, x, y, z);
        }

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

    private static void render(String type, int rune, int rotation, int x, int y, int z, boolean noBakedLight) {

        Tessellator t = Tessellator.instance;

        ChalkType chalkType = RitualRegistry.CHALK_TYPES.get(type);
        int color = chalkType.getRGB(0, rune);
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;

        IIcon icon = chalkType.icons[rune];
        t.addTranslation(x, y, z);
        renderIconIn2D(t, icon, 16, 1f / 16f, 1, 45 * rotation, r, g, b, false, noBakedLight);
        t.addTranslation(-x, -y, -z);

    }

    public static void renderIconIn2D(Tessellator t, IIcon icon, int iconSize, float width, float scale,
        float rotationDegrees, int r, int g, int b, boolean highlightBoth, boolean noBakedLights) {
        float minU = icon.getMinU();
        float minV = icon.getMinV();
        float maxU = icon.getMaxU();
        float maxV = icon.getMaxV();
        float iconWidth = iconSize;
        float iconHeight = iconSize;

        float rad = (float) Math.toRadians(rotationDegrees);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float pivotX = 0.5f;
        float pivotZ = 0.5f;

        t.setColorOpaque(r, g, b);
        setRotatedNormal(t, 0, -1, 0, sin, cos);
        addRotatedScaledVertex(t, 0, width, 1, minU, minV, sin, cos, pivotX, pivotZ, scale);
        addRotatedScaledVertex(t, 1, width, 1, maxU, minV, sin, cos, pivotX, pivotZ, scale);
        addRotatedScaledVertex(t, 1, width, 0, maxU, maxV, sin, cos, pivotX, pivotZ, scale);
        addRotatedScaledVertex(t, 0, width, 0, minU, maxV, sin, cos, pivotX, pivotZ, scale);

        if (!highlightBoth && !noBakedLights) t.setColorOpaque((int) (r * 0.5), (int) (g * 0.5), (int) (b * 0.5));
        setRotatedNormal(t, 0, 1, 0, sin, cos);
        addRotatedScaledVertex(t, 0, 0, 0, minU, maxV, sin, cos, pivotX, pivotZ, scale);
        addRotatedScaledVertex(t, 1, 0, 0, maxU, maxV, sin, cos, pivotX, pivotZ, scale);
        addRotatedScaledVertex(t, 1, 0, 1, maxU, minV, sin, cos, pivotX, pivotZ, scale);
        addRotatedScaledVertex(t, 0, 0, 1, minU, minV, sin, cos, pivotX, pivotZ, scale);

        float f5 = 0.5f * (minU - maxU) / iconWidth;
        float f6 = 0.5f * (maxV - minV) / iconHeight;

        if (!noBakedLights) t.setColorOpaque((int) (r * 0.6), (int) (g * 0.6), (int) (b * 0.6));
        setRotatedNormal(t, -1, 0, 0, sin, cos);
        int k;
        float f7;
        float f8;

        for (k = 0; k < iconWidth; ++k) {
            f7 = (float) k / iconWidth;
            f8 = minU + (maxU - minU) * f7 - f5;
            addRotatedScaledVertex(t, f7, width, 0, f8, maxV, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, f7, 0, 0, f8, maxV, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, f7, 0, 1, f8, minV, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, f7, width, 1, f8, minV, sin, cos, pivotX, pivotZ, scale);
        }

        setRotatedNormal(t, 1, 0, 0, sin, cos);
        float f9;

        for (k = 0; k < iconWidth; ++k) {
            f7 = (float) k / iconWidth;
            f8 = minU + (maxU - minU) * f7 - f5;
            f9 = f7 + 1.0f / iconWidth;
            addRotatedScaledVertex(t, f9, width, 1, f8, minV, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, f9, 0, 1, f8, minV, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, f9, 0, 0, f8, maxV, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, f9, width, 0, f8, maxV, sin, cos, pivotX, pivotZ, scale);
        }

        if (!noBakedLights) t.setColorOpaque((int) (r * 0.8), (int) (g * 0.8), (int) (b * 0.8));
        setRotatedNormal(t, 0, 0, 1, sin, cos);

        for (k = 0; k < iconHeight; ++k) {
            f7 = (float) k / iconHeight;
            f8 = maxV + (minV - maxV) * f7 - f6;
            f9 = f7 + 1.0f / iconHeight;

            addRotatedScaledVertex(t, 0, 0, f9, minU, f8, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, 1, 0, f9, maxU, f8, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, 1, width, f9, maxU, f8, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, 0, width, f9, minU, f8, sin, cos, pivotX, pivotZ, scale);
        }

        setRotatedNormal(t, 0, 0, -1, sin, cos);

        for (k = 0; k < iconHeight; ++k) {
            f7 = (float) k / iconHeight;
            f8 = maxV + (minV - maxV) * f7 - f6;
            addRotatedScaledVertex(t, 1, 0, f7, maxU, f8, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, 0, 0, f7, minU, f8, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, 0, width, f7, minU, f8, sin, cos, pivotX, pivotZ, scale);
            addRotatedScaledVertex(t, 1, width, f7, maxU, f8, sin, cos, pivotX, pivotZ, scale);
        }
    }

    private static void addRotatedScaledVertex(Tessellator t, float x, float y, float z, float u, float v, float sin,
        float cos, float pivotX, float pivotZ, float scale) {
        float dx = (x - pivotX) * scale;
        float dz = (z - pivotZ) * scale;

        float rx = dx * cos - dz * sin;
        float rz = dx * sin + dz * cos;

        t.addVertexWithUV(rx + pivotX, y * scale, rz + pivotZ, u, v);
    }

    private static void setRotatedNormal(Tessellator t, float nx, float ny, float nz, float sin, float cos) {
        float rx = nx * cos - nz * sin;
        float rz = nx * sin + nz * cos;
        t.setNormal(rx, ny, rz);
    }

    private static void drawStorage(IIcon icon, int x, int y, int z) {
        Tessellator t = Tessellator.instance;
        double uMin = icon.getMinU();
        double uMax = icon.getMaxU();
        double vMin = icon.getMinV();
        double vMax = icon.getMaxV();

        double minX = x + (6.999 / 16.0D);
        double maxX = x + (9.001 / 16.0D);
        double minY = y - 0.001;
        double maxY = y + (2.001D / 16.0D);
        double minZ = z + (6.999 / 16.0D);
        double maxZ = z + (9.001 / 16.0D);

        float r = 1f;
        float g = 1f;
        float b = 1f;

        // Y- (Bottom) - Darkest shading (0.5)
        t.setColorOpaque_F(r * 0.5F, g * 0.5F, b * 0.5F);
        t.addVertexWithUV(minX, minY, maxZ, uMin, vMax);
        t.addVertexWithUV(minX, minY, minZ, uMin, vMin);
        t.addVertexWithUV(maxX, minY, minZ, uMax, vMin);
        t.addVertexWithUV(maxX, minY, maxZ, uMax, vMax);

        // Y+ (Top) - Full brightness (1.0)
        t.setColorOpaque_F(r * 1.0F, g * 1.0F, b * 1.0F);
        t.addVertexWithUV(maxX, maxY, maxZ, uMax, vMax);
        t.addVertexWithUV(maxX, maxY, minZ, uMax, vMin);
        t.addVertexWithUV(minX, maxY, minZ, uMin, vMin);
        t.addVertexWithUV(minX, maxY, maxZ, uMin, vMax);

        // Z- (North) - Moderate shading (0.8)
        t.setColorOpaque_F(r * 0.8F, g * 0.8F, b * 0.8F);
        t.addVertexWithUV(minX, maxY, minZ, uMax, vMin);
        t.addVertexWithUV(maxX, maxY, minZ, uMin, vMin);
        t.addVertexWithUV(maxX, minY, minZ, uMin, vMax);
        t.addVertexWithUV(minX, minY, minZ, uMax, vMax);

        // Z+ (South) - Moderate shading (0.8)
        t.setColorOpaque_F(r * 0.8F, g * 0.8F, b * 0.8F);
        t.addVertexWithUV(minX, maxY, maxZ, uMin, vMin);
        t.addVertexWithUV(minX, minY, maxZ, uMin, vMax);
        t.addVertexWithUV(maxX, minY, maxZ, uMax, vMax);
        t.addVertexWithUV(maxX, maxY, maxZ, uMax, vMin);

        // X- (West) - Heavy shading (0.6)
        t.setColorOpaque_F(r * 0.6F, g * 0.6F, b * 0.6F);
        t.addVertexWithUV(minX, maxY, maxZ, uMax, vMin);
        t.addVertexWithUV(minX, maxY, minZ, uMin, vMin);
        t.addVertexWithUV(minX, minY, minZ, uMin, vMax);
        t.addVertexWithUV(minX, minY, maxZ, uMax, vMax);

        // X+ (East) - Heavy shading (0.6)
        t.setColorOpaque_F(r * 0.6F, g * 0.6F, b * 0.6F);
        t.addVertexWithUV(maxX, minY, maxZ, uMin, vMax);
        t.addVertexWithUV(maxX, minY, minZ, uMax, vMax);
        t.addVertexWithUV(maxX, maxY, minZ, uMax, vMin);
        t.addVertexWithUV(maxX, maxY, maxZ, uMin, vMin);
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

    private int cycleRune = 0;
    private long lastCycle = System.currentTimeMillis();

    @Override
    public void renderItem(ItemRenderType itemRenderType, ItemStack stack, Object... data) {
        String type = ItemChalkStick.getChalkType(stack);
        type = type == null ? RitualRegistry.DEFAULT_CHALK_TYPE_NAME : type;
        if (System.currentTimeMillis() - lastCycle >= 1000) {
            lastCycle = System.currentTimeMillis();
            cycleRune = WhicheryUtils.rand.nextInt(RitualRegistry.CHALK_TYPES.get(type).runeCount);
        }
        Tessellator t = Tessellator.instance;

        boolean stackHasRune = stack.hasTagCompound() && stack.getTagCompound()
            .hasKey("rune");

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        GL11.glRotatef(-90, 1, 0, 0);
        GL11.glTranslatef(-0.5f, 0, -0.5f);
        t.startDrawingQuads();
        render(
            type,
            stackHasRune ? ItemChalkStick.getChalkRune(stack) : cycleRune,
            ItemChalkStick.getChalkRotation(stack),
            0,
            0,
            0,
            true);
        t.draw();
        GL11.glPopMatrix();

    }
}
