package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import graphics.nim.volterra.font.FontLoader;
import graphics.nim.volterra.font.Letter;
import graphics.nim.volterra.util.Matrix4f;
import graphics.nim.volterra.util.Vector2f;
import graphics.nim.volterra.util.Vector3f;

public class Canvas {
	private int shader;
	
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
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
		
		shader = ShaderLoader.loadShaders("unlit.vert", "unlit.frag");
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
	
	public void setBounds(float left, float right, float bottom, float top) {
		projMatrix.setIdentity();

		projMatrix.array[0] = 2 / (right - left);
		projMatrix.array[5] = 2 / (top - bottom);
		projMatrix.array[10] = -1;
		projMatrix.array[12] = -(right + left) / (right - left);
		projMatrix.array[13] = -(top + bottom) / (top - bottom);
		projMatrix.array[14] = 0;
		
		bounds.set(left, right, bottom, top);
		glViewport(0, 0, (int) (right-left), (int) (top-bottom));
	}
	
	public void setCamera(Camera camera) {
		Vector2f position = camera.getPosition();
		float zoom = camera.getZoom();
		
		viewMatrix.setIdentity();
		viewMatrix.translate(new Vector3f(-position.x, -position.y, 0));
		viewMatrix.scale(new Vector3f(zoom, zoom, 1));
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT);
		glUseProgram(shader);
		glUniformMatrix4fv(glGetUniformLocation(shader, "projMatrix"), false, projMatrix.getBuffer());
		glUniformMatrix4fv(glGetUniformLocation(shader, "viewMatrix"), false, viewMatrix.getBuffer());
		glActiveTexture(0);
	}
	
	public void draw(Entity e) {
		Transform t = e.getComponent(Transform.class);
		Sprite sprite = e.getComponent(Sprite.class);
		
		int frames = sprite.getNumFrames();
		if (frames > 1) {
			sprite.update();
		}
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureHandle());
		glUniform1i(glGetUniformLocation(shader, "hasTexture"), 1);
		glUniform1i(glGetUniformLocation(shader, "sprite"), 0);
		glUniform3f(glGetUniformLocation(shader, "color"), color.x, color.y, color.z);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(t.position.x, t.position.y, t.depth));
		if (!sprite.isFlipped()) {
			modelMatrix.scale(new Vector3f(sprite.getWidth() * t.scale.x, sprite.getHeight() * t.scale.y, 1));
		} else {
			modelMatrix.scale(new Vector3f(sprite.getWidth() * -t.scale.x, sprite.getHeight() * t.scale.y, 1));
		}
		glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());
		
		glBindVertexArray(sprite.getHandle());
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	public void drawRect(float x, float y, float w, float h) {
		glUniform1i(glGetUniformLocation(shader, "hasTexture"), 0);
		glUniform3f(glGetUniformLocation(shader, "color"), color.x, color.y, color.z);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, 0));
		modelMatrix.scale(new Vector3f(w, h, 1));
		glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());
		
		glBindVertexArray(quad);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	public void drawImage(float x, float y, Texture image) {
		glBindTexture(GL_TEXTURE_2D, image.getHandle());
		glUniform1i(glGetUniformLocation(shader, "hasTexture"), 1);
		glUniform1i(glGetUniformLocation(shader, "sprite"), 0);
		glUniform3f(glGetUniformLocation(shader, "color"), 1, 1, 1);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, 0));
		modelMatrix.scale(new Vector3f(image.getWidth(), image.getHeight(), 1));
		glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());
		
		glBindVertexArray(quad);
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	public void drawImage(float x, float y, Sprite sprite) {
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureHandle());
		glUniform1i(glGetUniformLocation(shader, "hasTexture"), 1);
		glUniform1i(glGetUniformLocation(shader, "sprite"), 0);
		glUniform3f(glGetUniformLocation(shader, "color"), 1, 1, 1);
		
		modelMatrix.setIdentity();
		modelMatrix.translate(new Vector3f(x, y, 0));
		modelMatrix.scale(new Vector3f(sprite.getWidth(), sprite.getHeight(), 1));
		glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());
		
		glBindVertexArray(sprite.getHandle());
		glDrawArrays(GL_TRIANGLES, 0, 6);
	}
	
	public void drawString(String text, int x, int y, float depth, float scale) {
		float stride = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			
			Letter letter = font.getHandle(c);
			
			glBindTexture(GL_TEXTURE_2D, font.spriteSheet.getHandle());
			glUniform1i(glGetUniformLocation(shader, "hasTexture"), 1);
			glUniform1i(glGetUniformLocation(shader, "sprite"), 0);
			glUniform3f(glGetUniformLocation(shader, "color"), fontColor.x, fontColor.y, fontColor.z);
			
			modelMatrix.setIdentity();
			modelMatrix.translate(new Vector3f(x + stride, y, depth));
			modelMatrix.scale(new Vector3f(letter.width * scale, letter.height * scale, 1));
			glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());

			glBindVertexArray(letter.handle);
			glDrawArrays(GL_TRIANGLES, 0, 6);
			
			stride += letter.width * scale;
		}
	}
}
