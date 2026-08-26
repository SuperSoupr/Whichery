package com.supersouper.whichery.common.rituals.matching;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.ModItems;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.common.blocks.BlockChalkRuneSmall;
import com.supersouper.whichery.common.tileentities.ChalkRuneSmallTileEntity;
import com.supersouper.whichery.utils.DrawUtils;
import com.supersouper.whichery.utils.WhicheryUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class BlockMatcherChalkSmall implements IBlockMatcher {

    private static final int[] minusOnes = new int[] { -1, -1, -1, -1 };
    private final String[] types;
    private final int[] runes;

    private BlockMatcherChalkSmall(String[] types, int[] runes) {
        this.types = types;
        this.runes = runes;
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof ChalkRuneSmallTileEntity cte)) return false;

        boolean match = true;
        String[] tmp = Arrays.copyOf(cte.getTypes(), types.length);
        required: for (int i = 0; i < types.length; i++) {
            if (types[i] == null) continue;
            for (int j = 0; j < tmp.length; j++) {
                if (tmp[j] != null && tmp[j].equals(types[i]) && (runes[i] == -1 || cte.getRune(j) == runes[i])) {
                    tmp[j] = null;
                    continue required;
                }
            }
            match = false;
            break;
        }
        if (match) {
            tes.add(te);
        }
        return match;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(ModItems.CHALK_STICK_SMALL.get());
    }

    @Override
    public int[] itemStackHashCodes() {
        ArrayList<String> allTypes = new ArrayList<>();
        for (String type : types) {
            if (type != null && !allTypes.contains(type)) {
                allTypes.add(type);
            }
        }
        int itemHash = ModItems.CHALK_STICK_SMALL.get()
            .hashCode();
        int[] itemStackHashCodes = new int[allTypes.size()];
        for (int i = 0; i < allTypes.size(); i++) {
            itemStackHashCodes[i] = itemHash + allTypes.get(i)
                .hashCode();
        }
        return itemStackHashCodes;
    }

    @Override
    public void place(World world, int x, int y, int z) {
        world.setBlock(x, y, z, ModBlocks.CHALK_RUNE_BLOCK_SMALL.get());
        ChalkRuneSmallTileEntity te = (ChalkRuneSmallTileEntity) world.getTileEntity(x, y, z);
        if (te != null) {
            String[] tmp = Arrays.copyOf(types, types.length);
            int[] tmp2 = Arrays.copyOf(runes, runes.length);
            shuffleRunesAndTypes(tmp2, tmp);

            for (int i = 0; i < tmp.length; i++) {
                te.setType(i, tmp[i]);

                if (tmp2[i] == -1) {
                    if (tmp[i] != null) {
                        te.setRune(i, WhicheryUtils.rand.nextInt(RitualRegistry.CHALK_TYPES.get(tmp[i]).runeCount));
                    }
                } else {
                    te.setRune(i, tmp2[i]);
                }

                te.setRotation(i, WhicheryUtils.rand.nextInt(8));
            }
            te.markDirty();
        }
    }

    @SideOnly(Side.CLIENT)
    private int[] cycleRunes;
    private int[] cycleRunesRandom;
    private String[] cycleTypes;
    @SideOnly(Side.CLIENT)
    private long lastCycle;

    @SideOnly(Side.CLIENT)
    @Override
    public void drawIcon(Tessellator t, TileEntity te, int x, int y, int z, int w, int h, double alpha) {
        if (cycleRunes == null) {
            cycleRunes = Arrays.copyOf(runes, runes.length);
            cycleTypes = Arrays.copyOf(types, types.length);
            cycleRunesRandom = new int[4];
        }
        if (System.currentTimeMillis() - lastCycle >= 1000) {
            shuffleRunesAndTypes(cycleRunes, cycleTypes);
            for (int i = 0; i < cycleTypes.length; i++) {
                if (cycleTypes[i] == null) continue;
                int a = WhicheryUtils.rand.nextInt(RitualRegistry.CHALK_TYPES.get(cycleTypes[i]).runeCount);
                cycleRunesRandom[i] = a;
            }
            lastCycle = System.currentTimeMillis();
        }

        String[] tmp2;
        if (te != null && te.getClass() == ChalkRuneSmallTileEntity.class) {
            ChalkRuneSmallTileEntity cte = (ChalkRuneSmallTileEntity) te;
            String[] tmp = Arrays.copyOf(cte.getTypes(), types.length);
            tmp2 = Arrays.copyOf(cycleTypes, types.length);
            required: for (int i = 0; i < tmp2.length; i++) {
                if (tmp2[i] == null) continue;
                for (int j = 0; j < tmp.length; j++) {
                    if (tmp[j] != null && tmp[j].equals(tmp2[i])
                        && (cycleRunes[i] == -1 || cte.getRune(j) == cycleRunes[i])) {
                        tmp[j] = null;
                        tmp2[i] = null;
                        continue required;
                    }
                }
            }
        } else {
            tmp2 = cycleTypes;
        }

        for (int i = 0; i < tmp2.length; i++) {
            String cycleType = tmp2[i];
            if (cycleType == null) continue;
            int cycleRuneC = cycleRunes[i];

            IIcon icon = RitualRegistry.RUNE_ICONS_SMALL.get(cycleType)[cycleRuneC == -1 ? cycleRunesRandom[i]
                : cycleRuneC];
            // RitualPreviewRenderer.setUniformsFromIcon(icon);
            int color = RitualRegistry.CHALK_TYPES.get(cycleType).drawColor;
            int r = (color >> 16) & 255;
            int g = (color >> 8) & 255;
            int b = color & 255;
            t.setColorRGBA(r, g, b, (int) (alpha * 255));
            DrawUtils.drawRect(
                t,
                x + w * BlockChalkRuneSmall.positions[i][0],
                y + h * BlockChalkRuneSmall.positions[i][1],
                z,
                w / 2,
                h / 2,
                icon.getMinU(),
                icon.getMinV(),
                icon.getMaxU(),
                icon.getMaxV());
        }
    }

    public static void shuffleRunesAndTypes(int[] arr1, String[] arr2) {
        for (int i = 0; i < 4; i++) {
            int to = WhicheryUtils.rand.nextInt(arr1.length);

            int tmp = arr1[i];
            arr1[i] = arr1[to];
            arr1[to] = tmp;

            String tmp2 = arr2[i];
            arr2[i] = arr2[to];
            arr2[to] = tmp2;
        }
    }

    private static final Int2ObjectOpenHashMap<BlockMatcherChalkSmall> cache = new Int2ObjectOpenHashMap<>();

    public static BlockMatcherChalkSmall of(Object... objects) {
        if (objects.length > 8) {
            throw new IllegalArgumentException(
                "Too many objects passed to BlockMatcherChalkSmall.of call (" + objects.length + ")");
        }
        int typeCount = 0;
        int runesCount = 0;
        String[] types = new String[4];
        int[] runes = new int[4];
        Arrays.fill(runes, -1);
        for (Object o : objects) {
            if (o instanceof String) {
                types[typeCount] = (String) o;
                typeCount++;
            } else if (o instanceof Integer) {
                runes[runesCount] = (int) o;
                runesCount++;
            } else {
                throw new IllegalArgumentException(
                    "Class '" + o.getClass() + "' is not allowed in BlockMatcherChalkSmall.of");
            }
        }
        return of(types, runes);
    }

    public static BlockMatcherChalkSmall of(String[] types, int[] runes) {
        if (types.length != 4) {
            types = Arrays.copyOf(types, 4);
        }

        int inHash = Arrays.hashCode(types) + Arrays.hashCode(runes);

        BlockMatcherChalkSmall matcher = cache.get(inHash);
        if (matcher == null) {
            matcher = new BlockMatcherChalkSmall(types, runes);
            cache.put(inHash, matcher);
        }
        return matcher;
    }
}
