package com.supersouper.whichery.common.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.supersouper.whichery.api.ITransformation;
import com.supersouper.whichery.api.TransformationRegistry;
import com.supersouper.whichery.common.entity.extendedproperties.TransformationProperty;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class TransformationPacket implements IMessage {

    private int entityId;
    private String transformId;

    public TransformationPacket() {}

    public TransformationPacket(int entityId, String transformId) {
        this.entityId = entityId;
        this.transformId = transformId == null ? "" : transformId;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, transformId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        transformId = ByteBufUtils.readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<TransformationPacket, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(TransformationPacket message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().theWorld;
            if (world == null) return null;

            Entity target = world.getEntityByID(message.entityId);

            if (target instanceof EntityPlayer player) {
                TransformationProperty data = TransformationProperty.get(player);

                if (data != null) {
                    if (message.transformId.isEmpty()) {
                        data.removeTransformation();
                    } else {
                        ITransformation transform = TransformationRegistry.getTransformation(message.transformId);
                        data.setTransformation(transform);
                    }
                }
            }
            return null;
        }
    }
}
