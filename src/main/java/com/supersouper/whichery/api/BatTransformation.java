package com.supersouper.whichery.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.common.entity.extendedproperties.TransformationProperty;
import com.supersouper.whichery.common.entity.extendedproperties.VampirismProperty;

public class BatTransformation implements ITransformation {

    @Override
    public String getId() {
        return Whichery.MODID + "bat";
    }

    @Override
    public float getWidth() {
        return 0.5F;
    }

    @Override
    public float getHeight() {
        return 0.9F;
    }

    @Override
    public float getEyeHeight() {
        return 0.81F;
    }

    @Override
    public EntityLivingBase getRenderEntity(EntityPlayer player) {
        EntityBat bat = new EntityBat(player.worldObj);
        bat.setIsBatHanging(false);
        return bat;
    }

    @Override
    public void onTick(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            VampirismProperty props = VampirismProperty.get(player);
            if (props == null || !props.isVampire()
                || (player.ticksExisted % 20 == 0 && !props.drainBlood(20, player))) {

                TransformationProperty tProps = TransformationProperty.get(player);
                if (tProps != null) tProps.removeTransformation();
                return;
            }
        }

        player.capabilities.isFlying = true;
        player.fallDistance = 0;
    }

    @Override
    public void onUntransform(EntityPlayer player) {
        player.capabilities.isFlying = false;
    }
}
