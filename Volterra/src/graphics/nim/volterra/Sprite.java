package graphics.nim.volterra;

import graphics.nim.volterra.util.Log;

public class Sprite extends Component {
	private Texture texture;
	private int[] vaoArray;
	
	private int width;
	private int height;
	
	private int currentFrame = 0;
	private int numFrames = 1;
	
	public Sprite(Texture texture) {
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		
		vaoArray = new int[numFrames];
		vaoArray[0] = ShapeLoader.getSubQuad(0, 0, 1, 1);
	}
	
	public Sprite(Texture texture, int frameWidth, int frameHeight) {
		this.texture = texture;
		this.width = frameWidth;
		this.height = frameHeight;
		
		int numFramesX = texture.getWidth() / width;
		int numFramesY = texture.getHeight() / height;
		numFrames = numFramesX * numFramesY;
		System.out.println("Num Frames: " + numFrames);
		vaoArray = new int[numFrames];
		for (int i = 0; i < numFrames; i++) {
			float x1 = ((frameWidth * i) % texture.getWidth()) / (float)texture.getWidth();
			float y1 = ((frameHeight * i) % texture.getHeight()) / (float)texture.getHeight();
			float x2 = x1 + (frameWidth / (float)texture.getWidth());
			float y2 = y1 + (frameHeight / (float)texture.getHeight());
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
	
	public void nextFrame() {
		this.currentFrame = (currentFrame + 1) % numFrames;
	}
	
	public int getNumFrames() {
		return numFrames;
	}
}
