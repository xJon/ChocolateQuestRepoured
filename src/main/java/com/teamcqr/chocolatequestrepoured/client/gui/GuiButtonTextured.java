package com.teamcqr.chocolatequestrepoured.client.gui;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTextured extends GuiButton {

	private final ResourceLocation texture;
	private final ResourceLocation icon;
	private double u;
	private double v;
	private double texWidth;
	private double texHeight;

	public GuiButtonTextured(int buttonId, int x, int y, int width, int height, String texture, @Nullable String icon) {
		this(buttonId, x, y, width, height, texture, icon, 0.0D, 0.0D, 1.0D, 1.0D / 3.0D);
	}

	public GuiButtonTextured(int buttonId, int x, int y, int width, int height, String texture, @Nullable String icon, double u, double v, double texWidth, double texHeight) {
		super(buttonId, x, y, width, height, "");
		this.texture = new ResourceLocation(Reference.MODID, "textures/gui/" + texture + ".png");
		this.icon = icon != null && !icon.isEmpty() ? new ResourceLocation(Reference.MODID, "textures/gui/" + icon + ".png") : null;
		this.u = u;
		this.v = v;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			mc.getTextureManager().bindTexture(this.texture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GuiHelper.drawTexture(this.x, this.y, this.u, this.v + i * this.texHeight, this.width, this.height, this.texWidth, this.texHeight);
			if (this.icon != null) {
				mc.getTextureManager().bindTexture(this.icon);
				GuiHelper.drawTexture(this.x, this.y, 0.0D, 0.0D, this.width, this.height, 1.0D, 1.0D);
			}
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}

}