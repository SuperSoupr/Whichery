package com.supersouper.whichery.utils;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class NBTUtils {

    public static String[] StringNBTTagListToArray(NBTTagList tagList) {
        if (tagList == null) return null;
        if (tagList.func_150303_d() != 8) {
            throw new IllegalArgumentException(
                "Incorrect tag list type. Expected 8 but got " + tagList.func_150303_d());
        }
        String[] result = new String[tagList.tagCount()];
        for (int i = 0; i < tagList.tagCount(); i++) {
            result[i] = tagList.getStringTagAt(i);
        }
        return result;
    }

    public static NBTTagList StringArrayToNBTTagList(String[] array) {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < array.length; i++) {
            tagList.appendTag(new NBTTagString(array[i]));
        }
        return tagList;
    }
}
