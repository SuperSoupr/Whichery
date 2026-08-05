package com.supersouper.whichery.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {
    // spotless:off

    // make sure to leave a trailing comma
    TRANSFORMATION_ENTITY_RENDER(new MixinBuilder("Render dummy entity for transformation")
        .addClientMixins("minecraft.MixinRenderPlayer")
        .setPhase(Phase.EARLY)),
    ACCESSORS(new MixinBuilder("Accessors")
        .addCommonMixins("minecraft.EntityAccessor")
        .setPhase(Phase.EARLY)),
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
