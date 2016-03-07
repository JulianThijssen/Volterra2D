package graphics.nim.volterra;

import java.util.HashMap;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class Shader {
	public int handle;
	
	private HashMap<String, Integer> locationMap = new HashMap<String, Integer>();
	
	public Shader(int handle) {
		this.handle = handle;
	}
	
	public int location(String name) {
		Integer location = locationMap.get(name);
		if (location == null) {
			location = glGetUniformLocation(handle, name);
			locationMap.put(name, location);
		}
		return location;
	}
}
