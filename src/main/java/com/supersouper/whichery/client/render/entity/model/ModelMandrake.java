package com.supersouper.whichery.client.render.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelMandrake extends ModelBase {

    private final ModelRenderer legLeft;
    private final ModelRenderer legRight;
    private final ModelRenderer body;
    private final ModelRenderer armLeft;
    private final ModelRenderer armRight;
    private final ModelRenderer head;

    public ModelMandrake() {
        textureWidth = 32;
        textureHeight = 32;

        legLeft = new ModelRenderer(this);
        legLeft.setRotationPoint(1.0F, 22.0F, 0.0F);
        legLeft.cubeList.add(new ModelBox(legLeft, 16, 0, 0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F));

        legRight = new ModelRenderer(this);
        legRight.setRotationPoint(-1.0F, 22.0F, 0.0F);
        legRight.cubeList.add(new ModelBox(legRight, 20, 0, 0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F));

        armLeft = new ModelRenderer(this);
        armLeft.setRotationPoint(3.0F, 18.0F, 0.0F);
        armLeft.cubeList.add(new ModelBox(armLeft, 4, 18, 0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F));

        armRight = new ModelRenderer(this);
        armRight.setRotationPoint(-3.0F, 18.0F, 0.0F);
        armRight.cubeList.add(new ModelBox(armRight, 0, 18, 0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 24.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 0, 0, -2.0F, -7.0F, -1.0F, 5, 4, 3, 0.0F));
        body.cubeList.add(new ModelBox(body, 12, 7, -1.0F, -3.0F, -1.0F, 3, 1, 3, 0.0F));

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 24.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 7, -1.0F, -10.0F, -1.0F, 3, 3, 3, 0.0F));
        head.cubeList.add(new ModelBox(head, 0, 13, 2.0F, -12.0F, 0.0F, 1, 3, 2, 0.0F));
        head.cubeList.add(new ModelBox(head, 12, 14, 0.0F, -13.0F, 0.0F, 1, 3, 1, 0.0F));
        head.cubeList.add(new ModelBox(head, 6, 13, -2.0F, -12.0F, -1.0F, 1, 3, 2, 0.0F));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        legLeft.render(scale);
        legRight.render(scale);
        body.render(scale);
        armLeft.render(scale);
        armRight.render(scale);
        head.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch, float scaleFactor, Entity entity) {
        legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.5F * limbSwingAmount;
        legRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.5F * limbSwingAmount;

        armLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1F * limbSwingAmount;
        armRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1F * limbSwingAmount;
    }
}
