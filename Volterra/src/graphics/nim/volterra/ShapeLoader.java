package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class ShapeLoader {
	public static final int VERTEX_COUNT = 3;
	public static final int UV_COUNT = 2;
	
	public static int getQuad() {
		return getSubQuad(0, 0, 1, 1);
	}
	
	public static int getSubQuad(float x1, float y1, float x2, float y2) {
		int[] elements = {0, 1, 2, 2, 1, 3};
		float[] vertices = {-0.5f, -0.5f, 0, 0.5f, -0.5f, 0, -0.5f, 0.5f, 0, 0.5f, 0.5f, 0};
		float[] texCoords = {x1, y1, x2, y1, x1, y2, x2, y2};
		
		return uploadData(vertices, texCoords, elements);
	}
	
	private static int uploadData(float[] vertices, float[] texCoords, int[] elements) {
		int vao = glGenVertexArrays();
		int vertexVBO = glGenBuffers();
		int texCoordVBO = glGenBuffers();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(elements.length * VERTEX_COUNT);
		FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(elements.length * UV_COUNT);
		
		for (int i = 0; i < elements.length; i++) {
			int index = elements[i];
			vertexBuffer.put(vertices[index * VERTEX_COUNT + 0]);
			vertexBuffer.put(vertices[index * VERTEX_COUNT + 1]);
			vertexBuffer.put(vertices[index * VERTEX_COUNT + 2]);
			texCoordBuffer.put(texCoords[index * UV_COUNT + 0]);
			texCoordBuffer.put(1 - texCoords[index * UV_COUNT + 1]);
		}
		vertexBuffer.flip();
		texCoordBuffer.flip();
		
		glBindVertexArray(vao);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexVBO);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, VERTEX_COUNT, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		
		glBindBuffer(GL_ARRAY_BUFFER, texCoordVBO);
		glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, UV_COUNT, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		return vao;
	}
}
