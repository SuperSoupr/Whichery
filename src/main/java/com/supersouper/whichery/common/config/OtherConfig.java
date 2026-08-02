package com.supersouper.whichery.common.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.supersouper.whichery.Whichery;

@Config.LangKey("whichery.config.other")
@Config(modid = Whichery.MODID, category = "other")
public class OtherConfig {

    @Config.DefaultInt(67)
    @Config.RequiresMcRestart
    public static int spiritRealmDimID;

    @Config.DefaultInt(167)
    @Config.RequiresMcRestart
    public static int spiritRealmBiomeID;
}
