package graphics.nim.volterra.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Matrix4f {
	public float[] array = new float[16];
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
	
	public Matrix4f() {
		setIdentity();
	}
	
	public Matrix4f(float array[]) {
		for(int i = 0; i < 16; i++) {
			this.array[i] = array[i];
		}
	}

	public void setIdentity() {
		for(int i = 0; i < 16; i++) {
			if(i % 5 == 0) {
				array[i] = 1;
			} else {
				array[i] = 0;
			}
		}
	}
	
	public FloatBuffer getBuffer() {
		for(int i = 0; i < 16; i++) {
			buffer.put(array[i]);
		}
		buffer.flip();
		
		return buffer;
	}
	
	public void translate(Vector3f v) {
		array[12] += array[0] * v.x + array[4] * v.y + array[8] * v.z;
		array[13] += array[1] * v.x + array[5] * v.y + array[9] * v.z;
		array[14] += array[2] * v.x + array[6] * v.y + array[10] * v.z;
		array[15] += array[3] * v.x + array[7] * v.y + array[11] * v.z;
	}

	public void rotate(float angle, float x, float y, float z) {
		float c = (float) Math.cos(angle * Math.PI / 180);
		float s = (float) Math.sin(angle * Math.PI / 180);
		float ic = 1 - c;
		
		float f0 = array[0] * ((x * x * ic) + c) + array[4] * ((x * y * ic) + (z * s)) + array[8] * ((x * z * ic) - (y * s));
		float f1 = array[1] * ((x * x * ic) + c) + array[5] * ((x * y * ic) + (z * s)) + array[9] * ((x * z * ic) - (y * s));
		float f2 = array[2] * ((x * x * ic) + c) + array[6] * ((x * y * ic) + (z * s)) + array[10] * ((x * z * ic) - (y * s));
		float f3 = array[3] * ((x * x * ic) + c) + array[7] * ((x * y * ic) + (z * s)) + array[11] * ((x * z * ic) - (y * s));
		float f4 = array[0] * ((x * y * ic) - (z * s)) + array[4] * ((y * y * ic) + c) + array[8] * ((y * z * ic) + (x * s));
		float f5 = array[1] * ((x * y * ic) - (z * s)) + array[5] * ((y * y * ic) + c) + array[9] * ((y * z * ic) + (x * s));
		float f6 = array[2] * ((x * y * ic) - (z * s)) + array[6] * ((y * y * ic) + c) + array[10] * ((y * z * ic) + (x * s));
		float f7 = array[3] * ((x * y * ic) - (z * s)) + array[7] * ((y * y * ic) + c) + array[11] * ((y * z * ic) + (x * s));
		float f8 = array[0] * ((x * z * ic) + (y * s)) + array[4] * ((y * z * ic) - (x * s)) + array[8] * ((z * z * ic) + c);
		float f9 = array[1] * ((x * z * ic) + (y * s)) + array[5] * ((y * z * ic) - (x * s)) + array[9] * ((z * z * ic) + c);
		float f10 = array[2] * ((x * z * ic) + (y * s)) + array[6] * ((y * z * ic) - (x * s)) + array[10] * ((z * z * ic) + c);
		float f11 = array[3] * ((x * z * ic) + (y * s)) + array[7] * ((y * z * ic) - (x * s)) + array[11] * ((z * z * ic) + c);
		
		array[0] = f0;
		array[1] = f1;
		array[2] = f2;
		array[3] = f3;
		array[4] = f4;
		array[5] = f5;
		array[6] = f6;
		array[7] = f7;
		array[8] = f8;
		array[9] = f9;
		array[10] = f10;
		array[11] = f11;
	}
	
	public void rotate(Vector3f euler) {
		rotate(euler.x, 1, 0, 0);
		rotate(euler.y, 0, 1, 0);
		rotate(euler.z, 0, 0, 1);
	}

	public void scale(Vector3f scale) {
		array[0] = array[0] * scale.x;
		array[1] = array[1] * scale.x;
		array[2] = array[2] * scale.x;
		array[3] = array[3] * scale.x;
		array[4] = array[4] * scale.y;
		array[5] = array[5] * scale.y;
		array[6] = array[6] * scale.y;
		array[7] = array[7] * scale.y;
		array[8] = array[8] * scale.z;
		array[9] = array[9] * scale.z;
		array[10] = array[10] * scale.z;
		array[11] = array[11] * scale.z;
	}
	
	public Vector3f transform(Vector3f v, float w) {
		Vector3f dest = new Vector3f();
		dest.x = array[0] * v.x + array[4] * v.y + array[8] * v.z + array[12] * w;
		dest.y = array[1] * v.x + array[5] * v.y + array[9] * v.z + array[13] * w;
		dest.z = array[2] * v.x + array[6] * v.y + array[10] * v.z + array[14] * w;
		return dest;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(array[0]+" "+array[4]+" "+array[8]+" "+array[12]+"\n");
		sb.append(array[1]+" "+array[5]+" "+array[9]+" "+array[13]+"\n");
		sb.append(array[2]+" "+array[6]+" "+array[10]+" "+array[14]+"\n");
		sb.append(array[3]+" "+array[7]+" "+array[11]+" "+array[15]);
		return sb.toString();
	}
}
