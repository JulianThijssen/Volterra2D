package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
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
import static org.lwjgl.opengl.GL30.GL_R32F;

import graphics.nim.volterra.util.Log;
import graphics.nim.volterra.util.Timer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class TextureLoader {
	public static Texture load(String path) {
		Timer t = new Timer();

		try {
			t.start();
			BufferedImage image = ImageIO.read(new File(path));
			int width = image.getWidth();
			int height = image.getHeight();

			// TODO Check if buffered image has alpha and change the 4 parameter
			ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
			buf.put((byte[]) image.getRaster().getDataElements(0, 0, width, height, null));
			buf.flip();

			int handle = uploadTexture(width, height, buf);
			
			t.stop();
			System.out.println(t.getElapsed());
			return new Texture(handle, width, height);
		} catch (FileNotFoundException e) {
			Log.error("Image was not found: " + path);
		} catch (IOException e) {
			Log.error("An error occurred while loading the image: " + path);
		}
		
		return null;
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

		int handle = uploadTexture(width, height, buf);
		
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
		
		Texture texture = new Texture(handle, width, height);
		return texture;
	}
	
	private static int uploadTexture(int width, int height, ByteBuffer buffer) {
		int handle = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, handle);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glGenerateMipmap(GL_TEXTURE_2D);
		return handle;
	}
}
