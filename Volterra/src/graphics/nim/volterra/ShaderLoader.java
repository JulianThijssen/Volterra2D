package graphics.nim.volterra;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import graphics.nim.volterra.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderLoader {
	public static final int LOG_SIZE = 1024;

	public static Shader loadShaders(String vertpath, String fragpath) {
		int vertexShader = loadShader(vertpath, GL_VERTEX_SHADER);
		int fragmentShader = loadShader(fragpath, GL_FRAGMENT_SHADER);
		
		int program = glCreateProgram();
		
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		
		glLinkProgram(program);
		glValidateProgram(program);
		
		return new Shader(program);
	}
	
	private static int loadShader(String filepath, int type) {
		StringBuilder shaderSource = new StringBuilder();
		
		int shader = 0;
		
		try {
			BufferedReader in;
			
			InputStream is = BaseGame.class.getClassLoader().getResourceAsStream(filepath);
			if (is == null) {
				File file = new File(filepath);
				in = new BufferedReader(new FileReader(file));
			} else {
				in = new BufferedReader(new InputStreamReader(is));
			}
			
			String line = null;
			while((line = in.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			in.close();
		} catch (FileNotFoundException e) {
			Log.error("Couldn't find shader at: " + filepath);
		} catch(IOException e) {
			Log.error("Could not load shader from: " + filepath);
		} 
		
		shader = glCreateShader(type);
		glShaderSource(shader, shaderSource);
		glCompileShader(shader);
		
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			StringBuilder sb = new StringBuilder();
			if (type == GL_VERTEX_SHADER) {
				sb.append("Vertex shader: ");
			}
			if (type == GL_FRAGMENT_SHADER) {
				sb.append("Fragment shader: ");
			}
			Log.error(sb.toString() + glGetShaderInfoLog(shader, LOG_SIZE));
		}
		
		return shader;
	}
}
