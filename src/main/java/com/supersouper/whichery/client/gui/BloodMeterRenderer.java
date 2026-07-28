package com.supersouper.whichery.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import com.supersouper.whichery.common.entity.extendedproperties.VampirismProperty;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BloodMeterRenderer extends Gui {

    private static final ResourceLocation BLOOD_METER = new ResourceLocation(
        "whichery",
        "textures/gui/blood_icons.png");

    private final Minecraft mc;

    public BloodMeterRenderer(Minecraft mc) {
        this.mc = mc;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.FOOD) return;

        EntityPlayer player = mc.thePlayer;
        VampirismProperty props = VampirismProperty.get(player);
        if (props == null || !props.isVampire()) return;

        int screenWidth = event.resolution.getScaledWidth();
        int screenHeight = event.resolution.getScaledHeight();

        int offsetY = -49;
        // TODO: Configurable offset
        if (player.getAir() < 300) offsetY -= 10;
        renderBloodMeter((screenWidth / 2) + 10, screenHeight + offsetY, props.getBlood(), props.getMaxBlood());
    }

    private void renderBloodMeter(int xBase, int yBase, int blood, int maxBlood) {

        int previousTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

        mc.getTextureManager()
            .bindTexture(BLOOD_METER);
        GL11.glEnable(GL11.GL_BLEND);

        int numIcons = 10;
        int bloodPerIcon = maxBlood / numIcons;

        for (int i = 0; i < numIcons; i++) {
            int x = xBase + (9 - i) * 8;
            int y = yBase;

            drawTexturedModalRect(x, y, 16, 0, 9, 9);
            func_152125_a(x, y, 16.0F, 0.0F, 9, 9, 9, 9, 25.0F, 9.0F);
            int iconBlood = blood - (i * bloodPerIcon);

            if (iconBlood >= bloodPerIcon) {
                func_152125_a(x, y, 0.0F, 0.0F, 9, 9, 9, 9, 25.0F, 9.0F);
            } else if (iconBlood > 0) {
                func_152125_a(x, y, 8.0F, 0.0F, 9, 9, 9, 9, 25.0F, 9.0F);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, previousTex);
    }
}
