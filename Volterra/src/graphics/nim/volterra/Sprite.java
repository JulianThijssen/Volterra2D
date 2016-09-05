package graphics.nim.volterra;

import graphics.nim.volterra.util.Log;
import graphics.nim.volterra.util.Vector2f;

public class Sprite {
	private Texture texture;
	private int[] vaoArray;
	
	private int width;
	private int height;
	
	private Vector2f pivot = new Vector2f(0, 0);
	
	private int currentFrame = 0;
	private int numFrames = 1;
	private float time = 0;
	private float length = 1;
	
	public float rotation = 0;
	private boolean flipped = false;
	private boolean animation = false;
	private boolean playing = false;
	private boolean looping = false;
	private boolean finished = false;
	
	public Sprite(Texture texture) {
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		
		vaoArray = new int[numFrames];
		vaoArray[0] = ShapeLoader.getQuad();
	}
	
	public Sprite(Texture texture, int frameWidth, int frameHeight, float clipLength) {
		this.texture = texture;
		this.width = frameWidth;
		this.height = frameHeight;
		this.length = clipLength;
		
		int numFramesX = texture.getWidth() / width;
		int numFramesY = texture.getHeight() / height;
		numFrames = numFramesX * numFramesY;
		vaoArray = new int[numFrames];

		for (int i = 0; i < numFrames; i++) {
			float x1 = (((frameWidth * i) % texture.getWidth()) / (float)texture.getWidth()) + (0.5f / (float)texture.getWidth());
			float y1 = ((frameHeight * i) % texture.getHeight()) / (float)texture.getHeight() + (0.5f / (float)texture.getHeight());
			float x2 = x1 + (frameWidth / (float)texture.getWidth()) - (0.5f / (float)texture.getWidth());
			float y2 = y1 + (frameHeight / (float)texture.getHeight()) - (0.5f / (float)texture.getHeight());
			vaoArray[i] = ShapeLoader.getSubQuad(x1, y1, x2, y2);
		}
		setLength(clipLength);
		
		Log.debug("New sprite created!");
		Log.debug(this.toString());
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
	
	public Vector2f getPivot() {
		return pivot;
	}
	
	public void setLength(float length) {
		this.length = length;
		
		// Reset time back to 0 to prevent bugs in update()
		time = 0;
		animation = length > 0.0001f;
	}
	
	public void setCurrentFrame(int frame) {
		if (frame < 0 || frame >= numFrames) {
			Log.error("Tried to open frame " + frame + " on sprite, but it's out of range.");
		}
		this.currentFrame = frame;
	}
	
	public void setPivot(int xOffset, int yOffset) {
		pivot.set((float) xOffset, (float) yOffset);
	}
	
	public void play() {
		if (playing) {
			return;
		}
		time = 0;
		finished = false;
		playing = true;
	}
	
	public void stop() {
		playing = false;
		finished = true;
	}
	
	public void update() {
		if (playing) {
			time += Time.renderTime;
			
			if (time > length) {
				time = length - 0.001f;
				
				if (looping) {
					stop();
					play();
				} else {
					stop();
				}
			}
		}
		
		float f = time / length;
		setCurrentFrame((int) (f * numFrames));
	}
	
	public void setLooping(boolean looping) {
		this.looping = looping;
	}
	
	public boolean isFinished() {
		return finished;
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
	
	public boolean isAnimation() {
		return animation;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Num Frames: " + numFrames + '\n');
		sb.append("Sheet size: " + texture.getWidth() + " x " + texture.getHeight() + '\n');
		sb.append("Frame size: " + width + " x " + height + '\n');
		
		return sb.toString();
	}
}
