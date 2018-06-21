package graphics.nim.volterra.util;

public class Color {
	public static Color WHITE      = new Color(1, 1, 1, 1);
	public static Color BLACK      = new Color(0, 0, 0, 1);
	public static Color RED        = new Color(1, 0, 0, 1);
	public static Color GREEN      = new Color(0, 1, 0, 1);
	public static Color BLUE       = new Color(0, 0, 1, 1);
	public static Color GREY       = new Color(0.5f, 0.5f, 0.5f, 1);
	public static Color YELLOW     = new Color(1.0f, 1.0f, 0.0f, 1);
	public static Color MAGENTA    = new Color(1.0f, 0.0f, 1.0f, 1);
	public static Color CYAN       = new Color(0.0f, 1.0f, 1.0f, 1);
	public static Color PINK       = new Color(1.0f, 0.5f, 0.5f, 1);
	public static Color LIME       = new Color(0.5f, 1.0f, 0.5f, 1);
	
	public float r = 0;
	public float g = 0;
	public float b = 0;
	public float a = 1;
	
	public Color(float r, float g, float b) {
		set(r, g, b);
	}
	
	public Color(float r, float g, float b, float a) {
		set(r, g, b, a);
	}
	
	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		clamp();
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		clamp();
	}
	
	public Color add(Color c) {
		this.r += c.r;
		this.g += c.g;
		this.b += c.b;
		clamp();
		return this;
	}
	
	public Color sub(Color c) {
		this.r -= c.r;
		this.g -= c.g;
		this.b -= c.b;
		clamp();
		return this;
	}
	
	private float clamp(float f) {
		return f > 1 ? 1 : f < 0 ? 0 : f;
	}
	
	private void clamp() {
		r = clamp(r);
		g = clamp(g);
		b = clamp(b);
		a = clamp(a);
	}
	
	public static Color add(Color c1, Color c2) {
		return new Color(c1.r + c2.r, c1.g + c2.g, c1.b + c2.b);
	}
	
	public static Color sub(Color c1, Color c2) {
		return new Color(c1.r - c2.r, c1.g - c2.g, c1.b - c2.b);
	}
}
