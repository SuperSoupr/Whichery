package com.supersouper.whichery.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.supersouper.whichery.client.render.entity.model.ModelMandrakeRoot;

public class RenderMandrakeRoot extends RenderLiving {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
        "whichery",
        "textures/models/entity/mandrake_root.png");

    public RenderMandrakeRoot() {
        super(new ModelMandrakeRoot(), 0.3F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}
