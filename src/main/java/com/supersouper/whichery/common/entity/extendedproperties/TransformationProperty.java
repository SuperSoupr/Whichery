package com.supersouper.whichery.common.entity.extendedproperties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.supersouper.whichery.api.ITransformation;
import com.supersouper.whichery.api.TransformationRegistry;
import com.supersouper.whichery.common.network.PacketHandler;
import com.supersouper.whichery.common.network.s2c.TransformationPacket;
import com.supersouper.whichery.mixins.early.minecraft.EntityAccessor;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TransformationProperty implements IExtendedEntityProperties {

    public static final String KEY = "TransformationProperty";

    private final EntityPlayer player;
    private ITransformation currentTransformation;

    @SideOnly(Side.CLIENT)
    private EntityLivingBase renderEntity;

    public TransformationProperty(EntityPlayer player) {
        this.player = player;
    }

    public static TransformationProperty get(EntityPlayer player) {
        return (TransformationProperty) player.getExtendedProperties(KEY);
    }

    public ITransformation getTransformation() {
        return currentTransformation;
    }

    @SideOnly(Side.CLIENT)
    public EntityLivingBase getRenderEntity() {
        if (currentTransformation != null) {
            if (renderEntity == null) {
                renderEntity = currentTransformation.getRenderEntity(player);
            }
            return renderEntity;
        }
        return null;
    }

    public void removeTransformation() {
        setTransformation(null);
    }

    public void setTransformation(ITransformation transformation) {
        if (this.currentTransformation == transformation) return;

        if (this.currentTransformation != null) {
            this.currentTransformation.onUntransform(this.player);
        }

        this.currentTransformation = transformation;

        if (player.worldObj.isRemote) {
            this.renderEntity = null;
        }

        if (this.currentTransformation != null) {
            this.currentTransformation.onTransform(this.player);
            updatePlayerSize(this.currentTransformation.getWidth(), this.currentTransformation.getHeight());
        } else {
            updatePlayerSize(0.6F, 1.8F);
        }

        syncPlayerTransformation(player);
    }

    public void updatePlayerSize(float width, float height) {
        if (player.width != width || player.height != height) {
            ((EntityAccessor) player).invokeSetSize(width, height);

            float halfWidth = width / 2.0F;
            player.boundingBox.minX = player.posX - halfWidth;
            player.boundingBox.maxX = player.posX + halfWidth;
            player.boundingBox.minZ = player.posZ - halfWidth;
            player.boundingBox.maxZ = player.posZ + halfWidth;

            player.boundingBox.maxY = player.boundingBox.minY + height;
        }
    }

    public static void syncPlayerTransformation(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            TransformationProperty props = get(player);
            if (props == null) return;

            String id = props.currentTransformation != null ? props.currentTransformation.getId() : "";
            TransformationPacket packet = new TransformationPacket(player.getEntityId(), id);

            NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
                player.dimension,
                player.posX,
                player.posY,
                player.posZ,
                256.0D);
            PacketHandler.INSTANCE.sendToAllAround(packet, point);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound tag = new NBTTagCompound();
        if (currentTransformation != null) {
            tag.setString("TransformID", currentTransformation.getId());
        }
        compound.setTag(KEY, tag);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound tag = compound.getCompoundTag(KEY);
        if (tag.hasKey("TransformID")) {
            currentTransformation = TransformationRegistry.getTransformation(tag.getString("TransformID"));
        }
    }

    @Override
    public void init(Entity entity, World world) {}
}
