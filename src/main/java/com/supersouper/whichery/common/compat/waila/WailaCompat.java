package com.supersouper.whichery.common.compat.waila;

import com.supersouper.whichery.common.entity.demon.EntityPossessedChicken;

import mcp.mobius.waila.api.IWailaRegistrar;

@SuppressWarnings("unused")
public class WailaCompat {

    public static void load(IWailaRegistrar registrar) {
        registrar.registerOverrideEntityProvider(new PossessedCreatureEntityProvider(), EntityPossessedChicken.class);
    }

}
