package com.supersouper.whichery.client.render;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.supersouper.whichery.CommonProxy;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.blocks.BlockChalkRuneSmall;
import com.supersouper.whichery.common.items.ItemChalkSmall;
import com.supersouper.whichery.common.tileentities.ChalkRuneSmallTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ChalkRuneSmallISBRH implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final ChalkRuneSmallISBRH INSTANCE = new ChalkRuneSmallISBRH();

    private static void render(String[] types, int[] runes, int[] rotations, boolean[] hides, int x, int y, int z,
        boolean noBakedLights) {
        if (types == null) return;

        Tessellator t = Tessellator.instance;

        t.addTranslation(x, y, z);
        for (int i = 0; i < types.length; i++) {
            if (hides[i]) continue;
            if (types[i] == null) continue;
            int color = RitualRegistry.CHALK_TYPES.get(types[i]).drawColor;
            int r = (color >> 16) & 255;
            int g = (color >> 8) & 255;
            int b = color & 255;

            IIcon icon = RitualRegistry.RUNE_ICONS_SMALL.get(types[i])[runes[i]];
            t.addTranslation(
                BlockChalkRuneSmall.positions[i][0] - 0.25f,
                0,
                BlockChalkRuneSmall.positions[i][1] - 0.25f);
            ChalkRuneISBRH.renderIconIn2D(
                t,
                icon,
                8,
                1f / 16f + i / 1000f,
                0.5f,
                45 * rotations[i],
                r,
                g,
                b,
                false,
                noBakedLights);
            t.addTranslation(
                -BlockChalkRuneSmall.positions[i][0] + 0.25f,
                0,
                -BlockChalkRuneSmall.positions[i][1] + 0.25f);
        }
        t.addTranslation(-x, -y, -z);

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        if (!(world.getTileEntity(x, y, z) instanceof ChalkRuneSmallTileEntity cte)) return false;

        render(cte.getTypes(), cte.getRunes(), cte.getRotations(), cte.hides, x, y, z, false);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return CommonProxy.chalkRuneSmallRenderID;
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

    private long lastCycle = System.currentTimeMillis();
    private final int[] cycleRunes = new int[] { 0, 0, 0, 0 };
    private final String[] cycleTypes = new String[4];
    private final int[] zeros = new int[] { 0, 0, 0, 0 };
    private final boolean[] falses = new boolean[] { false, false, false, false };

    private void randomizeCycle() {
        if (System.currentTimeMillis() - lastCycle < 1000) {
            return;
        }
        lastCycle = System.currentTimeMillis();

        for (int i = 0; i < 4; i++) {
            List<String> allTypes = RitualRegistry.getChalkTypeList();
            int j = WhicheryUtils.rand.nextInt(allTypes.size() + 1);
            if (j >= allTypes.size()) {
                cycleTypes[i] = null;
                cycleRunes[i] = 0;
            } else {
                cycleTypes[i] = allTypes.get(j);
                cycleRunes[i] = WhicheryUtils.rand.nextInt(RitualRegistry.CHALK_TYPES.get(cycleTypes[i]).runeCount);
            }
        }
    }

    @Override
    public void renderItem(ItemRenderType renderType, ItemStack stack, Object... data) {
        randomizeCycle();

        String[] types;
        int[] runes;
        int[] rotations = zeros;
        if (stack.getTagCompound() == null) {
            types = cycleTypes;
            runes = cycleRunes;
        } else {
            types = ItemChalkSmall.getChalkTypes(stack);
            runes = ItemChalkSmall.getChalkRunes(stack);
            if (stack.getTagCompound()
                .hasKey("rotations")) {
                rotations = ItemChalkSmall.getChalkRotations(stack);
            }
        }

        Tessellator t = Tessellator.instance;
        if (renderType == ItemRenderType.INVENTORY) {
            GL11.glPushMatrix();
            GL11.glRotatef(-90, 1, 0, 0);
            GL11.glTranslatef(-0.5f, 0, -0.5f);
        }
        t.startDrawingQuads();
        render(types, runes, rotations, falses, 0, 0, 0, true);
        t.draw();
        if (renderType == ItemRenderType.INVENTORY) {
            GL11.glPopMatrix();
        }
    }
}
