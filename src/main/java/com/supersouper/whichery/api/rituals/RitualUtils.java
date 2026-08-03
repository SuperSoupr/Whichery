package com.supersouper.whichery.api.rituals;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class RitualUtils {

    public static Ritual findRitualAt(IBlockAccess world, int x, int y, int z, byte[] rotationBuffer) {
        for (Ritual ritual : RitualRegistry.rituals()) {
            if (ritual.recipe.match(world, x, y, z, rotationBuffer, tes)) {
                return ritual;
            }
        }

        return null;
    }

    public static int packCoords(int x, int y, int z) {
        return packCoords((byte) x, (byte) y, (byte) z);
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
}
