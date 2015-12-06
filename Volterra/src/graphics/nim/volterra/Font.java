package graphics.nim.volterra;

import java.util.HashMap;

public class Font {
	public Texture spriteSheet;
	public HashMap<Character, Letter> letters = new HashMap<Character, Letter>();
	
	public Font() {
		//spriteSheet = new Sprite(Resources.getTexture("Digits"), 8, 8);
	}
	
	public Letter getHandle(char c) {
		return letters.get(c);
	}
}
