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
import graphics.nim.volterra.texture.Texture;
import graphics.nim.volterra.util.Color;
import graphics.nim.volterra.util.Matrix4f;
import graphics.nim.volterra.util.Vector2f;
import graphics.nim.volterra.util.Vector3f;

public class Canvas {
	private static final float Z_NEAR = -1000;
	private static final float Z_FAR = 1000;
	private static final float Z_RANGE = Z_FAR - Z_NEAR;
	
	/** The internal quad for drawing all on-screen sprites.*/
	private int quad;
	
	/** The currently bound shader for drawing.*/
	private Shader shader;
	
	/** The current projection matrix, this matrix can be changed with {@link #setBounds(float, float, float, float) setBounds}.*/
	private Matrix4f projMatrix = new Matrix4f();
	/** The current view matrix, this matrix can be changed by a {@link graphics.nim.volterra.Camera Camera} object.*/
	private Matrix4f viewMatrix = new Matrix4f();
	/** The current model matrix, this matrix is changed in order to transform sprites.*/
	private Matrix4f modelMatrix = new Matrix4f();
	
	/** Contains the bounds of the screen. */
	private Bounds bounds = new Bounds(-1, 1, -1, 1);
	
	/** The current color with which all shapes are drawn.*/
	private Color color = new Color(1, 1, 1, 1);
	
	/** The currently bound font with which text is drawn.*/
	private Font font;
	/** The current color with which all text is drawn.*/
	private Vector3f fontColor = new Vector3f(1, 1, 1);

	/**
	 * Class constructor.
	 */
	public Canvas() {
		init();
	}
	
	/**
	 * Sets the currently bound shader for drawing.
	 * 
	 * @param name The name under which the shader is stored in {@link graphics.nim.volterra.Resources Resources}.
	 */
	public void setShader(String name) {
		shader = Resources.getShader(name);
		shader.bind();
		shader.uniformMatrix4f("projMatrix", projMatrix);
		shader.uniformMatrix4f("viewMatrix", viewMatrix);
	}
	
	/**
	 * Sets the font with which all further text is drawn.
	 * 
	 * @param font The font to use for text drawing.
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Sets the initial state of the canvas.
	 */
	private void init() {
		quad = ShapeLoader.getQuad();
		font = FontLoader.loadFont("Arial", 50);
		
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
		
		Resources.addShader("Unlit", "unlit.vert", "unlit.frag");
		setShader("Unlit");
	}
	
	/**
	 * Sets the color the canvas is cleared to upon calling {@link #clear() clear}.
	 * 
	 * @param r The red component of the color in the range [0-1].
	 * @param g The green component of the color in the range [0-1].
	 * @param b The blue component of the color in the range [0-1].
	 */
	public void setClearColor(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}
	
