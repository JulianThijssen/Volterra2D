import graphics.nim.volterra.Entity;
import graphics.nim.volterra.Resources;
import graphics.nim.volterra.Sprite;
import graphics.nim.volterra.Transform;

public class EntityFactory {
	public static Entity createPaddle(int x, int y) {
		Entity e = new Entity();
		e.addComponent(new Transform(x, y, 0));
		e.addComponent(new Velocity(0, 0));
		e.addComponent(new Sprite(Resources.getTexture("Paddle")));
		
		return e;
	}
	
	public static Entity createBall(int x, int y) {
		Entity e = new Entity();
		e.addComponent(new Transform(x, y, 0));
		e.addComponent(new Velocity(0, 0));
		e.addComponent(new Sprite(Resources.getTexture("BallSheet"), 16, 16, 1));
		
		return e;
	}
}
