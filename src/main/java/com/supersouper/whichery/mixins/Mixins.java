package com.supersouper.whichery.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {
    // spotless:off

    // make sure to leave a trailing comma
//    EXAMPLE_MIXIN(new MixinBuilder("")
//        .addCommonMixins("minecraft.MixinMinecraftClass")
//        .setPhase(Phase.EARLY)
//        .setApplyIf(() -> true)),
    ;
    // leave trailing semicolon
    // spotless:on

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return this.builder;
    }
}
