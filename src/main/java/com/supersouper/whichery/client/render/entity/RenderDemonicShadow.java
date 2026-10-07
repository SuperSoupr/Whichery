package com.supersouper.whichery.client.render.entity;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.common.entity.demon.EntityDemonicShadow;

public class RenderDemonicShadow extends Render {

    private static final ResourceLocation smokeTexture = new ResourceLocation(
        Whichery.MODID,
        "textures/models/entity/demon/demonic_shadow.png");

    public RenderDemonicShadow() {
        this.shadowSize = 0;
    }

    public void doRender(EntityDemonicShadow demon, double x, double y, double z, float yaw, float partialTicks) {
        float shrinkFactor = demon.getShrinkFactor();

        if (shrinkFactor >= 1) {
            // A fully shrunken demon is not rendered at all
            return;
        }

        float inverseShrinkFactor = 1 - shrinkFactor;

        this.bindTexture(getEntityTexture(demon));

        // Linear interpolation for extra smooth shadow look
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        GL11.glPushMatrix();

        GL11.glTranslated(x, y + demon.height * 0.5, z);

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);

        Tessellator tessellator = Tessellator.instance;
        double renderTime = (double) demon.ticksExisted + partialTicks;
        int layers = 6;

        for (int layer = 0; layer < layers; layer++) {
            GL11.glPushMatrix();

            double wobbleSpeed = 0.125;
            double rotationSpeed = 1.6;

            double baseWobbleX = Math.sin(renderTime * wobbleSpeed + (layer * 18));
            double baseWobbleY = Math.cos(renderTime * (wobbleSpeed * 0.8) + (layer * 28));
            double baseWobbleZ = Math.sin(renderTime * (wobbleSpeed * 1.3) + (layer * 38));

            double fastNoiseX = 0.35 * Math.sin(renderTime * (wobbleSpeed * 2.5) + (layer * 45));
            double fastNoiseY = 0.35 * Math.cos(renderTime * (wobbleSpeed * 2.1) + (layer * 55));
            double fastNoiseZ = 0.35 * Math.sin(renderTime * (wobbleSpeed * 2.8) + (layer * 65));

            double offsetX = (baseWobbleX + fastNoiseX) * 0.18 * inverseShrinkFactor;
            double offsetY = (baseWobbleY + fastNoiseY) * 0.32 * inverseShrinkFactor;
            double offsetZ = (baseWobbleZ + fastNoiseZ) * 0.18 * inverseShrinkFactor;

            // "Wobble" effect
            GL11.glTranslated(offsetX, offsetY, offsetZ);

            // Camera billboard effect (always point to the player)
            GL11.glRotatef(-this.renderManager.playerViewY, 0, 1, 0);
            GL11.glRotatef(this.renderManager.playerViewX, 1, 0, 0);

            // Rotate a bit for more life-like movement
            GL11.glRotated((renderTime * rotationSpeed + (layer * 65)) % 360, 0, 0, 1);

            // "Pulsating" effect
            float pulseW = 1.1F + (float) Math.sin(renderTime * 0.225 + layer) * 0.18F * inverseShrinkFactor;
            float pulseH = 1.5F + (float) Math.cos(renderTime * 0.16 + (layer * 2)) * 0.25F * inverseShrinkFactor;
            GL11.glScalef(pulseW, pulseH, pulseW);

            // factor 0.98f to prevent blending bugs
            GL11.glColor4f(0, 0, 0, 0.98f * inverseShrinkFactor);

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-0.5, -0.5, 0, 0, 1);
            tessellator.addVertexWithUV(0.5, -0.5, 0, 1, 1);
            tessellator.addVertexWithUV(0.5, 0.5, 0, 1, 0);
            tessellator.addVertexWithUV(-0.5, 0.5, 0, 0, 0);
            tessellator.draw();

            GL11.glPopMatrix();
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        doRender((EntityDemonicShadow) entity, x, y, z, yaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return smokeTexture;
    }
}
