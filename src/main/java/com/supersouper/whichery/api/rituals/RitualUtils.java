package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class RitualUtils {

    public static Ritual findRitualAt(EntityPlayer player, IBlockAccess world, int x, int y, int z,
        byte[] rotationBuffer, ArrayList<TileEntity> tes) {
        for (Ritual ritual : RitualRegistry.rituals()) {
            if (ritual.recipe.match(player, world, x, y, z, rotationBuffer, tes)) {
                return ritual;
            }
        }

        return null;
    }

    public static int packCoords(byte x, byte y, byte z) {
        int px = ((x << 16) & 0xFF0000);
        int py = ((y << 8) & 0xFF00);
        int pz = (z & 0xFF);
        return px | py | pz;
    }

    public static void unpackCoords(int packedCoords, byte[] coords) {
        coords[0] = (byte) ((packedCoords >> 16) & 0xFF);
        coords[1] = (byte) ((packedCoords >> 8) & 0xFF);
        coords[2] = (byte) (packedCoords & 0xFF);
    }

    public static int hashItemStack(ItemStack item) {
        Objects.requireNonNull(Objects.requireNonNull(item));

        return RitualRegistry.ITEM_HASHERS.getOrDefault(
            item.getItem(),
            stack -> stack.getItem()
                .hashCode() + stack.getItemDamage())
            .apply(item);
    }

    public static ItemStack createChalkItem(Item item, String type) {
        ItemStack result = new ItemStack(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", type);
        result.setTagCompound(tag);
        return result;
    }
}
