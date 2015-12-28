package graphics.nim.volterra;

import graphics.nim.volterra.util.Vector2f;

public class Camera {
	private Vector2f position = new Vector2f(0, 0);
	private float zoom = 1;
	
	public Camera() {
		
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
}
