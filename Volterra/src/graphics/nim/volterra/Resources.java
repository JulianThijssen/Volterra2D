package graphics.nim.volterra;

import graphics.nim.volterra.font.FontLoader;
import graphics.nim.volterra.util.Log;

import java.util.HashMap;

public class Resources {
	private static HashMap<String, Shader> shaders = new HashMap<String, Shader>();
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();
	private static HashMap<String, Font> fonts = new HashMap<String, Font>();
	private static HashMap<String, Integer> shapes = new HashMap<String, Integer>();
	
	public static void addShader(String name, String vertpath, String fragpath) {
		Shader shader = ShaderLoader.loadShaders(vertpath, fragpath);
		shaders.put(name, shader);
	}
	
	public static void addTexture(String name, String path) {
		Texture texture = TextureLoader.load(path);
		textures.put(name, texture);
	}
	
	public static void addSpriteSheet(String name, String path, int frameWidth, int frameHeight) {
		addTexture(name, path); // XXX Should this be added to textures? Could cause conflicts
		Texture texture = getTexture(name);
		
		SpriteSheet spriteSheet = new SpriteSheet(texture, frameWidth, frameHeight);
		spriteSheets.put(name, spriteSheet);
	}
	
	public static void addFont(String name, String fontName, float size) {
		Font font = FontLoader.loadFont(fontName, size);
		fonts.put(name, font);
	}
	
	public static void addFont(String name, String path, float size, boolean antialias) {
		Font font = FontLoader.loadFont(path, size, antialias);
		fonts.put(name, font);
	}
	
	public static void addShape(String name, int shape) {
		shapes.put(name, shape);
	}
	
	public static Shader getShader(String name) {
		return shaders.get(name);
	}
	
	public static Texture getTexture(String name) {
		Texture texture = textures.get(name);
		
		if (texture == null) {
			Log.error("You are trying to load non-existing texture: " + name);
			System.exit(1);
		}
		
		return texture;
	}

	public static SpriteSheet getSpriteSheet(String name) {
		SpriteSheet spriteSheet = spriteSheets.get(name);
		
		if (spriteSheet == null) {
			Log.error("You are trying to load non-existing spritesheet: " + name);
			System.exit(1);
		}
		
		return spriteSheet;
	}

	public static Font getFont(String name) {
		return fonts.get(name);
	}
	
	public static int getShape(String name) {
		return shapes.get(name);
	}
}
