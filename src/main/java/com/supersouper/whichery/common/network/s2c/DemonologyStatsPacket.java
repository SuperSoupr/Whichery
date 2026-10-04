package com.supersouper.whichery.common.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.supersouper.whichery.common.entity.extendedproperties.DemonologyProperty;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class DemonologyStatsPacket implements IMessage {

    private boolean canSeeDemonsPossessingHosts;

    public DemonologyStatsPacket() {}

    public DemonologyStatsPacket(DemonologyProperty props) {
        this.canSeeDemonsPossessingHosts = props.isCanSeeDemonsPossessingHosts();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(canSeeDemonsPossessingHosts);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        canSeeDemonsPossessingHosts = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<DemonologyStatsPacket, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(DemonologyStatsPacket message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return null;

            DemonologyProperty props = DemonologyProperty.get(player);
            if (props == null) return null;

            props.setCanSeeDemonsPossessingHosts(message.canSeeDemonsPossessingHosts, player);

            return null;
        }
    }
}
