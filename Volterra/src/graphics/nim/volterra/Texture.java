package graphics.nim.volterra;

public class Texture {
	private int handle;
	private int width;
	private int height;
	
	public Texture(int handle, int width, int height) {
		this.handle = handle;
		this.width = width;
		this.height = height;
	}
	
	public int getHandle() {
		return handle;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
