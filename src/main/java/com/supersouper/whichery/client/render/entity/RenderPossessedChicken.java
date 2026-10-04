package com.supersouper.whichery.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.common.entity.demon.EntityPossessedChicken;
import com.supersouper.whichery.common.entity.extendedproperties.DemonologyProperty;

public class RenderPossessedChicken extends RenderChicken {

    private static final ResourceLocation POSSESSED_CHICKEN_TEXTURE_EYES = new ResourceLocation(
        Whichery.MODID,
        "textures/models/entity/demon/possessed_chicken_eyes.png");

    private static final ResourceLocation DEMON_SHADOW_TEXTURE = new ResourceLocation(
        Whichery.MODID,
        "textures/models/entity/demon/demon_tier0_silhouette.png");

    public RenderPossessedChicken() {
        super(new ModelChicken(), 0.3f);
        this.setRenderPassModel(this.mainModel);
    }

    private boolean shouldRenderYellowEyes(EntityPossessedChicken entity, float partialTicks) {
        return canPlayerSeeDemons() || isNightAndDidLightningBoltHitRecently(entity, partialTicks);
    }

    private boolean shouldRenderShadowSilhouette(EntityPossessedChicken entity, float partialTicks) {
        return canPlayerSeeDemons() && entity.isStaringAtAwarePlayer()
            && isNightAndDidLightningBoltHitRecently(entity, partialTicks);
    }

    private boolean isNightAndDidLightningBoltHitRecently(EntityPossessedChicken entity, float partialTicks) {
        World world = entity.worldObj;
        float celestialAngle = world.getCelestialAngle(partialTicks);
        boolean isNight = (celestialAngle > 0.25F && celestialAngle < 0.75F);

        return isNight && world.lastLightningBolt > 0;
    }

    private boolean canPlayerSeeDemons() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        // Might be null in edge cases, e.g. rendering in main menu
        if (player == null) {
            return false;
        }

        DemonologyProperty playerDemonologyProperties = DemonologyProperty.get(player);

        return playerDemonologyProperties != null && playerDemonologyProperties.isCanSeeDemonsPossessingHosts();
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float partialTicks) {
        EntityPossessedChicken chicken = (EntityPossessedChicken) entity;

        if (pass == 1) {
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(0, 0);
        }

        if (pass != 0 || !shouldRenderYellowEyes(chicken, partialTicks)) {
            return -1;
        } else {
            this.bindTexture(POSSESSED_CHICKEN_TEXTURE_EYES);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            GL11.glDepthMask(!entity.isInvisible());

            // prevent z-fighting
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(-3, -3);

            adjustBrightnessToSurroundings(chicken, partialTicks);

            return 1;
        }
    }

    @Override
    protected int inheritRenderPass(EntityLivingBase entity, int pass, float partialTicks) {
        /*
         * The super implementation calls shouldRenderPass() again, but we need no logic here for render passes.
         * Otherwise, the hurt animation for this layer (which we don't need anyway) would use the same brightness
         * calculation as the eyes,
         * and thus render the chicken with higher brightness when hurt/dying.
         */
        return -1;
    }

    /*
     * Make the eyes glow; don't use a fixed brightness as that does not look good in all circumstances (moody vs
     * full-bright, fog, thunder, rain, daylight),as the default chicken texture is white,
     * and the eye is only one pixel, which limits our abilities to add contrast.
     */
    private void adjustBrightnessToSurroundings(EntityPossessedChicken chicken, float partialTicks) {
        int currentMobBrightness = chicken.getBrightnessForRender(partialTicks);
        int blockLight = currentMobBrightness % 65536;
        int skyLight = currentMobBrightness / 65536;

        // Make it brighter than the surroundings so it glows nicely in contrast to them, but not too bright
        blockLight = Math.min(240, blockLight + 60);
        skyLight = Math.min(240, skyLight + 60);

        // At low light levels require at least a minimum brightness, as the glow should be very visible then
        if (blockLight < 195) blockLight = 195;
        if (skyLight < 195) skyLight = 195;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) blockLight, (float) skyLight);
        GL11.glColor4f(1, 1, 1, 1);
    }

    @Override
    public void doRender(EntityChicken entity, double x, double y, double z, float yaw, float partialTicks) {

        /*
         * Render the shadow silhouette in global pass 1, so it displays correctly with e.g. water behind it.
         * This is not the same as the render passes in shouldRenderPass(...) etc. - those are called in a loop in this
         * renderer.
         */
        int renderPass = MinecraftForgeClient.getRenderPass();

        if (renderPass == 1 && shouldRenderShadowSilhouette((EntityPossessedChicken) entity, partialTicks)) {
            renderDemonicShadowSilhouette(entity, x, y, z);
        }

        if (renderPass == 0) {
            super.doRender(entity, x, y, z, yaw, partialTicks);
        }
    }

    private void renderDemonicShadowSilhouette(EntityChicken chicken, double x, double y, double z) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player == null) {
            return;
        }

        Vec3 playerToChickenVec = Vec3
            .createVectorHelper(chicken.posX - player.posX, chicken.posY - player.posY, chicken.posZ - player.posZ)
            .normalize();

        float renderYaw = this.renderManager.playerViewY;
        float renderPitch = this.renderManager.playerViewX;

        // Don't angle the silhouette if the pitch is small
        if (renderPitch > 0) {
            renderPitch = Math.max(0, renderPitch - 5);
        } else if (renderPitch < 0) {
            renderPitch = Math.min(0, renderPitch + 5);
        }

        float clampedPitch = Math.max(-30, Math.min(30, renderPitch));

        float scaleFactor = 2.5f;

        GL11.glPushMatrix();

        // We move and rotate the shadow such that it always faces the player ("billboarding") and is a bit behind the
        // chicken
        GL11.glTranslated(
            x + playerToChickenVec.xCoord / 2,
            y + playerToChickenVec.yCoord / 2,
            z + playerToChickenVec.zCoord / 2);
        GL11.glRotatef(180 - renderYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-clampedPitch, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

        this.bindTexture(DEMON_SHADOW_TEXTURE);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.85F);

        double width = 0.6875;
        double halfWidth = width / 2;
        double height = 1.0;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setNormal(0, 0, 1);
        tessellator.addVertexWithUV(-halfWidth, 0, 0, 0, 1);
        tessellator.addVertexWithUV(halfWidth, 0, 0, 1, 1);
        tessellator.addVertexWithUV(halfWidth, height, 0, 1, 0);
        tessellator.addVertexWithUV(-halfWidth, height, 0, 0, 0);

        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        /*
         * This method is invoked in both pass 0 and 1. We only want to render it in pass 0 (otherwise the shadow is
         * rendered twice and appears darker).
         */
        if (MinecraftForgeClient.getRenderPass() == 0) {
            super.doRenderShadowAndFire(entity, x, y, z, yaw, partialTicks);
        }
    }
}
