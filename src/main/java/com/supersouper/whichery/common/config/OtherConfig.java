package com.supersouper.whichery.common.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.supersouper.whichery.Whichery;

@Config.LangKey("whichery.config.other")
@Config(modid = Whichery.MODID, category = "other")
public class OtherConfig {

    @Config.Order(0)
    @Config.DefaultBoolean(true)
    @Config.Name("Example")
    @Config.Comment("Example Config Option")
    @Config.RequiresMcRestart
    public static boolean exampleConfigOption;
}
