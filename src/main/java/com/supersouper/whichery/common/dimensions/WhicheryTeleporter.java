package com.supersouper.whichery.common.dimensions;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class WhicheryTeleporter extends Teleporter {

    public WhicheryTeleporter(WorldServer worldServer) {
        super(worldServer);
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float rotationYaw) {
        entity.setLocationAndAngles(x, 65.0, z, entity.rotationYaw, 0.0F);
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    }

    @Override
    public void removeStalePortalLocations(long time) {}
}
