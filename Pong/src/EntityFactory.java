import graphics.nim.volterra.Resources;
import graphics.nim.volterra.Sprite;

public class EntityFactory {
	public static Paddle createPaddle(int x, int y) {
		Sprite sprite = new Sprite(Resources.getSpriteSheet("Paddle"), 0, true);
		return new Paddle(sprite, x, y);
	}
	
	public static Ball createBall(int x, int y) {
		Sprite sprite = new Sprite(Resources.getSpriteSheet("BallSheet"), 1.0f, true);
		return new Ball(sprite, x, y);
	}
}
