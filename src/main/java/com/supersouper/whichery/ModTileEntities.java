package com.supersouper.whichery;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import com.supersouper.whichery.client.render.ChalkTESR;
import com.supersouper.whichery.common.tileentities.ChalkTileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public enum ModTileEntities {
    // spotless:off

    // make sure to leave a trailing comma
    CHALK(true, ChalkTileEntity.class, new ChalkTESR(), "")
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

    public static void initRenderers() {
        for (ModTileEntities te : VALUES) {
            if (te.isEnabled() && te.renderer != null) {
                ClientRegistry.bindTileEntitySpecialRenderer(te.clazz, te.renderer);
            }
        }
    }

    private final boolean isEnabled;
    private final Class<? extends TileEntity> clazz;
    private final TileEntitySpecialRenderer renderer;
    private final String name;

    ModTileEntities(Class<? extends TileEntity> clazz, String name) {
        this(true, clazz, name);
    }

    ModTileEntities(Boolean enabled, Class<? extends TileEntity> clazz, String name) {
        this(enabled, clazz, null, name);
    }

    ModTileEntities(Boolean enabled, Class<? extends TileEntity> clazz, TileEntitySpecialRenderer renderer,
        String name) {
        this.isEnabled = enabled;
        this.clazz = clazz;
        this.renderer = renderer;
        this.name = "TileEntity" + name + Whichery.MODID.toUpperCase();
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
