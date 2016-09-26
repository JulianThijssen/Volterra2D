package graphics.nim.volterra;

import graphics.nim.volterra.texture.Texture;
import graphics.nim.volterra.util.Log;

public class SpriteSheet {
	private Texture texture;
	private int[] vaoArray;
	
	private int frameWidth;
	private int frameHeight;
	private int numFrames = 1;
	
	public SpriteSheet(Texture texture, int frameWidth, int frameHeight) {
		this.texture = texture;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		int texWidth = texture.getWidth();
		int texHeight = texture.getHeight();
		
		int numFramesX = texWidth / frameWidth;
		int numFramesY = texHeight / frameHeight;
		numFrames = numFramesX * numFramesY;
		vaoArray = new int[numFrames];

		for (int i = 0; i < numFrames; i++) {
			float x1 = (((frameWidth * i) % texWidth) / (float) texWidth) + (0.5f / (float) texWidth);
			float y1 = (((frameHeight * i) % texHeight) / (float) texHeight) + (0.5f / (float) texHeight);
			float x2 = x1 + (frameWidth / (float) texWidth) - (0.5f / (float) texWidth);
			float y2 = y1 + (frameHeight / (float) texHeight) - (0.5f / (float) texHeight);
			vaoArray[i] = ShapeLoader.getSubQuad(x1, y1, x2, y2);
		}
		
		Log.debug("New spritesheet created!");
		Log.debug(this.toString());
	}
	
	public int getFrameWidth() {
		return frameWidth;
	}
	
	public int getFrameHeight() {
		return frameHeight;
	}
	
	public int getNumFrames() {
		return numFrames;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public int getHandle(int frame) {
		return vaoArray[frame];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Num Frames: " + numFrames + '\n');
		sb.append("Sheet size: " + texture.getWidth() + " x " + texture.getHeight() + '\n');
		sb.append("Frame size: " + frameWidth + " x " + frameHeight + '\n');
		
		return sb.toString();
	}
}
