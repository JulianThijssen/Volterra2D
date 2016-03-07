package graphics.nim.volterra;

public class Bounds {
	public static final float   DEFAULT_LEFT = -1;
	public static final float   DEFAULT_RIGHT = 1;
	public static final float   DEFAULT_BOTTOM = -1;
	public static final float   DEFAULT_TOP = 1;

	public float left   = DEFAULT_LEFT;
	public float right  = DEFAULT_RIGHT;
	public float bottom = DEFAULT_BOTTOM;
	public float top    = DEFAULT_TOP;

	public Bounds() {
		
	}
	
	public Bounds(float width, float height) {
		set(-width / 2, width / 2, -height / 2, height / 2);
	}
	
	public Bounds(float left, float right, float bottom, float top) {
		set(left, right, bottom, top);
	}
	
	public void set(float left, float right, float bottom, float top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
	}
}
