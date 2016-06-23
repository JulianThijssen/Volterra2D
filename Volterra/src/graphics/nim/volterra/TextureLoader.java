package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import graphics.nim.volterra.util.Log;
import graphics.nim.volterra.util.Timer;

public class TextureLoader {
	public static Texture load(String path) {
		Timer t = new Timer();

		t.start();

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);
        
		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 0);

		if (buffer == null) {
			Log.error("Failed to load texture at path: " + path);
		}

		int handle = uploadTexture(buffer, w.get(0), h.get(0), c.get(0));
		
		t.stop();
		System.out.println("STB: " + t.getElapsedNano());
		
		return new Texture(handle, w.get(0), h.get(0));
	}
	
	public static Texture load(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		// TODO Check if buffered image has alpha and change the 4 parameter
		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
		java.awt.image.WritableRaster wr = image.getRaster();
		Object o = wr.getDataElements(0, 0, width, height, null);
		buf.put((byte[]) o);
		buf.flip();

		int handle = uploadTexture(buf, width, height, 4);
		
		return new Texture(handle, width, height);
	}
	
	public static Texture createTex(int width, int height, int internal, int format, int type, float[] pixels) {
		int handle = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, handle);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		if (pixels != null) {
			FloatBuffer buffer = BufferUtils.createFloatBuffer(pixels.length);
			buffer.put(pixels);
			buffer.flip();
			glTexImage2D(GL_TEXTURE_2D, 0, internal, width, height, 0, format, type, buffer);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, internal, width, height, 0, format, type, (ByteBuffer) null);
		}
		glBindTexture(GL_TEXTURE_2D, 0);
		
		Texture texture = new Texture(handle, width, height);
		return texture;
	}
	
	private static int uploadTexture(ByteBuffer buffer, int width, int height, int components) {
		int handle = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, handle);

		if (components == 4) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		}
		else if (components == 3) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
		}

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glGenerateMipmap(GL_TEXTURE_2D);
		return handle;
	}
}
