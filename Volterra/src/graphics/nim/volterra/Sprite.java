package graphics.nim.volterra;

import graphics.nim.volterra.util.Log;

public class Sprite implements Component {
	private Texture texture;
	private int[] vaoArray;
	
	private int width;
	private int height;
	
	private int currentFrame = 0;
	private int numFrames = 1;
	private float time = 0;
	private float length = 1;
	
	private boolean flipped = false;
	
	public Sprite(Texture texture) {
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		
		vaoArray = new int[numFrames];
		vaoArray[0] = ShapeLoader.getSubQuad(0, 0, 1, 1);
	}
	
	public Sprite(Texture texture, int frameWidth, int frameHeight, float clipLength) {
		this.texture = texture;
		this.width = frameWidth;
		this.height = frameHeight;
		this.length = clipLength;
		
		int numFramesX = texture.getWidth() / width;
		int numFramesY = texture.getHeight() / height;
		numFrames = numFramesX * numFramesY;
		System.out.println("Num Frames: " + numFrames);
		vaoArray = new int[numFrames];
		System.out.println("Tex dims: " + texture.getWidth() + " x " + texture.getHeight());
		System.out.println("Frame dims: " + frameWidth + " x " + frameHeight);
		for (int i = 0; i < numFrames; i++) {
			float x1 = (((frameWidth * i) % texture.getWidth()) / (float)texture.getWidth()) + (0.5f / (float)texture.getWidth());
			float y1 = ((frameHeight * i) % texture.getHeight()) / (float)texture.getHeight() + (0.5f / (float)texture.getHeight());
			float x2 = x1 + (frameWidth / (float)texture.getWidth()) - (0.5f / (float)texture.getWidth());
			float y2 = y1 + (frameHeight / (float)texture.getHeight()) - (0.5f / (float)texture.getHeight());
			System.out.printf("%f, %f, %f, %f\n", x1, y1, x2, y2);
			vaoArray[i] = ShapeLoader.getSubQuad(x1, y1, x2, y2);
		}
	}
	
	public int getHandle() {
		return vaoArray[currentFrame];
	}
	
	public int getTextureHandle() {
		return texture.getHandle();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setCurrentFrame(int frame) {
		if (frame < 0 || frame >= numFrames) {
			Log.error("Tried to open frame " + frame + " on sprite, but it's out of range.");
		}
		this.currentFrame = frame;
	}
	
	public void update() {
		time += Time.renderTime;
		
		if (time > length / numFrames) {
			this.currentFrame = (currentFrame + 1) % numFrames;
			time = 0;
		}
	}
	
	public int getNumFrames() {
		return numFrames;
	}
	
	public boolean isFlipped() {
		return flipped;
	}
	
	public void flip(boolean flip) {
		flipped = flip;
	}
}
