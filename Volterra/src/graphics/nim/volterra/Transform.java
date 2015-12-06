package graphics.nim.volterra;

import graphics.nim.volterra.util.Vector2f;
import graphics.nim.volterra.util.Vector3f;

public class Transform extends Component {
	public Vector3f position = new Vector3f(0, 0, 0);
	public Vector2f scale = new Vector2f(1, 1);
	public float rotation = 0;
	public float depth = 0;
	
	public Transform() {
		
	}
	
	public Transform(float x, float y, float depth) {
		this.position.set(x, y, depth);
	}
}
