package com.supersouper.whichery;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

import com.supersouper.whichery.common.entity.EntityMandrakeRoot;

import cpw.mods.fml.common.registry.EntityRegistry;

public enum ModEntities {

    MANDRAKE_ROOT(EntityMandrakeRoot.class, "MandrakeRoot", 0x724b2c, 0x0a4b2c);

    private final Class<? extends Entity> entityClass;
    private final String name;
    private final int trackingRange;
    private final int updateFrequency;
    private final boolean sendsVelocityUpdates;
    private final int backgroundEggColor;
    private final int foregroundEggColor;

    private static int eggStartID = 14500;

    ModEntities(Class<? extends Entity> entityClass, String name) {
        this(entityClass, name, 64, 3, true, -1, -1);
    }

    ModEntities(Class<? extends Entity> entityClass, String name, int backgroundEggColor, int foregroundEggColor) {
        this(entityClass, name, 64, 3, true, backgroundEggColor, foregroundEggColor);
    }

    ModEntities(Class<? extends Entity> entityClass, String name, int trackingRange, int updateFrequency,
        boolean sendsVelocityUpdates, int backgroundEggColor, int foregroundEggColor) {
        this.entityClass = entityClass;
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
                entity.sendsVelocityUpdates);

            if (entity.backgroundEggColor != -1 && entity.foregroundEggColor != -1) {
                int id = getUniqueEggID();
                EntityList.IDtoClassMapping.put(id, entity.entityClass);
                EntityList.entityEggs
                    .put(id, new EntityList.EntityEggInfo(id, entity.backgroundEggColor, entity.foregroundEggColor));
            }
        }
    }

    private static int getUniqueEggID() {
        do {
            eggStartID++;
        } while (EntityList.getStringFromID(eggStartID) != null);
        return eggStartID;
    }
}
