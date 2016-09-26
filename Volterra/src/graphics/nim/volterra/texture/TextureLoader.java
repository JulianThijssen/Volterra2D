package graphics.nim.volterra.texture;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
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
	public enum TextureType {
		TYPE_RGB, TYPE_RGBA;
	}
	
	public static Texture load(String path, Sampling sampling) {
		Timer t = new Timer();
		t.start();

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);
        
		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 0);

		if (buffer == null) {
			Log.error("Failed to load texture at path: " + path);
		}

		Texture texture = uploadTexture(buffer, w.get(0), h.get(0), c.get(0), sampling);
		
		t.stop();
		float timeTaken = t.getElapsedNano() / 1e6f;
		Log.info(path + " loaded in: " + timeTaken + "ms");
		
		return texture;
	}
	
	public static Texture load(BufferedImage image, Sampling sampling) {
		int width = image.getWidth();
		int height = image.getHeight();

		TextureType type;
		switch (image.getType()) {
		case BufferedImage.TYPE_3BYTE_BGR:
			type = TextureType.TYPE_RGB; break;
		case BufferedImage.TYPE_4BYTE_ABGR:
			type = TextureType.TYPE_RGBA; break;
		default:
			type = TextureType.TYPE_RGBA;
		}

		int components = 4;
		if (type == TextureType.TYPE_RGB) {
			components = 3;
		}
		if (type == TextureType.TYPE_RGBA) {
			components = 4;
		}
		
		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * components);
		java.awt.image.WritableRaster wr = image.getRaster();
		buf.put((byte[]) wr.getDataElements(0, 0, width, height, null));
		buf.flip();

		Texture texture = uploadTexture(buf, width, height, components, sampling);
		
		return texture;
	}
	
	public static Texture createTex(int width, int height, int internal, int format, int type, Sampling sampling, float[] pixels) {
		int handle = glGenTextures();
		
		Texture texture = new Texture(handle, width, height, sampling);
		texture.bind();
		
		setGLSampling(texture.getSampling());
		
		if (pixels != null) {
			FloatBuffer buffer = BufferUtils.createFloatBuffer(pixels.length);
			buffer.put(pixels);
			buffer.flip();
			glTexImage2D(GL_TEXTURE_2D, 0, internal, width, height, 0, format, type, buffer);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, internal, width, height, 0, format, type, (ByteBuffer) null);
		}
		texture.unbind();
		
		return texture;
	}
	
	private static Texture uploadTexture(ByteBuffer buffer, int width, int height, int components, Sampling sampling) {
		int handle = glGenTextures();
		Texture texture = new Texture(handle, width, height, sampling);
		glBindTexture(GL_TEXTURE_2D, handle);

		if (components == 4) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		}
		else if (components == 3) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
		}

		setGLSampling(texture.getSampling());
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		return texture;
	}
	
	private static void setGLSampling(Sampling sampling) {
		switch (sampling) {
		case POINT:
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			break;
		case BILINEAR:
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			break;
		case TRILINEAR:
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glGenerateMipmap(GL_TEXTURE_2D);
			break;
		}
	}
}