	/**
	 * Sets the color with which further shapes are drawn.
	 * 
	 * @param r The red component of the color in the range [0-1].
	 * @param g The green component of the color in the range [0-1].
	 * @param b The blue component of the color in the range [0-1].
	 * @param a The alpha component of the color in the range [0-1].
	 */
	public void setColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}
	
	/**
	 * Sets the color with which further text is drawn.
	 * 
	 * @param r The red component of the color in the range [0-1].
	 * @param g The green component of the color in the range [0-1].
	 * @param b The blue component of the color in the range [0-1].
	 */
	public void setFontColor(float r, float g, float b) {
		fontColor.set(r, g, b);
	}
	
	/**
	 * Takes a screenshot of the current framebuffer.
	 */
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
	
	/**
	 * Sets the bounds of the screen.
	 * 
	 * @param left The left boundary of the screen.
	 * @param right The right boundary of the screen.
	 * @param bottom The bottom boundary of the screen.
	 * @param top The top boundary of the screen.
	 */
	public void setBounds(float left, float right, float bottom, float top) {
		projMatrix.setIdentity();

		projMatrix.array[0] = 2 / (right - left);
		projMatrix.array[5] = 2 / (top - bottom);
		projMatrix.array[10] = -2 / (Z_RANGE);
		projMatrix.array[12] = -(right + left) / (right - left);
		projMatrix.array[13] = -(top + bottom) / (top - bottom);
		projMatrix.array[14] = -(Z_FAR + Z_NEAR) / (Z_RANGE);
		projMatrix.array[15] = 1;
		
		bounds.set(left, right, bottom, top);
		glViewport(0, 0, Window.width, Window.height);
	}
	
	public void setViewport(int left, int right, int w, int h) {
		glViewport(left, right, w, h);
	}
	
	/**
	 * Sets the camera with which the scene is rendered.
	 * Internally this method sets the viewMatrix member variable.
	 * 
	 * @param camera The camera with which to render the scene.
	 */
	public void setCamera(Camera camera) {
		Vector2f position = camera.getPosition();
		float rotation = camera.getRotation();
		Vector2f zoom = camera.getZoom();
		
		viewMatrix.setIdentity();
		viewMatrix.translate(new Vector3f(-position.x, -position.y, 0));
		viewMatrix.rotate(rotation, 0, 0, 1);
		viewMatrix.scale(new Vector3f(zoom.x, zoom.y, 1));
	}
	
	/**
	 * Binds a texture to an OpenGL texture slot.
	 * Internally this method makes the texture slot active
	 * before binding the given texture. The slot should
	 * be a constant integer instead of an OpenGL enum
	 * (0 instead of GL_TEXTURE0).
	 * 
	 * @param slot The texture slot to bind the texture to.
	 * @param texture The texture to bind.
	 */
	public void bindTexture(int slot, Texture texture) {
		glActiveTexture(GL_TEXTURE0 + slot);
		texture.bind();
	}
	
	/**
	 * Clears the color and depth buffer
	 */
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		shader.bind();
		shader.uniformMatrix4f("projMatrix", projMatrix);
		shader.uniformMatrix4f("viewMatrix", viewMatrix);
		glActiveTexture(GL_TEXTURE0);
	}
	
	/**
	 * Clears the color and depth buffer
	 */
	public void clearDepth() {
		glClear(GL_DEPTH_BUFFER_BIT);
		shader.bind();
		shader.uniformMatrix4f("projMatrix", projMatrix);
		shader.uniformMatrix4f("viewMatrix", viewMatrix);
		glActiveTexture(GL_TEXTURE0);
	}
	
	/**
	 * Draws a rectangle at position x,y with size w x h.
	 * The color of the rectangle can be set with {@link #setColor(float, float, float) setColor}.
	 * 
	 * @param x The x-component of the position of the rectangle.
	 * @param y The y-component of the position of the rectangle.
	 * @param w The width of the rectangle.
	 * @param h The height of the rectangle.
	 */
	public void drawRect(float x, float y, float w, float h) {
		drawRect(x, y, 0, w, h);
	}
	
	/**
	 * Draws a rectangle at position x,y,d with size w x h.
	 * The color of the rectangle can be set with {@link #setColor(float, float, float) setColor}.
	 * 
	 * @param x The x-component of the position of the rectangle.
	 * @param y The y-component of the position of the rectangle.
	 * @param d The depth of the position of the rectangle.
	 * @param w The width of the rectangle.
	 * @param h The height of the rectangle.
	 */
	public void drawRect(float x, float y, float d, float w, float h) {
		shader.uniform1i("hasTexture", 0);
		shader.uniform4f("color", color.r, color.g, color.b, color.a);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, -d / Z_RANGE));
		modelMatrix.scale(new Vector3f(w, h, 1));
		shader.uniformMatrix4f("modelMatrix", modelMatrix);
		
		glBindVertexArray(quad);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	/**
	 * Draws a texture at position x,y with a given depth value.
	 * 
	 * @param x The x-component of the position of the texture.
	 * @param y The y-component of the position of the texture.
	 * @param depth The depth of the position of the texture.
	 * @param image The texture to be rendered.
	 */
	public void drawImage(float x, float y, float depth, Texture texture) {
		texture.bind();
		shader.uniform1i("hasTexture", 1);
		shader.uniform1i("sprite", 0);
		shader.uniform4f("color", color.r, color.g, color.b, color.a);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, -depth / Z_RANGE));
		modelMatrix.scale(new Vector3f(texture.getWidth(), texture.getHeight(), 1));
		shader.uniformMatrix4f("modelMatrix", modelMatrix);
		
		glBindVertexArray(quad);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}

	/**
	 * Draws a sprite at position x,y with a given depth value.
	 * 
	 * @param x The x-component of the position of the sprite.
	 * @param y The y-component of the position of the sprite.
	 * @param depth The depth of the position of the sprite.
	 * @param sprite The sprite to be rendered.
	 */
	public void drawImage(float x, float y, float depth, Sprite sprite) {
		sprite.getTexture().bind();
		shader.uniform1i("hasTexture", 1);
		shader.uniform1i("sprite", 0);
		shader.uniform4f("color", color.r, color.g, color.b, color.a);
		
		sprite.update();
		
		Vector2f pivot = sprite.getPivot();
		modelMatrix.setIdentity();
		modelMatrix.translate(x-pivot.x, y-pivot.y, -depth/Z_RANGE);
		modelMatrix.rotate(0, 0, sprite.getRotation());
		if (!sprite.isFlipped()) {
			modelMatrix.scale(sprite.getWidth(), sprite.getHeight(), 1);
		} else {
			modelMatrix.scale(-sprite.getWidth(), sprite.getHeight(), 1);
		}
		shader.uniformMatrix4f("modelMatrix", modelMatrix);
		
		glBindVertexArray(sprite.getQuad());
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	/**
	 * Draws the specified string at position x,y and the specified depth
	 * 
	 * @param text The text to be rendered.
	 * @param x The x-component of the position of the text.
	 * @param y The y-component of the position of the text.
	 * @param depth The depth of the position of the text.
	 * @param scale The scale with which to resize the text.
	 */
	public void drawString(String text, int x, int y, float depth, float scale) {
		float stride = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			
			Letter letter = font.getHandle(c);
			
			font.texture.bind();
			shader.uniform1i("hasTexture", 1);
			shader.uniform1i("sprite", 0);
			shader.uniform4f("color", color.r, color.g, color.b, color.a);
			
			modelMatrix.setIdentity();
			modelMatrix.translate(new Vector3f(x + stride, y, -depth / Z_RANGE));
			modelMatrix.scale(new Vector3f(letter.width * scale, letter.height * scale, 1));
			shader.uniformMatrix4f("modelMatrix", modelMatrix);
			
			glBindVertexArray(letter.handle);
			glDrawArrays(GL_TRIANGLES, 0, 6);
			
			stride += letter.width * scale;
		}
	}
}
