package graphics.nim.volterra;

import graphics.nim.volterra.util.Matrix4f;
import graphics.nim.volterra.util.Vector2f;
import graphics.nim.volterra.util.Vector3f;

public class Camera {
	public static final float   DEFAULT_LEFT = -1;
	public static final float   DEFAULT_RIGHT = 1;
	public static final float   DEFAULT_TOP = 1;
	public static final float   DEFAULT_BOTTOM = -1;
	public static final float   DEFAULT_DEPTH = 20;
	
	private float   left        = DEFAULT_LEFT;
	private float   right       = DEFAULT_RIGHT;
	private float   top         = DEFAULT_TOP;
	private float   bottom      = DEFAULT_BOTTOM;
	private float   zNear       = -DEFAULT_DEPTH/2;
	private float   zFar        = DEFAULT_DEPTH/2;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	private Vector2f position = new Vector2f(0, 0);
	private float zoom = 1;
	
	public Camera() {
		recalculateProj();
		recalculateView();
	}
	
	public Camera(float width, float height) {
		this.left = -width/2;
		this.right = width/2;
		this.bottom = -height/2;
		this.top = height/2;
		recalculateProj();
		recalculateView();
	}
	
	public Matrix4f getProjMatrix() {
		return projectionMatrix;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	private void recalculateProj() {
		projectionMatrix.setIdentity();

		projectionMatrix.array[0] = 2 / (right - left);
		projectionMatrix.array[5] = 2 / (top - bottom);
		projectionMatrix.array[10] = -2 / (zFar - zNear);
		projectionMatrix.array[12] = -(right + left) / (right - left);
		projectionMatrix.array[13] = -(top + bottom) / (top - bottom);
		projectionMatrix.array[14] = -(zFar + zNear) / (zFar - zNear);
	}
	
	private void recalculateView() {
		viewMatrix.setIdentity();
		viewMatrix.translate(new Vector3f(position, 0));
		viewMatrix.scale(new Vector3f(zoom, zoom, 1));
	}
	
	public void setWidth(float width) {
		this.left = -width / 2;
		this.right = width / 2;
		recalculateProj();
	}
	
	public void setHeight(float height) {
		this.bottom = -height / 2;
		this.top = height / 2;
		recalculateProj();
	}
	
	public void setDepth(float depth) {
		this.zNear = -depth/2;
		this.zFar = depth/2;
		recalculateProj();
	}
	
	public void setPosition(float x, float y) {
		position.set(x, y);
		recalculateView();
	}
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
		recalculateView();
	}
}
