package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelTurtleChestplate - Sir Squidly Created using Tabula 7.1.0 Heavy
 * adjustments made to this code by Toaster
 */
public class ModelArmorTurtle extends ModelCustomArmorBase {

	public ModelRenderer shell1;
	public ModelRenderer shell2;
	public ModelRenderer tassetR1;
	public ModelRenderer tassetL1;

	public ModelArmorTurtle(float scale) {
		super(scale, 64, 64);

		this.shell1 = new ModelRenderer(this, 0, 32);
		this.shell1.setRotationPoint(0.0F, -2.0F, 3.5F);
		this.shell1.addBox(-6.0F, 0.0F, 0.0F, 12, 16, 4, scale);

		this.shell2 = new ModelRenderer(this, 32, 32);
		this.shell2.setRotationPoint(0.0F, 0.0F, 7.5F);
		this.shell2.addBox(-4.0F, 0.0F, 0.0F, 8, 12, 2, scale);

		this.tassetR1 = new ModelRenderer(this, 0, 52);
		this.tassetR1.mirror = true;
		this.tassetR1.setRotationPoint(6.0F, 0.0F, -0.2F);
		this.tassetR1.addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5, scale);
		this.setRotateAngle(tassetR1, 0.0F, 0.0F, -0.4363323129985824F);

		this.tassetL1 = new ModelRenderer(this, 0, 52);
		this.tassetL1.setRotationPoint(-6.0F, 0F, -0.2F);
		this.tassetL1.addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5, scale);
		this.setRotateAngle(tassetL1, 0.0F, 0.0F, 0.4363323129985824F);

		this.bipedHead = new ModelRenderer(this, 30, 46);
		this.bipedHead.setRotationPoint(0F, 0F, 0F);
		this.bipedHead.addBox(-4.5F, -8.0F, -4.0F, 9, 8, 8, 0.2F + scale);

		this.bipedBody.addChild(this.shell2);
		this.bipedBody.addChild(this.shell1);
		this.bipedLeftLeg.addChild(this.tassetL1);
		this.bipedRightLeg.addChild(this.tassetR1);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}