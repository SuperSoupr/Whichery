package com.supersouper.whichery.common.rituals.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.supersouper.whichery.ModBlocks;
import com.supersouper.whichery.ModItems;
import com.supersouper.whichery.api.rituals.RitualRegistry;
import com.supersouper.whichery.api.rituals.matching.IBlockMatcher;
import com.supersouper.whichery.common.tileentities.ChalkRuneSmallTileEntity;
import com.supersouper.whichery.utils.WhicheryUtils;

public class BlockMatcherChalkSmall implements IBlockMatcher {

    private final String[] types;

    public BlockMatcherChalkSmall(String... types) {
        if (types.length != 4) {
            this.types = Arrays.copyOf(types, 4);
        } else {
            this.types = types;
        }
    }

    @Override
    public boolean match(IBlockAccess world, int x, int y, int z, ArrayList<TileEntity> tes) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof ChalkRuneSmallTileEntity cte)) return false;

        boolean match = true;
        String[] tmp = Arrays.copyOf(cte.getTypes(), types.length);
        required: for (String type : types) {
            if (type == null) continue;
            for (int j = 0; j < tmp.length; j++) {
                if (tmp[j] != null && tmp[j].equals(type)) {
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
        return new ItemStack(ModItems.CHALK_SMALL.get());
    }

    @Override
    public int[] itemStackHashCodes() {
        ArrayList<String> allTypes = new ArrayList<>();
        for (String type : types) {
            if (type != null && !allTypes.contains(type)) {
                allTypes.add(type);
            }
        }
        int itemHash = ModItems.CHALK_SMALL.get()
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
            Collections.shuffle(Arrays.asList(tmp));
            for (int i = 0; i < tmp.length; i++) {
                te.setType(i, tmp[i]);
                te.setRune(i, WhicheryUtils.rand.nextInt(RitualRegistry.CHALK_TYPES.get(tmp[i]).runeCount));
                te.setRotation(i, WhicheryUtils.rand.nextInt(4));
                te.markDirty();
            }
        }
    }
}
