package graphics.nim.volterra.util;

public class MathUtil {
	public static float clamp(float f, float low, float high) {
		if (f < low) {
			return low;
		}
		else if (f > high) {
			return high;
		}
		return f;
	}
	
	public static int clamp(int i, int low, int high) {
		if (i < low) {
			return low;
		}
		else if (i > high) {
			return high;
		}
		return i;
	}
	
	public static float abs(float f) {
		if (f < 0) {
			return f * -1;
		}
		return f;
	}
	
	public static float sign(float f) {
		return (f > 0) ? 1: -1;
	}
	
	public static boolean inRange(float f, float low, float high) {
		return f >= low && f <= high;
	}
}
