package com.supersouper.whichery.common.event;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.api.rituals.RitualAnimation;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

@EventBusSubscriber(side = Side.CLIENT)
public class RitualEvents {

    public static ArrayList<RitualAnimation> animations = new ArrayList<>();

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase viewEntity = mc.renderViewEntity != null ? mc.renderViewEntity : mc.thePlayer;
        double playerX = viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * event.partialTicks;
        double playerY = viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * event.partialTicks;
        double playerZ = viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * event.partialTicks;
        GL11.glTranslated(-playerX, -playerY, -playerZ);
        for (RitualAnimation animation : animations) {
            GL11.glTranslatef(animation.leader.xCoord, animation.leader.yCoord, animation.leader.zCoord);
            animation.render(event);
            GL11.glTranslatef(-animation.leader.xCoord, -animation.leader.yCoord, -animation.leader.zCoord);
        }
        GL11.glTranslated(playerX, playerY, playerZ);
    }
}
