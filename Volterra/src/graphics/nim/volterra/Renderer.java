package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import graphics.nim.volterra.util.Matrix4f;
import graphics.nim.volterra.util.Vector3f;

public class Renderer {
	private int shader;
	private static RenderQueue renderQueue = new RenderQueue();
	
	private Camera camera = new Camera(0, Window.width, 0, Window.height, -10, 10);
	private Matrix4f projMatrix = new Matrix4f();
	private Matrix4f modelMatrix = new Matrix4f();
	//private Font font;
	//private int quad = ShapeLoader.getQuad();
	
	public Renderer() {
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
		
		shader = ShaderLoader.loadShaders("res/unlit.vert", "res/unlit.frag");
		camera.loadProjectionMatrix(projMatrix);
	}
	
//	public void setFont(Font font) {
//		this.font = font;
//	}
	
	public void init() {
		
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		glUseProgram(shader);
		
		glUniformMatrix4fv(glGetUniformLocation(shader, "projMatrix"), false, projMatrix.getBuffer());
		glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());
		
		glActiveTexture(0);
		
		while (!renderQueue.isEmpty()) {
			RenderTask task = renderQueue.pop();
			
			if (task.getType() == RenderTask.TaskType.IMAGE) {
				Sprite sprite = task.sprite;
				Transform t = task.transform;
				
				glBindTexture(GL_TEXTURE_2D, sprite.getTextureHandle());
				glUniform1i(glGetUniformLocation(shader, "sprite"), 0);
				
				modelMatrix.setIdentity();
				modelMatrix.translate(new Vector3f(t.position.x, t.position.y, t.position.z));
				modelMatrix.scale(new Vector3f(sprite.getWidth() * t.scale.x, sprite.getHeight() * t.scale.y, 1));
				glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());
				
				glBindVertexArray(sprite.getHandle());
				glDrawArrays(GL_TRIANGLES, 0, 6);
			}
			if (task.getType() == RenderTask.TaskType.TEXT) {
				Transform t = task.transform;
				
				float x = 0;
				String text = task.text;
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);
					
					Letter letter = task.font.getHandle(c);
					
					glBindTexture(GL_TEXTURE_2D, task.font.spriteSheet.getHandle());
					glUniform1i(glGetUniformLocation(shader, "sprite"), 0);
					
					modelMatrix.setIdentity();
					modelMatrix.translate(new Vector3f(t.position.x + x, t.position.y, t.position.z));
					modelMatrix.scale(new Vector3f(letter.width * t.scale.x, letter.height * t.scale.y, 1));
					glUniformMatrix4fv(glGetUniformLocation(shader, "modelMatrix"), false, modelMatrix.getBuffer());

					glBindVertexArray(letter.handle);
					glDrawArrays(GL_TRIANGLES, 0, 6);
					
					x += letter.width * t.scale.x;
				}
			}
		}
	}
	
	public void setClearColor(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}
	
	public void setFontColor(float r, float g, float b) {
		
	}
	
	public static void draw(Entity e) {
		Transform transform = e.getComponent(Transform.class);
		Sprite sprite = e.getComponent(Sprite.class);
		
		RenderTask task = new RenderTask(sprite, transform);
		renderQueue.add(task);
	}
	
	public static void drawString(Font font, String text, int x, int y, float depth, float scale) {
		RenderTask task = new RenderTask(font, text, new Vector3f(x, y, depth), scale);
		renderQueue.add(task);
	}
}
