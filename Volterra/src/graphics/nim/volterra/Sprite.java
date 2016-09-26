package graphics.nim.volterra;

import graphics.nim.volterra.texture.Texture;
import graphics.nim.volterra.util.Log;
import graphics.nim.volterra.util.Vector2f;

public class Sprite {
	private Texture texture;
	private SpriteSheet spriteSheet;
	
	protected int width;
	protected int height;
	
	private Vector2f pivot = new Vector2f(0, 0);
	
	public float rotation = 0;
	private boolean flipped = false;
	
	private final boolean motile;
	public Animation animation = null;
	private int currentFrame = 0;
	
	public Sprite(Texture texture) {
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		motile = false;
	}

	public Sprite(SpriteSheet spriteSheet, float clipLength, boolean looping) {
		this.spriteSheet = spriteSheet;
		this.width = spriteSheet.getFrameWidth();
		this.height = spriteSheet.getFrameHeight();
		
		animation = new Animation(clipLength, looping);
		motile = true;
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
	
	public float getRotation() {
		return rotation;
	}
	
	public void setPivot(int xOffset, int yOffset) {
		pivot.set((float) xOffset, (float) yOffset);
	}

	public boolean isFlipped() {
		return flipped;
	}
	
	public void flip(boolean flip) {
		flipped = flip;
	}
	

	protected Texture getTexture() {
		if (motile) {
			return spriteSheet.getTexture();
		}
		return texture;
	}

	protected int getQuad() {
		if (motile) {
			return spriteSheet.getHandle(currentFrame);
		}
		return Resources.getShape("Quad");
	}
	
	
	// Animation
	public void setCurrentFrame(int frame) {
		if (frame < 0 || frame >= spriteSheet.getNumFrames()) {
			Log.error("Tried to open frame " + frame + " on sprite, but it's out of range.");
		}
		this.currentFrame = frame;
	}
	
	public void update() {
		if (!motile) {
			return;
		}
		final int numFrames = spriteSheet.getNumFrames();
		animation.update();
		setCurrentFrame((int) (animation.progress() * numFrames));
	}
}
