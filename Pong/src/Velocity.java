import graphics.nim.volterra.Component;
import graphics.nim.volterra.util.Vector2f;

public class Velocity implements Component {
	public Vector2f velocity = new Vector2f(0, 0);
	
	public Velocity(float x, float y) {
		velocity.set(x, y);
	}
}
