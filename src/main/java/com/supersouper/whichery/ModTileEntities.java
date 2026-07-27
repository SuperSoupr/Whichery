package com.supersouper.whichery;

import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.common.tileentities.ChalkTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;

public enum ModTileEntities {
    // spotless:off

    // make sure to leave a trailing comma
    CHALK(true, ChalkTileEntity.class, "")
    ;
    // spotless:on

    public static final ModTileEntities[] VALUES = values();

    public static void init() {
        for (ModTileEntities te : VALUES) {
            if (te.isEnabled()) {
                GameRegistry.registerTileEntity(te.clazz, te.name);
            }
        }
    }

    private final boolean isEnabled;
    private final Class<? extends TileEntity> clazz;
    private final String name;

    ModTileEntities(Class<? extends TileEntity> clazz, String name) {
        this(true, clazz, name);
    }

    ModTileEntities(Boolean enabled, Class<? extends TileEntity> clazz, String name) {
        this.isEnabled = enabled;
        this.clazz = clazz;
        this.name = "TileEntity" + name + Whichery.MODID.toUpperCase();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Class<? extends TileEntity> getTileEntityClass() {
        return clazz;
    }
}
