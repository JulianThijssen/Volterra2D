package graphics.nim.volterra;

import graphics.nim.volterra.font.Letter;
import graphics.nim.volterra.texture.Texture;

import java.util.HashMap;

public class Font {
	public Texture texture;
	public HashMap<Character, Letter> letters = new HashMap<Character, Letter>();
	
	public Font() {
		//spriteSheet = new Sprite(Resources.getTexture("Digits"), 8, 8);
	}
	
	public Letter getHandle(char c) {
		return letters.get(c);
	}
}
