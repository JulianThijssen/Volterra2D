import graphics.nim.volterra.Sprite;
import graphics.nim.volterra.util.Vector2f;

public class Paddle {
	public Vector2f position;
	public Sprite sprite;
	
	public Vector2f velocity = new Vector2f(0, 0);
	
	public Paddle(Sprite sprite, int x, int y) {
		this.position = new Vector2f(x, y);
		this.sprite = sprite;
	}
}
