package com.teamcqr.chocolatequestrepoured.client.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiTextField;

public class GuiHelper {

	public static void drawTexture(double x, double y, double u, double v, double width, double height, double texWidth, double texHeight) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(u, v + texHeight);
		GL11.glVertex2d(x, y + height);

		GL11.glTexCoord2d(u + texWidth, v + texHeight);
		GL11.glVertex2d(x + width, y + height);

		GL11.glTexCoord2d(u + texWidth, v);
		GL11.glVertex2d(x + width, y);

		GL11.glTexCoord2d(u, v);
		GL11.glVertex2d(x, y);
		GL11.glEnd();
	}

	public static boolean isMouseOver(int mouseX, int mouseY, GuiTextField textField) {
		if (mouseX < textField.x) {
			return false;
		}
		if (mouseX > textField.x + textField.width) {
			return false;
		}
		if (mouseY < textField.y) {
			return false;
		}
		return mouseY <= textField.y + textField.height;
	}

}
