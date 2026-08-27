package com.supersouper.whichery.api.rituals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RitualPreview {

    public final Ritual ritual;
    public final int x, y;
    public final IBlockAccess world;
    public final EntityPlayer player;
    public final int worldX, worldY, worldZ;
    public final int rotation;

    RitualPreview(Ritual ritual, int x, int y, IBlockAccess world, EntityPlayer player, int worldX, int worldY,
        int worldZ, int rotation) {
        this.ritual = ritual;
        this.x = x;
        this.y = y;
        this.world = world;
        this.player = player;
        this.worldX = worldX;
        this.worldY = worldY - ritual.recipe.centerY;
        this.worldZ = worldZ;
        this.rotation = rotation;
    }

    @SideOnly(Side.CLIENT)
    public static final ArrayList<RitualPreview> PREVIEWS_CLIENT = new ArrayList<>();
    public static final HashMap<EntityPlayer, ArrayList<RitualPreview>> PREVIEW_SERVER = new HashMap<>();

    private static final int[] rotationCorrections = new int[] { 2, 1, 0, 3 };
    public static final int[] rotationCorrections2 = new int[] { 0, 3, 2, 1 };

    public static void addPreview(Ritual ritual, World world, EntityPlayer player, int worldX, int worldY, int worldZ,
        int rotation) {
        if (world.isRemote) {
            RitualPreview.PREVIEWS_CLIENT.clear();
            RitualPreview.PREVIEWS_CLIENT.add(
                new RitualPreview(
                    ritual,
                    10,
                    10,
                    world,
                    player,
                    worldX,
                    worldY,
                    worldZ,
                    rotationCorrections[rotation]));
        } else {
            ArrayList<RitualPreview> previews = PREVIEW_SERVER.get(player);
            if (previews == null) {
                previews = new ArrayList<>();
                PREVIEW_SERVER.put(player, previews);
            }
            previews.clear();
            previews.add(
                new RitualPreview(
                    ritual,
                    10,
                    10,
                    world,
                    player,
                    worldX,
                    worldY,
                    worldZ,
                    rotationCorrections[rotation]));
        }
    }

    public static void clearPreviews() {
        RitualPreview.PREVIEWS_CLIENT.clear();
    }

    public static void removePreview(Ritual ritual) {
        RitualPreview.PREVIEWS_CLIENT.removeIf(preview -> preview.ritual.equals(ritual));
    }

    public static int getRuneFromPreview(World world, EntityPlayer player, int x, int y, int z,
        Function<IBlockMatcher, Boolean> checker, Function<IBlockMatcher, Integer> getter) {
        byte[] pos = new byte[3];
        int[] pos2d = new int[2];
        ArrayList<RitualPreview> previews = world.isRemote ? PREVIEWS_CLIENT
            : RitualPreview.PREVIEW_SERVER.getOrDefault(player, new ArrayList<>());
        for (RitualPreview preview : previews) {
            for (int i = 0; i < preview.ritual.recipe.matchers.length; i++) {
                IBlockMatcher matcher = preview.ritual.recipe.matchers[i];
                RitualUtils.unpackCoords(preview.ritual.recipe.matcherPositions[i], pos);

                pos2d[0] = pos[0];
                pos2d[1] = pos[2];
                for (int j = 0; j < RitualPreview.rotationCorrections2[preview.rotation]; j++) {
                    WhicheryUtils.rotate(pos2d, preview.ritual.recipe.centerX, preview.ritual.recipe.centerZ);
                }
                // world.setBlock(pre + pos[1], preview.worldZ - preview.ritual.recipe.centerZ + pos2d[1],
                // Blocks.stone);
                if (!(preview.world == world && preview.worldX - preview.ritual.recipe.centerX + pos2d[0] == x
                    && preview.worldY + pos[1] == y
                    && preview.worldZ - preview.ritual.recipe.centerZ + pos2d[1] == z)) {
                    continue;
                }

                if (checker.apply(matcher)) {
                    return getter.apply(matcher);
                }
            }
        }
        return -1;
    }
}
