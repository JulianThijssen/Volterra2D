package graphics.nim.volterra.texture;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	protected int handle;
	private int width;
	private int height;
	private Sampling sampling;
	
	public Texture(int handle, int width, int height) {
		this.handle = handle;
		this.width = width;
		this.height = height;
		this.sampling = Sampling.POINT;
	}
	
	public Texture(int handle, int width, int height, Sampling sampling) {
		this.handle = handle;
		this.width = width;
		this.height = height;
		this.sampling = sampling;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Sampling getSampling() {
		return sampling;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
