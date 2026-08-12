package com.supersouper.whichery.client.render;

import java.util.List;
import java.util.Random;

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

import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.blocks.BlockChalkRuneSmall;
import com.supersouper.whichery.common.tileentities.ChalkSmallTileEntity;

public class ChalkRuneSmallTESR extends TileEntitySpecialRenderer implements IItemRenderer {

    private static final Random rand = new Random();
    private static final String[] cycleTypes = new String[4];
    private static long lastCycle = 0;

    private static void randomizeCycleTypes() {
        if (System.currentTimeMillis() - lastCycle < 1000) {
            return;
        }
        lastCycle = System.currentTimeMillis();

        for (int i = 0; i < 4; i++) {
            List<String> allTypes = RitualRegistry.getChalkTypeList();
            int j = rand.nextInt(allTypes.size() + 1);
            if (j >= allTypes.size()) {
                cycleTypes[i] = null;
            } else {
                cycleTypes[i] = allTypes.get(j);
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        ChalkSmallTileEntity cte = (ChalkSmallTileEntity) tileEntity;
        render(cte, cte.getTypes(), x, y, z, partialTicks);
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
        randomizeCycleTypes();

        String[] types;
        if (item.getTagCompound() == null) {
            types = cycleTypes;
        } else {
            types = BlockChalkRuneSmall.getChalkTypes(item);
        }

        GL11.glPushMatrix();
        GL11.glRotatef(-90, 1, 0, 0);
        GL11.glTranslatef(-0.5f, 0, -0.5f);
        render(null, types, 0, 0, 0, 0);
        GL11.glPopMatrix();
    }

    private static void render(ChalkSmallTileEntity tileEntity, String[] types, double x, double y, double z,
        float partialTicks) {
        if (types == null) return;
        Tessellator t = Tessellator.instance;
        Minecraft mc = Minecraft.getMinecraft();
        int previousTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        IIcon icon = Blocks.stained_hardened_clay.getIcon(0, 0);
        // TODO custom chalk textures for each type
        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) continue;
            icon = RitualRegistry.RUNE_ICONS.get(types[i])[0];

            drawSmallRune(
                types[i],
                icon,
                x + BlockChalkRuneSmall.positions[i][0],
                y,
                z + BlockChalkRuneSmall.positions[i][1],
                partialTicks);
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, previousTex);
    }

    public static void drawSmallRune(String type, IIcon icon, double x, double y, double z, float partialTicks) {
        Tessellator t = Tessellator.instance;

        int color = RitualRegistry.CHALK_TYPES.get(type);
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        GL11.glColor3f(r / 255f, g / 255f, b / 255f);
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        GL11.glRotatef(90, 1, 0, 0);
        GL11.glScalef(0.5f, 0.5f, 1f);
        ItemRenderer.renderItemIn2D(
            t,
            icon.getMinU(),
            icon.getMinV(),
            icon.getMaxU(),
            icon.getMaxV(),
            icon.getIconWidth(),
            icon.getIconHeight(),
            0.5f / 16f);
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }
}
