package com.supersouper.whichery.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.ResourceLocation;

import com.supersouper.whichery.common.entity.EntityMandrakeRoot;

public class SoundMandrakeIdle extends MovingSound {

    private final EntityMandrakeRoot entity;

    public SoundMandrakeIdle(EntityMandrakeRoot entity) {
        super(new ResourceLocation("whichery", "mandrake.idle"));
        this.entity = entity;
        repeat = true;
        volume = 2.0F;
    }

    @Override
    public void update() {
        if (entity.getHealth() <= 0) {
            donePlaying = true;
            return;
        }
        xPosF = (float) entity.posX;
        yPosF = (float) entity.posY;
        zPosF = (float) entity.posZ;
    }
}
