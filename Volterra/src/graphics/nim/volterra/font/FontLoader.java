package graphics.nim.volterra.font;

import graphics.nim.volterra.Font;
import graphics.nim.volterra.ShapeLoader;
import graphics.nim.volterra.texture.Sampling;
import graphics.nim.volterra.texture.TextureLoader;
import graphics.nim.volterra.util.Log;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FontLoader {
	private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789,.!?;:'\"-()[]/";
	
	public static Font loadFont(String name, float size) {
		java.awt.Font font = new java.awt.Font(name, java.awt.Font.PLAIN, (int) size);
		
		return loadFont(font, size, true);
	}
	
	public static Font loadFont(String path, float size, boolean antialias) {
		java.awt.Font font = null;
		
		try {
			InputStream inputStream	= new FileInputStream(path);
		
			font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
			font = font.deriveFont(size);
		} catch (FileNotFoundException e) {
			Log.error("The font was not found at: " + path);
		} catch (IOException e) {
			Log.error("An error occurred while loading font: " + path);
		} catch (FontFormatException e) {
			Log.error("The specified font is incorrectly made");
		}
		
		return loadFont(font, size, antialias);
	}
	
	private static Font loadFont(java.awt.Font font, float size, boolean antialias) {
		List<LetterImage> charImages = new ArrayList<LetterImage>();
		
		int alphabetWidth = 0;
		int wMax = 0;
		int hMax = 0;
		
		// Get the width and height of the character
		BufferedImage tempfontImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tg = (Graphics2D) tempfontImage.getGraphics();
		if (antialias) {
			tg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		tg.setFont(font);
		FontMetrics fontMetrics = tg.getFontMetrics();
		
		// For every character in our alphabet
		for (int i = 0; i < alphabet.length(); i++) {
			char c = alphabet.charAt(i);
			
			// Get the width and height of the character in this font
			int charwidth = fontMetrics.charWidth(c);

			if (charwidth <= 0) {
				charwidth = 1;
			}
			int charheight = fontMetrics.getHeight();

			// Draw the character on a small buffered image
			BufferedImage fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D bg = (Graphics2D) fontImage.getGraphics();
			if (antialias) {
				bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			
			bg.setFont(font);
			bg.setColor(Color.WHITE);
			bg.drawString(String.valueOf(c), 0, 0 + fontMetrics.getAscent());
			System.out.println(c + " " + charwidth + " " + charheight);
			// Save the small buffered image in a LetterImage with the corresponding character and size
			LetterImage li = new LetterImage(c, fontImage, charwidth, charheight);
			charImages.add(li);
			
			// Calculate the total width of our alphabet
			alphabetWidth += charwidth;
			// Calculate the maximum width and height of a character
			if (charwidth > wMax) {
				wMax = charwidth;
			}
			if (charheight > hMax) {
				hMax = charheight;
			}
		}
		
		// Calculate the optimal size of our font texture
		int width = (alphabetWidth / 6) + wMax;
		int height = hMax * 6;
		
		// Draw the letters onto our font texture
		BufferedImage fontTexture = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D ftg = fontTexture.createGraphics();

		Font vFont = new Font();

		int x = 0, y = 0;
		for (int i = 0; i < charImages.size(); i++) {
			LetterImage li = charImages.get(i);
			if (x + li.width > width) {
				x = 0;
				y += li.height;
			}
			
			float u1 = (float) x / width;
			float u2 = u1 + ((float) li.width / width);
			float v1 = 1 - (float) y / height; // Flipped because we want (0, 0) to be bottom-left
			float v2 = v1 - ((float) li.height / height);
			int vao = ShapeLoader.getFontQuad(u1, v2, u2, v1); // V2 before V1, because it is closer to 0
			Letter letter = new Letter(vao, li.width, li.height);
			vFont.letters.put(li.c, letter);
			
			ftg.drawImage(li.image, x, y, null);
			x += li.width;
		}
		
		if (antialias) {
			vFont.texture = TextureLoader.load(fontTexture, Sampling.BILINEAR);
		} else {
			vFont.texture = TextureLoader.load(fontTexture, Sampling.POINT);
		}
		
		return vFont;
	}
}
