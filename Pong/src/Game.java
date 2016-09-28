import graphics.nim.volterra.BaseGame;
import graphics.nim.volterra.Camera;
import graphics.nim.volterra.Canvas;
import graphics.nim.volterra.GameState;
import graphics.nim.volterra.Resources;
import graphics.nim.volterra.Sprite;
import graphics.nim.volterra.Window;
import graphics.nim.volterra.input.Input;
import graphics.nim.volterra.texture.Sampling;
import graphics.nim.volterra.util.MathUtil;
import graphics.nim.volterra.util.Vector2f;

public class Game extends GameState {
	public static final int PLAYERS = 2;
	
	public static final float BALL_SPEED = 2.0f;
	public static final float PADDLE_SPEED = 3.0f;
	public static final float SPEED_UP = 1.3f;
	
	public static final int PADDING = 16;
	
	private final int[] CONTROLS = {
		Input.KEY_W,
		Input.KEY_S,
		Input.KEY_I,
		Input.KEY_K
	};
	
	private Paddle[] paddles = new Paddle[PLAYERS];
	private Ball ball;
	
	private int playerOneScore = 0;
	private int playerTwoScore = 0;
	
	private Camera mainCamera = new Camera();

	public Game(int id, BaseGame game) {
		super(id, game);
	}
	
	@Override
	public void init() {
		Resources.addSpriteSheet("Paddle", "res/Paddle.png", 16, 64, Sampling.POINT);
		Resources.addSpriteSheet("BallSheet", "res/BallSheet.png", 16, 16, Sampling.POINT);

		paddles[0] = EntityFactory.createPaddle(-Window.width/2 + PADDING, 0);
		paddles[1] = EntityFactory.createPaddle(Window.width/2 - PADDING, 0);
		ball = EntityFactory.createBall(0, 0);
		
		reset();
	}
	
	public void reset() {
		ball.position.set(0, 0);
		
		double rand = Math.random() - 0.5;
		int direction = rand > 0 ? 1 : -1;
		ball.velocity.set(direction, (float) Math.random() * 2 - 1);
		ball.velocity.normalise().scale(BALL_SPEED);
	}
	
	@Override
	public void update() {
		movePaddles();
		
		// Applying velocities
		ball.position.add(ball.velocity);

		// Paddle Collision
		for (int i = 0; i < paddles.length; i++) {
			Vector2f p = paddles[i].position;
			Sprite s = paddles[i].sprite;
			float closestX = MathUtil.clamp(ball.position.x, p.x - s.getWidth()/2, p.x + s.getWidth()/2);
			float closestY = MathUtil.clamp(ball.position.y, p.y - s.getHeight()/2, p.y + s.getHeight()/2);
			
			float distanceX = ball.position.x - closestX;
			float distanceY = ball.position.y - closestY;
			
			float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
			boolean collision = distanceSquared < ball.sprite.getWidth()/2 * ball.sprite.getWidth()/2;
			
			if (collision) {
				float speed  = ball.velocity.length();
				float dir = -MathUtil.sign(ball.velocity.x);
				ball.position.add(new Vector2f(dir * 3, 0));
				ball.velocity = Vector2f.random();
				ball.velocity.add(new Vector2f(dir * 2, 0));
				ball.velocity.normalise().scale(speed);
				ball.velocity.scale(SPEED_UP);
			}
		}
		
		// Wall Collision
		if (!MathUtil.inRange(ball.position.y, -Window.height/2, Window.height/2)) {
			ball.velocity.y *= -1;
		}

		// Win Condition
		if (ball.position.x >= Window.width) {
			playerOneScore += 1;
			reset();
		}
		if (ball.position.x <= -Window.width) {
			playerTwoScore += 1;
			reset();
		}
	}
	
	public void movePaddles() {
		float upperBound = Window.height/2 - PADDING;
		float lowerBound = -Window.height/2 + PADDING;
		
		for (int i = 0; i < paddles.length; i++) {
			Vector2f p = paddles[i].position;
			Vector2f v = paddles[i].velocity;
			
			boolean upPressed = Input.isKeyPressed(CONTROLS[i*2+0]);
			boolean downPressed = Input.isKeyPressed(CONTROLS[i*2+1]);
			
			// Paddle moving
			if (upPressed && p.y < upperBound) {
				v.y = PADDLE_SPEED;
			}
			else if (downPressed && p.y > lowerBound) {
				v.y = -PADDLE_SPEED;
			}
			else {
				v.y = 0;
			}
			p.add(v);
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.setBounds(-Window.width/2, Window.width/2, -Window.height/2, Window.height/2);
		canvas.setCamera(mainCamera);
		
		canvas.clear();
		canvas.setColor(0, 1, 0, 1);
		canvas.drawImage(paddles[0].position.x, paddles[0].position.y, 0, paddles[0].sprite);
		canvas.setColor(1, 0, 0, 1);
		canvas.drawImage(paddles[1].position.x, paddles[1].position.y, 0, paddles[1].sprite);
		
		canvas.setColor(1, 1, 1, 1);
		canvas.drawImage(ball.position.x, ball.position.y, 0, ball.sprite);

		canvas.setFont(Resources.getFont("NES"));
		canvas.drawString(Integer.toString(playerOneScore), -100, 150, 0, 1);
		canvas.drawString(Integer.toString(playerTwoScore), 100, 150, 0, 1);
	}

	@Override
	public void onStateEnter() {
		playerOneScore = 0;
		playerTwoScore = 0;
		reset();
	}

	@Override
	public void onStateLeave() {
		
	}
}
