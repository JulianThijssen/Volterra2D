package graphics.nim.volterra;

import graphics.nim.volterra.util.Vector2f;

public class Camera {
	private Vector2f position = new Vector2f(0, 0);
	private float rotation = 0;
	private Vector2f zoom = new Vector2f(1, 1);
	
	/**
	 * Class constructor.
	 */
	public Camera() {
		
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public Vector2f getZoom() {
		return zoom;
	}
	
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public void setRotation(float angle) {
		rotation = angle;
	}
	
	public void setZoom(float zoomX, float zoomY) {
		this.zoom.set(zoomX, zoomY);
	}
}
