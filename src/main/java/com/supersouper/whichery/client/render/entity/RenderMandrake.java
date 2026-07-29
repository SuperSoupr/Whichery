package com.supersouper.whichery.client.render.entity;

import com.supersouper.whichery.client.render.entity.model.ModelMandrake;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderMandrake extends RenderLiving {

    private static final ResourceLocation TEXTURE =
        new ResourceLocation("whichery", "textures/models/entity/mandrake.png");

    public RenderMandrake() {
        super(new ModelMandrake(), 0.3F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}
