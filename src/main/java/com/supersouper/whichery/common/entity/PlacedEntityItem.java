package com.supersouper.whichery.common.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PlacedEntityItem extends EntityItem {

    public PlacedEntityItem(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public PlacedEntityItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }

    public PlacedEntityItem(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        ItemStack stack = this.getDataWatcher()
            .getWatchableObjectItemStack(10);
        if (stack != null && stack.getItem() != null) {
            if (stack.getItem()
                .onEntityItemUpdate(this)) {
                return;
            }
        }

        if (this.getEntityItem() == null) {
            this.setDead();
        } else {
            onEntityUpdate();

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            this.noClip = this
                .func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);

            float f = 0.98F;

            if (this.onGround) {
                f = this.worldObj.getBlock(
                    MathHelper.floor_double(this.posX),
                    MathHelper.floor_double(this.boundingBox.minY) - 1,
                    MathHelper.floor_double(this.posZ)).slipperiness * 0.98F;
            }

            this.motionX *= (double) f;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= (double) f;

            if (this.onGround) {
                this.motionY *= -0.5D;
            }

            this.age++;
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {

    }
}
