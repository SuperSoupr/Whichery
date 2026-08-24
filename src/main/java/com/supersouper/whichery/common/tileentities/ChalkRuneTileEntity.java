package com.supersouper.whichery.common.tileentities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.supersouper.whichery.ModItems;
import com.supersouper.whichery.api.rituals.RitualLeaderTileEntity;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.common.entity.PlacedEntityItem;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChalkRuneTileEntity extends RitualLeaderTileEntity implements IInventory {

    private String type = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;
    private int rune = 0;
    private int rotation = 0;
    public boolean hasStorageUpgrade = false;
    @SideOnly(Side.CLIENT)
    private PlacedEntityItem placedEntityItem;
    // 0 is result stack, 1 is input stack
    private final ItemStack[] stacks = new ItemStack[2];

    public ChalkRuneTileEntity() {

    }

    public ChalkRuneTileEntity(World world) {
        setWorldObj(world);
    }

    public void setType(String type) {
        markDirty();
        this.type = type;
    }

    public String getType() {
        markDirty();
        return type;
    }

    public void setRotation(int rotation) {
        markDirty();
        this.rotation = rotation;
    }

    public int getRotation() {
        markDirty();
        return rotation;
    }

    public void setRune(int rune) {
        markDirty();
        this.rune = rune;
    }

    public int getRune() {
        markDirty();
        return rune;
    }

    public boolean onRightClicked(EntityPlayer player) {
        if (stacks[1] == null) {
            ItemStack held = player.getHeldItem();
            if (held != null) {
                if (held.getItem() == ModItems.CHALK_STORAGE_UPGRADE.get() && !hasStorageUpgrade) {
                    hasStorageUpgrade = true;
                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    held.stackSize--;
                    if (held.stackSize == 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                    return true;
                }
                setInventorySlotContents(1, held);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                return true;
            } else {
                if (hasStorageUpgrade && player.isSneaking()) {
                    if (!player.worldObj.isRemote) {
                        EntityItem entityItem = new EntityItem(
                            worldObj,
                            xCoord + 0.5,
                            yCoord + 0.5,
                            zCoord + 0.5,
                            ModItems.CHALK_STORAGE_UPGRADE.newItemStack());
                        worldObj.spawnEntityInWorld(entityItem);
                        entityItem.delayBeforeCanPickup = 5;
                    }
                    hasStorageUpgrade = false;
                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
        if (stacks[1] != null) {
            if (!worldObj.isRemote) {
                EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.6, zCoord + 0.5, stacks[1]);
                worldObj.spawnEntityInWorld(entityItem);
                entityItem.delayBeforeCanPickup = 5;
                stacks[1] = null;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            } else {
                stacks[1] = null;
                updateDisplayItem();
            }
            markDirty();
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateDisplayItem() {
        if (placedEntityItem != null) {
            if (stacks[1] == null) {
                placedEntityItem.setDead();
                placedEntityItem = null;
            } else {
                placedEntityItem.setEntityItemStack(stacks[1]);
            }
        } else {
            if (stacks[1] != null) {
                placedEntityItem = new PlacedEntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, stacks[1]);
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
        if (!worldObj.isRemote) {
            if (stacks[0] != null) {
                EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.6, zCoord + 0.5, stacks[0]);
                worldObj.spawnEntityInWorld(entityItem);
                entityItem.delayBeforeCanPickup = 5;
            }
            if (hasStorageUpgrade) {
                EntityItem entityItem = new EntityItem(
                    worldObj,
                    xCoord + 0.5,
                    yCoord + 0.5,
                    zCoord + 0.5,
                    ModItems.CHALK_STORAGE_UPGRADE.newItemStack());
                worldObj.spawnEntityInWorld(entityItem);
                entityItem.delayBeforeCanPickup = 5;
            }
        }
        super.invalidate();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        type = tag.getString("type");
        if (!RitualRegistry.chalkExists(type)) {
            type = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;
        }
        if (tag.hasKey("stack0")) {
            stacks[0] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack0"));
        } else {
            stacks[0] = null;
        }
        if (tag.hasKey("stack1")) {
            stacks[1] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack1"));
        } else {
            stacks[1] = null;
        }
        rune = tag.getByte("rune");
        rotation = tag.getByte("rotation");
        if (worldObj != null && worldObj.isRemote) {
            updateDisplayItem();
        }
        hasStorageUpgrade = tag.getBoolean("hasStorageUpgrade");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("type", type);
        if (stacks[0] != null) {
            tag.setTag("stack0", stacks[0].writeToNBT(new NBTTagCompound()));
        }
        if (stacks[1] != null) {
            tag.setTag("stack1", stacks[1].writeToNBT(new NBTTagCompound()));
        }
        tag.setByte("rune", (byte) rune);
        tag.setByte("rotation", (byte) rotation);
        tag.setBoolean("hasStorageUpgrade", hasStorageUpgrade);
    }

    @Override
    public void markDirty() {
        if (worldObj.isRemote) {
            updateDisplayItem();
        }
        super.markDirty();
    }

    @SideOnly(Side.CLIENT)
    public boolean cycling;

    @SideOnly(Side.CLIENT)
    public void tryCycleRune() {
        if (!cycling) return;

        setRune(WhicheryUtils.rand.nextInt(RitualRegistry.CHALK_TYPES.get(type).runeCount));
        setRotation(WhicheryUtils.rand.nextInt(8));

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    // IInventory

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return stacks[slotIn];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (stacks[index] == null) return null;

        ItemStack tmp;
        if (stacks[index].stackSize <= count) {
            tmp = stacks[index];
            stacks[index] = null;
        } else {
            tmp = stacks[index].splitStack(count);

            if (stacks[index].stackSize == 0) {
                stacks[index] = null;
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
        this.stacks[index] = stack.copy();
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
