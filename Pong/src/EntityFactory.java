import graphics.nim.volterra.Resources;
import graphics.nim.volterra.Sprite;

public class EntityFactory {
	public static Paddle createPaddle(int x, int y) {
		Sprite sprite = new Sprite(Resources.getTexture("Paddle"), 16, 64, 0);
		return new Paddle(sprite, x, y);
	}
	
	public static Ball createBall(int x, int y) {
		Sprite sprite = new Sprite(Resources.getTexture("BallSheet"), 16, 16, 1);
		return new Ball(sprite, x, y);
	}
}
