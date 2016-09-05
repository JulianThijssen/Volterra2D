package graphics.nim.volterra.util;

import java.nio.FloatBuffer;

public class Color {
	public float r = 1;
	public float g = 1;
	public float b = 1;
	public float a = 1;
	
	public Color() {
		
	}
	
	public Color(float r, float g, float b, float a) {
		set(r, g, b, a);
	}
	
	public Color(float r, float g, float b) {
		set(r, g, b);
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public void set(Color v) {
		this.r = v.r;
		this.g = v.g;
		this.b = v.b;
		this.a = v.a;
	}
	
	public Color add(Color v) {
		this.r += v.r;
		this.g += v.g;
		this.b += v.b;
		return this;
	}
	
	public Color sub(Color v) {
		this.r -= v.r;
		this.g -= v.g;
		this.b -= v.b;
		return this;
	}
	
	public Color scale(float scale) {
		this.r *= scale;
		this.g *= scale;
		this.b *= scale;
		return this;
	}
	
	public float length() {
		return (float) Math.sqrt(r * r + g * g + b * b);
	}
	
	public Color normalise() {
		float length = length();
		this.r /= length;
		this.g /= length;
		this.b /= length;
		return this;
	}
	
	public void load(FloatBuffer buf) {
		r = buf.get();
		g = buf.get();
		b = buf.get();
	}
	
	public void store(FloatBuffer buf) {
		buf.put(r);
		buf.put(g);
		buf.put(b);
	}

	@Override
	public String toString() {
		return "("+r+", "+g+", "+b+")";
	}
	
	public static Color add(Color v1, Color v2) {
		return new Color(v1.r + v2.r, v1.g + v2.g, v1.b + v2.b);
	}
	
	public static Color sub(Color v1, Color v2) {
		return new Color(v1.r - v2.r, v1.g - v2.g, v1.b - v2.b);
	}
	
	public static Color scale(Color v, float scale) {
		return new Color(v.r * scale, v.g * scale, v.b * scale);
	}
	
	public static float dot(Color v1, Color v2) {
		return v1.r * v2.r + v1.g * v2.g + v1.b * v2.b;
	}
}
