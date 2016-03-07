package graphics.nim.volterra.gui;

import graphics.nim.volterra.Entity;
import graphics.nim.volterra.Texture;

public class Button extends Entity {
	private String label;
	private boolean pressed = false;
	
	private Texture defaultTexture = null;
	private Texture pressedTexture = null;
	
	public Button(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public Texture getTexture() {
		if (pressed && pressedTexture != null) {
			return pressedTexture;
		}
		return defaultTexture;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	public void setDefaultTexture(Texture texture) {
		this.defaultTexture = texture;
	}
	
	public void setPressedTexture(Texture texture) {
		this.pressedTexture = texture;
	}
}
