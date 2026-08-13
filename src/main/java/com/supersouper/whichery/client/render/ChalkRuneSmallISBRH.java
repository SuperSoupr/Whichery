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
import com.supersouper.whichery.common.tileentities.ChalkSmallTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ChalkRuneSmallISBRH implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final ChalkRuneSmallISBRH INSTANCE = new ChalkRuneSmallISBRH();

    private static final String[] cycleTypes = new String[4];
    private static long lastCycle = 0;

    private static void randomizeCycleTypes() {
        if (System.currentTimeMillis() - lastCycle < 1000) {
            return;
        }
        lastCycle = System.currentTimeMillis();

        for (int i = 0; i < 4; i++) {
            List<String> allTypes = RitualRegistry.getChalkTypeList();
            int j = WhicheryUtils.rand.nextInt(allTypes.size() + 1);
            if (j >= allTypes.size()) {
                cycleTypes[i] = null;
            } else {
                cycleTypes[i] = allTypes.get(j);
            }
        }
    }

    private static void render(String[] types, int[] runes, int[] rotations, int x, int y, int z) {
        if (types == null) return;

        Tessellator t = Tessellator.instance;

        t.addTranslation(x, y, z);
        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) continue;
            int color = RitualRegistry.CHALK_TYPES.get(types[i]);
            int r = (color >> 16) & 255;
            int g = (color >> 8) & 255;
            int b = color & 255;

            IIcon icon = RitualRegistry.RUNE_ICONS.get(types[i])[runes[i]];
            t.addTranslation(
                BlockChalkRuneSmall.positions[i][0] - 0.25f,
                0,
                BlockChalkRuneSmall.positions[i][1] - 0.25f);
            ChalkRuneISBRH.renderIconIn2D(t, icon, 1f / 16f + i / 1000f, 0.5f, 45 * rotations[i], r, g, b);
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
        if (!(world.getTileEntity(x, y, z) instanceof ChalkSmallTileEntity cte)) return false;

        render(cte.getTypes(), cte.getRunes(), cte.getRotations(), x, y, z);
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

    private static final int[] zeros = new int[] { 0, 0, 0, 0 };

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
        // render(types, zeros, zeros, 0, 0, 0);
        GL11.glPopMatrix();
    }
}
