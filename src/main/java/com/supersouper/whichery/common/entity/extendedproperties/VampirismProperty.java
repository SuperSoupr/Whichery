package com.supersouper.whichery.common.entity.extendedproperties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.common.network.PacketHandler;
import com.supersouper.whichery.common.network.s2c.VampireStatsPacket;

public class VampirismProperty implements IExtendedEntityProperties {

    public static final String KEY = Whichery.MODID + "VampirismProperty";

    private boolean isVampire = false;
    private int vampireLevel = 0;
    private int blood = 0;
    private int maxBlood = 0;

    public void gainVampirism(EntityPlayer player) {
        if (isVampire) return;
        isVampire = true;
        vampireLevel = 1;
        blood = 800;
        maxBlood = 1000;
        syncToClient(player);
    }

    public void cureVampirism(EntityPlayer player) {
        if (!isVampire) return;
        isVampire = false;
        vampireLevel = 0;
        blood = 0;
        maxBlood = 0;
        syncToClient(player);
    }

    public boolean isVampire() {
        return isVampire;
    }

    public void setIsVampire(boolean isVampire, EntityPlayer player) {
        this.isVampire = isVampire;
        syncToClient(player);
    }

    public int getVampireLevel() {
        return vampireLevel;
    }

    public void setVampireLevel(int vampireLevel, EntityPlayer player) {
        this.vampireLevel = vampireLevel;
        syncToClient(player);
    }

    public int getMaxBlood() {
        return maxBlood;
    }

    public void setMaxBlood(int maxBlood, EntityPlayer player) {
        this.maxBlood = maxBlood;
        syncToClient(player);
    }

    public int getBlood() {
        return blood;
    }

    public void addBlood(int amount, EntityPlayer player) {
        blood = Math.min(maxBlood, blood + amount);
        syncToClient(player);
    }

    public void drainBlood(int amount, EntityPlayer player) {
        blood = Math.max(0, blood - amount);
        syncToClient(player);
    }

    public void setBlood(int amount, EntityPlayer player) {
        blood = Math.max(0, Math.min(maxBlood, amount));
        syncToClient(player);
    }

    public static VampirismProperty get(EntityPlayer player) {
        return (VampirismProperty) player.getExtendedProperties(KEY);
    }

    public static void syncToClient(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            VampirismProperty props = get(player);
            if (props != null) {
                PacketHandler.INSTANCE.sendTo(new VampireStatsPacket(props), (EntityPlayerMP) player);
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("isVampire", isVampire);
        tag.setInteger("vampireLevel", vampireLevel);
        tag.setInteger("blood", blood);
        tag.setInteger("maxBlood", maxBlood);

        compound.setTag(KEY, tag);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound tag = compound.getCompoundTag(KEY);

        if (tag != null) {
            isVampire = tag.getBoolean("isVampire");
            vampireLevel = tag.getInteger("vampireLevel");
            blood = tag.getInteger("blood");
            maxBlood = tag.getInteger("maxBlood");
        }
    }

    @Override
    public void init(Entity entity, World world) {

    }
}
