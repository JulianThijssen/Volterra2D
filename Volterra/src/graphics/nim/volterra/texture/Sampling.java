package graphics.nim.volterra.texture;

public enum Sampling {
	POINT, BILINEAR, TRILINEAR;
	
	public static Sampling fromString(String sampling) {
		if ("trilinear".equals(sampling)) {
			return TRILINEAR;
		}
		else if ("bilinear".equals(sampling)) {
			return BILINEAR;
		}
		else {
			return POINT;
		}
	}
}
