package com.supersouper.whichery.client.render.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

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
		legLeft.setRotationPoint(0.0F, 24.0F, 0.0F);
		legLeft.cubeList.add(new ModelBox(legLeft, 10, 29, 0.0F, -2.0F, -2.0F, 1, 2, 1, 0.0F));

		legRight = new ModelRenderer(this);
		legRight.setRotationPoint(0.0F, 24.0F, 0.0F);
		legRight.cubeList.add(new ModelBox(legRight, 18, 29, 0.0F, -2.0F, 0.0F, 1, 2, 1, 0.0F));

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 8, 16, -1.0F, -7.0F, -3.0F, 3, 4, 5, 0.0F));
		body.cubeList.add(new ModelBox(body, 10, 25, -1.0F, -3.0F, -2.0F, 3, 1, 3, 0.0F));

		armLeft = new ModelRenderer(this);
		armLeft.setRotationPoint(0.0F, 24.0F, 0.0F);
		armLeft.cubeList.add(new ModelBox(armLeft, 4, 22, 0.0F, -6.0F, -4.0F, 1, 2, 1, 0.0F));

		armRight = new ModelRenderer(this);
		armRight.setRotationPoint(0.0F, 24.0F, 0.0F);
		armRight.cubeList.add(new ModelBox(armRight, 24, 22, 0.0F, -6.0F, 2.0F, 1, 2, 1, 0.0F));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 10, 10, -1.0F, -10.0F, -2.0F, 3, 3, 3, 0.0F));
		head.cubeList.add(new ModelBox(head, 4, 12, 0.0F, -12.0F, -3.0F, 2, 3, 1, 0.0F));
		head.cubeList.add(new ModelBox(head, 14, 6, 0.0F, -13.0F, -1.0F, 1, 3, 1, 0.0F));
		head.cubeList.add(new ModelBox(head, 22, 12, -1.0F, -12.0F, 1.0F, 2, 3, 1, 0.0F));
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch, float scale) {
        //legLeft.rotateAngleX  =  net.minecraft.client.model.ModelBiped.func_78172_a(limbSwing, 0.0F) * limbSwingAmount;
        //legRight.rotateAngleX = -net.minecraft.client.model.ModelBiped.func_78172_a(limbSwing, 0.0F) * limbSwingAmount;
		legLeft.render(scale);
		legRight.render(scale);
		body.render(scale);
		armLeft.render(scale);
		armRight.render(scale);
		head.render(scale);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
