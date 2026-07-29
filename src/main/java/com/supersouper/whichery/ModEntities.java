package com.supersouper.whichery;

import com.supersouper.whichery.client.render.entity.RenderMandrake;
import com.supersouper.whichery.common.entity.EntityMandrake;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public enum ModEntities {

    MANDRAKE(EntityMandrake.class, new RenderMandrake(), "Mandrake", 0x724b2c, 0x0a4b2c);

    private final Class<? extends Entity> entityClass;
    private final RenderLiving entityRenderer;
    private final String name;
    private final int trackingRange;
    private final int updateFrequency;
    private final boolean sendsVelocityUpdates;
    private final int backgroundEggColor;
    private final int foregroundEggColor;

    ModEntities(Class<? extends Entity> entityClass, RenderLiving entityRenderer, String name) {
        this(entityClass, entityRenderer, name, 64, 3, true, -1, -1);
    }

    ModEntities(Class<? extends Entity> entityClass, RenderLiving entityRenderer, String name, int backgroundEggColor, int foregroundEggColor) {
        this(entityClass, entityRenderer, name, 64, 3, true, backgroundEggColor, foregroundEggColor);
    }

    ModEntities(Class<? extends Entity> entityClass, RenderLiving entityRenderer, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int backgroundEggColor, int foregroundEggColor) {
        this.entityClass = entityClass;
        this.entityRenderer = entityRenderer;
        this.name = name;
        this.trackingRange = trackingRange;
        this.updateFrequency = updateFrequency;
        this.sendsVelocityUpdates = sendsVelocityUpdates;
        this.backgroundEggColor = backgroundEggColor;
        this.foregroundEggColor = foregroundEggColor;
    }

    public static void init() {
        for (ModEntities entity : ModEntities.values()) {
            EntityRegistry.registerModEntity(
                entity.entityClass,
                entity.name,
                entity.ordinal(),
                Whichery.INSTANCE,
                entity.trackingRange,
                entity.updateFrequency,
                entity.sendsVelocityUpdates
            );

            if (entity.backgroundEggColor != -1 && entity.foregroundEggColor != -1) {
                int id = EntityRegistry.findGlobalUniqueEntityId();
                EntityList.IDtoClassMapping.put(id, entity.entityClass);
                EntityRegistry.registerGlobalEntityID(entity.entityClass, entity.name, id, entity.backgroundEggColor, entity.foregroundEggColor);
            }
        }
    }

    public static void initClient() {
        for (ModEntities entity : ModEntities.values()) {
            if (entity.entityRenderer != null) {
                RenderingRegistry.registerEntityRenderingHandler(
                    entity.entityClass,
                    entity.entityRenderer
                );
            }
        }
    }
}
