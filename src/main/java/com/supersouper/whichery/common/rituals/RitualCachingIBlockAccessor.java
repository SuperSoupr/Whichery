package com.supersouper.whichery.common.rituals;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.ApiStatus;

import com.supersouper.whichery.Whichery;
import com.supersouper.whichery.api.rituals.RitualUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

@ApiStatus.Internal
public class RitualCachingIBlockAccessor implements IBlockAccess {

    private final IBlockAccess base;

    public RitualCachingIBlockAccessor(IBlockAccess base) {
        this.base = base;
    }

    Int2ObjectArrayMap<Block> getBlockCache = new Int2ObjectArrayMap<>();

    @Override
    public Block getBlock(int x, int y, int z) {
        int packed = RitualUtils.packCoords(x, y, z);
        Block result = getBlockCache.get(packed);
        if (result == null) {
            result = base.getBlock(x, y, z);
            getBlockCache.put(packed, result);
        }
        return result;
    }

    Int2ObjectArrayMap<TileEntity> getTileEntityCache = new Int2ObjectArrayMap<>();

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        int packed = RitualUtils.packCoords(x, y, z);
        TileEntity result = getTileEntityCache.get(packed);
        if (result == null) {
            result = base.getTileEntity(x, y, z);
            getTileEntityCache.put(packed, result);
        }
        return result;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int min) {
        Whichery.LOG.log(Level.DEBUG, "getLightBrightnessForSkyBlocks unimplemented in RitualCachingIBlockAccessor");
        return base.getLightBrightnessForSkyBlocks(x, y, z, min);
    }

    Int2IntArrayMap getBlockMetadataCache = new Int2IntArrayMap();

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        int packed = RitualUtils.packCoords(x, y, z);
        int result = getBlockMetadataCache.get(packed);
        if (result == getBlockMetadataCache.defaultReturnValue()) {
            result = base.getBlockMetadata(x, y, z);
            getBlockMetadataCache.put(packed, result);
        }
        return result;
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int directionIn) {
        Whichery.LOG.log(Level.DEBUG, "isBlockProvidingPowerTo unimplemented in RitualCachingIBlockAccessor");
        return base.isBlockProvidingPowerTo(x, y, z, directionIn);
    }

    Int2ObjectArrayMap<Boolean> isAirBlockCache;

    @Override
    public boolean isAirBlock(int x, int y, int z) {
        if (isAirBlockCache == null) {
            isAirBlockCache = new Int2ObjectArrayMap<>();
        }
        int packed = RitualUtils.packCoords(x, y, z);
        Boolean result = isAirBlockCache.get(packed);
        if (result == null) {
            result = base.isAirBlock(x, y, z);
            isAirBlockCache.put(packed, result);
        }
        return result;
    }

    Int2ObjectArrayMap<BiomeGenBase> getBiomeGenForCoordsCache;

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        if (getBiomeGenForCoordsCache == null) {
            getBiomeGenForCoordsCache = new Int2ObjectArrayMap<>();
        }
        int packed = RitualUtils.packCoords(x, 0, z);
        BiomeGenBase result = getBiomeGenForCoordsCache.get(packed);
        if (result == null) {
            result = base.getBiomeGenForCoords(x, z);
            getBiomeGenForCoordsCache.put(packed, result);
        }
        return result;
    }

    @Override
    public int getHeight() {
        Whichery.LOG.log(Level.DEBUG, "getHeight unimplemented in RitualCachingIBlockAccessor");
        return base.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        Whichery.LOG.log(Level.DEBUG, "extendedLevelsInChunkCache unimplemented in RitualCachingIBlockAccessor");
        return base.extendedLevelsInChunkCache();
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        Whichery.LOG.log(Level.DEBUG, "isSideSolid unimplemented in RitualCachingIBlockAccessor");
        return base.isSideSolid(x, y, z, side, _default);
    }
}
