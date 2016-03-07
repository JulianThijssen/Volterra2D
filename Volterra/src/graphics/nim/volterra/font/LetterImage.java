package graphics.nim.volterra.font;

import java.awt.image.BufferedImage;

public class LetterImage {
	public char c;
	public BufferedImage image;
	public int width, height;
	
	public LetterImage(char c, BufferedImage image, int width, int height) {
		this.c = c;
		this.image = image;
		this.width = width;
		this.height = height;
	}
}
