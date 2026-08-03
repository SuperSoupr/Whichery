package com.supersouper.whichery.common.tileentities;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.supersouper.whichery.api.rituals.IRitualParticipator;
import com.supersouper.whichery.api.rituals.RitualLeaderTileEntity;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.entity.PlacedEntityItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChalkTileEntity extends RitualLeaderTileEntity implements IRitualParticipator, IInventory {

    private String type = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;
    @SideOnly(Side.CLIENT)
    private PlacedEntityItem placedEntityItem;
    private ItemStack stack;

    public ChalkTileEntity() {

    }

    public ChalkTileEntity(World world) {
        setWorldObj(world);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean onRightClicked(EntityPlayer player) {
        if (stack == null) {
            ItemStack held = player.getHeldItem();
            if (held != null) {
                if (isItemValidForSlot(0, held)) {
                    setInventorySlotContents(0, held);
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    return true;
                }
            }
        } else {
            dropHeldItem();
            return true;
        }
        return false;
    }

    private void dropHeldItem() {
        if (stack != null) {
            if (!worldObj.isRemote) {
                EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.6, zCoord + 0.5, stack);
                worldObj.spawnEntityInWorld(entityItem);
                entityItem.delayBeforeCanPickup = 5;
                stack = null;
            } else {
                stack = null;
                updateDisplayItem();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateDisplayItem() {
        if (placedEntityItem != null) {
            if (stack == null) {
                placedEntityItem.setDead();
                placedEntityItem = null;
            } else {
                placedEntityItem.setEntityItemStack(stack);
            }
        } else {
            if (stack != null) {
                placedEntityItem = new PlacedEntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, stack);
                worldObj.spawnEntityInWorld(placedEntityItem);
            }
        }
    }

    public void invalidate() {
        if (worldObj.isRemote && placedEntityItem != null) {
            placedEntityItem.setDead();
            placedEntityItem = null;
        }
        dropHeldItem();
        super.invalidate();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        type = compound.getString("type");
        if (compound.hasKey("stack")) {
            stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("stack"));
        } else {
            stack = null;
        }
        if (worldObj != null && worldObj.isRemote) {
            updateDisplayItem();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("type", type);
        if (stack != null) {
            compound.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void ritualTick() {
        if (worldObj.isRemote) {
            worldObj.spawnEntityInWorld(new EntityLightningBolt(worldObj, xCoord, yCoord, zCoord));
        }
    }

    @Override
    public void transitionToStage(int stage) {

    }

    @Override
    public void markDirty() {
        if (worldObj.isRemote) {
            updateDisplayItem();
        }
        super.markDirty();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (stack == null) return null;

        ItemStack tmp;
        if (stack.stackSize <= count) {
            tmp = stack;
            stack = null;
        } else {
            tmp = stack.splitStack(count);

            if (stack.stackSize == 0) {
                stack = null;
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
        return tmp;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stack = stack;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "container.whichery.chalk";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }
}
