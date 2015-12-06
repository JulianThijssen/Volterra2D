import graphics.nim.volterra.Sprite;
import graphics.nim.volterra.util.Vector2f;

public class GameObject {
	public Sprite sprite;
	public Vector2f position = new Vector2f(0, 0);
	
	public GameObject(Sprite sprite, int x, int y) {
		this.sprite = sprite;
		position.set(x, y);
	}
}
