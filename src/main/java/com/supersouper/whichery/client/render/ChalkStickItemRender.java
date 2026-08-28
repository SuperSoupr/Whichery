package com.supersouper.whichery.client.render;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.items.ItemChalkStick;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChalkStickItemRender implements IItemRenderer {

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return switch (helper) {
            case ENTITY_ROTATION, ENTITY_BOBBING -> true;
            default -> false;
        };
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (!(stack.getItem() instanceof ItemChalkStick chalkstick)) return;
        int iconIndex = chalkstick.getIconIndexFromDamage(stack);
        IIcon icon = chalkstick.getIconFromIndex(stack, iconIndex);

        ItemRenderUtil.applyStandardItemTransform(type);

        int color = RitualRegistry.CHALK_TYPES.get(ItemChalkStick.getChalkType(stack))
            .getRGB(2, iconIndex);
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;

        GL11.glColor3f(r / 255f, g / 255f, b / 255f);
        GL11.glEnable(GL11.GL_BLEND);
        ItemRenderUtil.renderItem(type, icon);
    }
}
