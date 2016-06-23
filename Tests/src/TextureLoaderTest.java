import static org.junit.Assert.assertTrue;

import org.junit.Test;

import graphics.nim.volterra.Texture;
import graphics.nim.volterra.TextureLoader;
import graphics.nim.volterra.Window;

public class TextureLoaderTest {

	@Test
	public void test() {
		Window window = new Window();
		
		Texture tex = TextureLoader.loadSTB("res/Test_sPNG.png");
		assertTrue(tex.getWidth() > 0);
		assertTrue(tex.getHeight() > 0);
	}
}
