package graphics.nim.volterra.font;

import graphics.nim.volterra.Font;
import graphics.nim.volterra.Letter;
import graphics.nim.volterra.ShapeLoader;
import graphics.nim.volterra.TextureLoader;
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
	private static String alphabet = "ABCDEFGIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789,.!?;:'\"-()[]/";
	
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
		
		int sum = 0;
		int xmax = 0;
		int ymax = 0;
		
		for (int i = 0; i < alphabet.length(); i++) {
			char c = alphabet.charAt(i);
			
			// Get the width and height of the character
			BufferedImage tempfontImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D tg = (Graphics2D) tempfontImage.getGraphics();
			if (antialias) {
				tg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			tg.setFont(font);
			FontMetrics fontMetrics = tg.getFontMetrics();
			int charwidth = fontMetrics.charWidth(c);

			if (charwidth <= 0) {
				charwidth = 1;
			}
			int charheight = fontMetrics.getHeight();
			
			// Draw the character on a small buffered image
			BufferedImage fontImage;
			fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D bg = (Graphics2D) fontImage.getGraphics();
			if (antialias) {
				bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			
			bg.setFont(font);
//			bg.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
//			bg.fillRect(0, 0, charwidth, charheight);
			bg.setColor(Color.WHITE);

			bg.drawString(String.valueOf(c), 0, 0 + fontMetrics.getAscent());

			LetterImage li = new LetterImage(c, fontImage, charwidth, charheight);
			charImages.add(li);
			sum += charwidth;
			if (charwidth > xmax) {
				xmax = charwidth;
			}
			if (charheight > ymax) {
				ymax = charheight;
			}
		}
		
		int width = sum / 6 + xmax;
		int height = ymax * 6;
		
		BufferedImage fontTexture = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D ftg = fontTexture.createGraphics();
//		ftg.setColor(Color.GREEN);
//		ftg.fillRect(0, 0, width, height);
//		ftg.setColor(Color.ORANGE);
//		ftg.fillRect(0, 0, 10, height);
//		ftg.setColor(Color.ORANGE);
//		ftg.fillRect(0, 0, width, 10);
		
		Font vFont = new Font();

		int x = 0, y = 0;
		for (int i = 0; i < charImages.size(); i++) {
			LetterImage li = charImages.get(i);
			float x1 = (float) x / width;
			float y1 = (float) y / height;
			float x2 = x1 + ((float) li.width / width);
			float y2 = y1 + ((float) li.height / height);
			int vao = ShapeLoader.getSubQuad(x1, 1-y2, x2, 1-y1); // FIXME what a mess of inversions
			Letter letter = new Letter(vao, li.width, li.height);
			vFont.letters.put(li.c, letter);
			
			ftg.drawImage(li.image, x, y, null);
			x += li.width;
			if (x > width - li.width) {
				x = 0;
				y += li.height;
			}
		}
		
		vFont.spriteSheet = TextureLoader.load(fontTexture);
		
		return vFont;
	}
}
