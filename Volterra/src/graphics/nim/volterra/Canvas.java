package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import graphics.nim.volterra.font.FontLoader;
import graphics.nim.volterra.font.Letter;
import graphics.nim.volterra.util.Matrix4f;
import graphics.nim.volterra.util.Vector2f;
import graphics.nim.volterra.util.Vector3f;

public class Canvas {
	private Shader shader;
	
	private Matrix4f projMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f modelMatrix = new Matrix4f();
	
	private Bounds bounds = new Bounds(-1, 1, -1, 1);
	
	private Font font;
	private Vector3f color = new Vector3f(1, 1, 1);
	private Vector3f fontColor = new Vector3f(1, 1, 1);
	private int quad = ShapeLoader.getQuad();

	public Canvas() {
		font = FontLoader.loadFont("Arial", 50);
		
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
		
		Resources.addShader("Unlit", "unlit.vert", "unlit.frag");
		setShader("Unlit");
	}
	
	public void setShader(String name) {
		shader = Resources.getShader(name);
		shader.bind();
		shader.uniformMatrix4f("projMatrix", projMatrix);
		shader.uniformMatrix4f("viewMatrix", viewMatrix);
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void init() {
		
	}
	
	public void setClearColor(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}
	
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}
	
	public void setFontColor(float r, float g, float b) {
		fontColor.set(r, g, b);
	}
	
	public void takeScreenshot() {
		glReadBuffer(GL_FRONT);

		int width = Window.width;
		int height = Window.height;
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		File file = new File("Screenshot.png");
		String format = "PNG";
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i + 0) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		
		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBounds(float left, float right, float bottom, float top) {
		projMatrix.setIdentity();

		float far = 100;
		float near = -100;
		projMatrix.array[0] = 2 / (right - left);
		projMatrix.array[5] = 2 / (top - bottom);
		projMatrix.array[10] = -2 / (far - near);
		projMatrix.array[12] = -(right + left) / (right - left);
		projMatrix.array[13] = -(top + bottom) / (top - bottom);
		projMatrix.array[14] = -(far + near) / (far - near);
		projMatrix.array[15] = 1;
		
		bounds.set(left, right, bottom, top);
		glViewport(0, 0, Window.width, Window.height);
	}
	
	public void setCamera(Camera camera) {
		Vector2f position = camera.getPosition();
		float rotation = camera.getRotation();
		Vector2f zoom = camera.getZoom();
		
		viewMatrix.setIdentity();
		viewMatrix.translate(new Vector3f(-position.x, -position.y, 0));
		viewMatrix.rotate(rotation, 0, 0, 1);
		viewMatrix.scale(new Vector3f(zoom.x, zoom.y, 1));
	}
	
	public void bindTexture(Texture texture) {
		glBindTexture(GL_TEXTURE_2D, texture.getHandle());
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT);
		shader.bind();
		shader.uniformMatrix4f("projMatrix", projMatrix);
		shader.uniformMatrix4f("viewMatrix", viewMatrix);
		glActiveTexture(GL_TEXTURE0);
	}
	
	public void drawRect(float x, float y, float w, float h) {
		shader.uniform1i("hasTexture", 1);
		shader.uniform3f("color", color.x, color.y, color.z);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, 0));
		modelMatrix.scale(new Vector3f(w, h, 1));
		shader.uniformMatrix4f("modelMatrix", modelMatrix);
		
		glBindVertexArray(quad);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	public void drawImage(float x, float y, float depth, Texture image) {
		glBindTexture(GL_TEXTURE_2D, image.getHandle());
		shader.uniform1i("hasTexture", 1);
		shader.uniform1i("sprite", 0);
		shader.uniform3f("color", color.x, color.y, color.z);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, 0));
		modelMatrix.scale(new Vector3f(image.getWidth(), image.getHeight(), 1));
		shader.uniformMatrix4f("modelMatrix", modelMatrix);
		
		glBindVertexArray(quad);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}

	public void drawImage(float x, float y, float depth, Sprite sprite) {
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureHandle());
		shader.uniform1i("hasTexture", 1);
		shader.uniform1i("sprite", 0);
		shader.uniform3f("color", color.x, color.y, color.z);
		
		int frames = sprite.getNumFrames();
		if (frames > 1) {
			sprite.update();
		}
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, 0));
		if (!sprite.isFlipped()) {
			modelMatrix.scale(new Vector3f(sprite.getWidth(), sprite.getHeight(), 1));
		} else {
			modelMatrix.scale(new Vector3f(-sprite.getWidth(), sprite.getHeight(), 1));
		}
		shader.uniformMatrix4f("modelMatrix", modelMatrix);
		
		glBindVertexArray(sprite.getHandle());
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	public void drawString(String text, int x, int y, float depth, float scale) {
		float stride = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			
			Letter letter = font.getHandle(c);
			
			glBindTexture(GL_TEXTURE_2D, font.spriteSheet.getHandle());
			shader.uniform1i("hasTexture", 1);
			shader.uniform1i("sprite", 0);
			shader.uniform3f("color", color.x, color.y, color.z);
			
			modelMatrix.setIdentity();
			modelMatrix.translate(new Vector3f(x + stride, y, depth));
			modelMatrix.scale(new Vector3f(letter.width * scale, letter.height * scale, 1));
			shader.uniformMatrix4f("modelMatrix", modelMatrix);
			
			glBindVertexArray(letter.handle);
			glDrawArrays(GL_TRIANGLES, 0, 6);
			
			stride += letter.width * scale;
		}
	}
}
