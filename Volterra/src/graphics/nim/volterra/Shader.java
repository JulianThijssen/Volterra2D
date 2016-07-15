package graphics.nim.volterra;

import java.util.HashMap;

import graphics.nim.volterra.util.Matrix4f;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private int handle;
	
	private HashMap<String, Integer> locationMap = new HashMap<String, Integer>();
	
	public Shader(int handle) {
		this.handle = handle;
	}
	
	public void bind() {
		glUseProgram(handle);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public int location(String name) {
		Integer location = locationMap.get(name);
		if (location == null) {
			location = glGetUniformLocation(handle, name);
			locationMap.put(name, location);
		}
		return location;
	}
	
	public void uniform1i(String name, int value) {
		glUniform1i(location(name), value);
	}
	
	public void uniform1f(String name, float value) {
		glUniform1f(location(name), value);
	}
	
	public void uniform3f(String name, float v0, float v1, float v2) {
		glUniform3f(location(name), v0, v1, v2);
	}
	
	public void uniformMatrix4f(String name, Matrix4f m) {
		glUniformMatrix4fv(location(name), false, m.getBuffer());
	}
}
