package com.supersouper.whichery.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.supersouper.whichery.utils.NBTUtils;

public class ChalkSmallTileEntity extends TileEntity {

    private String[] types = new String[4];

    public ChalkSmallTileEntity() {

    }

    public ChalkSmallTileEntity(World world) {
        setWorldObj(world);
    }

    public void setType(int pos, String type) {
        this.types[pos] = type;
    }

    public String[] getTypes() {
        return types;
    }

    public String getType(int pos) {
        return types[pos];
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("types")) {
            types = NBTUtils.StringNBTTagListToArray(tag.getTagList("types", 8));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("types", NBTUtils.StringArrayToNBTTagList(types));
    }
}
