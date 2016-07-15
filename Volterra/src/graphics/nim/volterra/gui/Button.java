package graphics.nim.volterra.gui;

import graphics.nim.volterra.Texture;
import graphics.nim.volterra.util.Vector2f;

public class Button {
	public Vector2f position = new Vector2f(0, 0);
	
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
