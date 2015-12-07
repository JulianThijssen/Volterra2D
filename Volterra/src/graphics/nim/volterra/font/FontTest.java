package graphics.nim.volterra.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FontTest extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789,.!?;:'\"-()[]/";
	JPanel panel;
	Graphics2D g;
	
	public FontTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		add(panel);
		
		setSize(600, 600);
		setVisible(true);
		
		g = (Graphics2D) panel.getGraphics();

		//draw(g);
		
		new Thread(this).start();
	}
	
	public void run() {
		while (true) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			
			try {
				draw(g, true);
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//panel.repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void draw(Graphics2D g, boolean antialias) throws FontFormatException, IOException {
		Font font = new Font("Arial", Font.PLAIN, 60);
		//InputStream inputStream	= new FileInputStream("NESCyrillic.ttf");
		
		//Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		font = font.deriveFont(60f);
		
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
			bg.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
			bg.fillRect(0, 0, charwidth, charheight);
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
		ftg.setColor(Color.GREEN);
		ftg.fillRect(0, 0, width, height);
		
		int x = 0, y = 0;
		for (int i = 0; i < charImages.size(); i++) {
			LetterImage li = charImages.get(i);

			ftg.drawImage(li.image, x, y, null);
			x += li.width;
			if (x > width - li.width) {
				x = 0;
				y += li.height;
			}
		}
		
		g.drawImage(fontTexture, 0, 0, null);
	}
	
	public static void main(String[] args) {
		new FontTest();
	}
}
