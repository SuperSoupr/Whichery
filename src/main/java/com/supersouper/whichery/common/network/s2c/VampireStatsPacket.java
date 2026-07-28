package com.supersouper.whichery.common.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.supersouper.whichery.common.entity.extendedproperties.VampirismProperty;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class VampireStatsPacket implements IMessage {

    private boolean isVampire;
    private int vampireLevel;
    private int blood;
    private int maxBlood;

    public VampireStatsPacket() {}

    public VampireStatsPacket(VampirismProperty props) {
        this.isVampire = props.isVampire();
        this.vampireLevel = props.getVampireLevel();
        this.blood = props.getBlood();
        this.maxBlood = props.getMaxBlood();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isVampire);
        buf.writeInt(vampireLevel);
        buf.writeInt(blood);
        buf.writeInt(maxBlood);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isVampire = buf.readBoolean();
        vampireLevel = buf.readInt();
        blood = buf.readInt();
        maxBlood = buf.readInt();
    }

    public static class Handler implements IMessageHandler<VampireStatsPacket, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(VampireStatsPacket message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return null;

            VampirismProperty props = VampirismProperty.get(player);
            if (props == null) return null;

            props.setIsVampire(message.isVampire, player);
            props.setVampireLevel(message.vampireLevel, player);
            props.setMaxBlood(message.maxBlood, player);
            props.setBlood(message.blood, player);

            return null;
        }
    }
}
