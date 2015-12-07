package graphics.nim.volterra;

import graphics.nim.volterra.font.FontLoader;

import java.util.HashMap;

public class Resources {
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private static HashMap<String, Font> fonts = new HashMap<String, Font>();
	
	public static void addTexture(String name, String path) {
		Texture texture = TextureLoader.load(path);
		textures.put(name, texture);
	}
	
	public static Texture getTexture(String name) {
		return textures.get(name);
	}
	
	public static void addFont(String name, String path, float size, boolean antialias) {
		Font font = FontLoader.loadFont(path, size, antialias);
		fonts.put(name, font);
	}
	
	public static Font getFont(String name) {
		return fonts.get(name);
	}
}
