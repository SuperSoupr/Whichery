package com.supersouper.whichery.mixins.accessors;

import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityLivingBase.class)
public interface EntityLivingBaseAccessor {

    @Invoker("getHurtSound")
    String getHurtSoundMixin();

    @Invoker("getSoundVolume")
    float getSoundVolumeMixin();

    @Invoker("getSoundPitch")
    float getSoundPitchMixin();

}
