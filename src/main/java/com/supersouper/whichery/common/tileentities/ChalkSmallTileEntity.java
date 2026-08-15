package com.supersouper.whichery.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.utils.ArrayUtils;
import com.supersouper.whichery.utils.NBTUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChalkSmallTileEntity extends TileEntity {

    private String[] types = new String[4];
    private int[] runes = new int[4];
    private int[] rotations = new int[4];
    @SideOnly(Side.CLIENT)
    public boolean[] hides;

    public ChalkSmallTileEntity() {
        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient()) {
            hides = new boolean[4];
        }
    }

    public ChalkSmallTileEntity(World world) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient()) {
            hides = new boolean[4];
        }
        setWorldObj(world);
    }

    public void setType(int pos, String type) {
        this.types[pos] = type;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String[] getTypes() {
        return types;
    }

    public String getType(int pos) {
        return types[pos];
    }

    @SideOnly(Side.CLIENT)
    public void hide(int pos, boolean hide) {
        this.hides[pos] = hide;
    }

    @SideOnly(Side.CLIENT)
    public boolean isHidden(int pos) {
        return hides[pos];
    }

    private void sanitizeTypes() {
        for (int i = 0; i < types.length; i++) {
            if (types[i] != null && !RitualRegistry.chalkExists(types[i])) {
                types[i] = RitualRegistry.DEFAULT_CHALK_TYPE_NAME;
            }
        }
    }

    public void setRotation(int pos, int rotation) {
        this.rotations[pos] = rotation;
    }

    public int[] getRotations() {
        return this.rotations;
    }

    public int getRotation(int pos) {
        return this.rotations[pos];
    }

    public void setRune(int pos, int rune) {
        this.runes[pos] = rune;
    }

    public int getRune(int pos) {
        return this.runes[pos];
    }

    public int[] getRunes() {
        return this.runes;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("types")) {
            types = NBTUtils.StringNBTTagListToArray(tag.getTagList("types", 8));
        }
        sanitizeTypes();
        runes = ArrayUtils.byteArrayToIntArray(tag.getByteArray("runes"));
        rotations = ArrayUtils.byteArrayToIntArray(tag.getByteArray("rotations"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("types", NBTUtils.StringArrayToNBTTagList(types));
        tag.setByteArray("runes", ArrayUtils.intArrayToByteArray(runes));
        tag.setByteArray("rotations", ArrayUtils.intArrayToByteArray(rotations));
    }
}
