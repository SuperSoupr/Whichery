package com.supersouper.whichery.api.rituals;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.supersouper.whichery.api.IBlockMatcher;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(
    iface = "com.gtnewhorizon.structurelib.alignment.constructable.IConstructable",
    modid = "structurelib")
public class RitualRecipe implements IConstructable {

    private final IBlockMatcher[][][] matchers;
    private final int centerX, centerZ, centerY;

    /**
     * IBlockMatcher 3d array formatted as: [y][x][z]
     * This is because most rituals are expected to only require one y level.
     */
    public RitualRecipe(IBlockMatcher[][][] matchers, int centerY, int centerX, int centerZ) {
        this.matchers = matchers;
        this.centerY = centerY;
        this.centerX = centerX;
        this.centerZ = centerZ;
    }

    public IBlockMatcher[][][] matchers() {
        return matchers;
    }

    public int centerX() {
        return centerX;
    }

    public int centerZ() {
        return centerZ;
    }

    public int centerY() {
        return centerY;
    }

    /**
     * Called with the coords of the "center" block to validate ritual placement
     */
    public boolean match(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    public void construct(World world, int x, int y, int z) {
        IBlockMatcher[][][] matchers = matchers();

        for (int cy = 0; cy < matchers.length; cy++) {
            for (int cx = 0; cx < matchers[cy].length; cx++) {
                for (int cz = 0; cz < matchers[cy][cx].length; cz++) {
                    IBlockMatcher matcher = matchers[cy][cx][cz];
                    if (matcher != null) {
                        matcher.place(world, x + cx, y + cy, z + cz);
                    }
                }
            }
        }
    }

    @Optional.Method(modid = "structurelib")
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Optional.Method(modid = "structurelib")
    @SideOnly(Side.CLIENT)
    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[0];
    }
}
