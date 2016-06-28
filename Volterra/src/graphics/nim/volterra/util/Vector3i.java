package graphics.nim.volterra.util;

import java.nio.IntBuffer;

public class Vector3i {
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	public Vector3i() {
		
	}
	
	public Vector3i(int x, int y, int z) {
		set(x, y, z);
	}
	
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(Vector3i v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector3i add(Vector3i v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}
	
	public Vector3i sub(Vector3i v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}
	
	public Vector3i scale(int scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		return this;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public void load(IntBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
	}
	
	public void store(IntBuffer buf) {
		buf.put(x);
		buf.put(y);
		buf.put(z);
	}

	@Override
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
	
	public static Vector3i add(Vector3i v1, Vector3i v2) {
		return new Vector3i(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	public static Vector3i sub(Vector3i v1, Vector3i v2) {
		return new Vector3i(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	public static Vector3i scale(Vector3i v, int scale) {
		return new Vector3i(v.x * scale, v.y * scale, v.z * scale);
	}
	
	public static Vector3i negate(Vector3i v) {
		return new Vector3i(-v.x, -v.y, -v.z);
	}
}
