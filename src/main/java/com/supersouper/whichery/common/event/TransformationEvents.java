package com.supersouper.whichery.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.supersouper.whichery.api.ITransformation;
import com.supersouper.whichery.common.entity.extendedproperties.TransformationProperty;
import com.supersouper.whichery.common.network.PacketHandler;
import com.supersouper.whichery.common.network.s2c.TransformationPacket;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@EventBusSubscriber
public class TransformationEvents {

    public static float renderYOffset = 0.0F;
    private static float previousEyeHeight = 0.0F;
    private static boolean isShifted = false;

    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer player) {
            player.registerExtendedProperties(TransformationProperty.KEY, new TransformationProperty(player));
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.wasDeath) return;
        TransformationProperty original = TransformationProperty.get(event.original);
        TransformationProperty clone = TransformationProperty.get(event.entityPlayer);
        if (original != null && clone != null && original.getTransformation() != null) {
            clone.setTransformation(original.getTransformation());
        }
    }

    // When a transformed player joins, send their transform packet to their own client
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.world.isRemote && event.entity instanceof EntityPlayerMP player) {
            TransformationProperty props = TransformationProperty.get(player);
            if (props != null && props.getTransformation() != null) {
                TransformationPacket packet = new TransformationPacket(
                    player.getEntityId(),
                    props.getTransformation()
                        .getId());
                PacketHandler.INSTANCE.sendTo(packet, player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            TransformationProperty data = TransformationProperty.get(player);

            if (data != null && data.getTransformation() != null) {
                ITransformation transform = data.getTransformation();
                transform.onTick(player);
                data.updatePlayerSize(transform.getWidth(), transform.getHeight());

                float eyeHeight = transform.getEyeHeight();
                if (player.isSneaking()) eyeHeight -= 0.08F;
                if (player.isPlayerSleeping()) eyeHeight = 0.2F;
                player.eyeHeight = eyeHeight - player.yOffset;

            } else if (data != null && data.getTransformation() == null) {
                data.updatePlayerSize(0.6F, 1.8F);
                player.eyeHeight = player.getDefaultEyeHeight();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;

        if (player != null && mc.theWorld != null) {
            if (event.phase == TickEvent.Phase.START) {
                // Changes needed for proper rendering
                TransformationProperty props = TransformationProperty.get(player);
                if (props != null && props.getTransformation() != null) {
                    float targetEyeHeight = props.getTransformation()
                        .getEyeHeight();
                    if (player.isSneaking()) targetEyeHeight -= 0.08F;
                    if (player.isPlayerSleeping()) targetEyeHeight = 0.2F;

                    float defaultEyeHeight = player.getDefaultEyeHeight();

                    renderYOffset = player.yOffset - targetEyeHeight + defaultEyeHeight;

                    previousEyeHeight = player.eyeHeight;
                    player.eyeHeight = defaultEyeHeight;

                    player.lastTickPosY -= renderYOffset;
                    player.prevPosY -= renderYOffset;
                    player.posY -= renderYOffset;
                    isShifted = true;
                }
            } else if (event.phase == TickEvent.Phase.END) {
                // Restore everything so physics don't explode
                if (isShifted) {
                    player.lastTickPosY += renderYOffset;
                    player.prevPosY += renderYOffset;
                    player.posY += renderYOffset;
                    player.eyeHeight = previousEyeHeight;
                    renderYOffset = 0.0F;
                    isShifted = false;
                }
            }
        }
    }

    // TODO probably add something that lets transforms define hand rendering instead of just canceling it
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            TransformationProperty data = TransformationProperty.get(player);
            if (data != null && data.getTransformation() != null) event.setCanceled(true);
        }
    }

    // When a player enters range of a transformed player, send that player's transform packet to them
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.target instanceof EntityPlayer targetPlayer) {
            TransformationProperty props = TransformationProperty.get(targetPlayer);
            if (props != null && props.getTransformation() != null) {
                TransformationPacket packet = new TransformationPacket(
                    targetPlayer.getEntityId(),
                    props.getTransformation()
                        .getId());
                PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) event.entityPlayer);
            }
        }
    }
}
