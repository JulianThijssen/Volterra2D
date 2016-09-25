package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	protected int handle;
	private int width;
	private int height;
	
	public Texture(int handle, int width, int height) {
		this.handle = handle;
		this.width = width;
		this.height = height;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
