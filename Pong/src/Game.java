import graphics.nim.volterra.Canvas;
import graphics.nim.volterra.Entity;
import graphics.nim.volterra.GameState;
import graphics.nim.volterra.Resources;
import graphics.nim.volterra.Sprite;
import graphics.nim.volterra.Transform;
import graphics.nim.volterra.input.Input;
import graphics.nim.volterra.util.MathUtil;
import graphics.nim.volterra.util.Vector3f;

public class Game extends GameState {
	public static final float BALL_SPEED = 2;
	public static final float PADDLE_SPEED = 3;
	public static final float SPEED_UP = 1.5f;
	
	private Entity[] paddles = new Entity[2];
	private Entity ball;
	
	private int playerOneScore = 0;
	private int playerTwoScore = 0;

	public Game(int id) {
		super(id);
	}
	
	@Override
	public void init() {
		Resources.addTexture("Paddle", "res/Paddle.png");
		Resources.addTexture("Ball", "res/Ball.png");
		Resources.addTexture("Digits", "res/Digits.png");
		
		paddles[0] = EntityFactory.createPaddle(32, 200);
		paddles[1] = EntityFactory.createPaddle(400-32, 200);
		ball = EntityFactory.createBall(200, 200);
		
		reset();
	}
	
	public void reset() {
		Transform tb = ball.getComponent(Transform.class);
		Velocity vb = ball.getComponent(Velocity.class);
		tb.position.set(200, 200, 0);
		
		vb.velocity.set((float) Math.random() + 1, (float) Math.random() * 2 - 1);
		vb.velocity.normalise().scale(BALL_SPEED);
	}
	
	@Override
	public void update() {
		Transform tb = ball.getComponent(Transform.class);
		Transform[] tp = {paddles[0].getComponent(Transform.class), paddles[1].getComponent(Transform.class)};
		
		Velocity vb = ball.getComponent(Velocity.class);
		Velocity[] vp = {paddles[0].getComponent(Velocity.class), paddles[1].getComponent(Velocity.class)};
		
		Sprite sb = ball.getComponent(Sprite.class);
		Sprite[] sp = {paddles[0].getComponent(Sprite.class), paddles[1].getComponent(Sprite.class)};
		
		// Paddle moving
		if (Input.isKeyPressed(Input.KEY_W)) {
			vp[0].velocity.y = PADDLE_SPEED;
		}
		else if (Input.isKeyPressed(Input.KEY_S)) {
			vp[0].velocity.y = -PADDLE_SPEED;
		}
		else {
			vp[0].velocity.y = 0;
		}
		
		if (Input.isKeyPressed(Input.KEY_I)) {
			vp[1].velocity.y = PADDLE_SPEED;
		}
		else if (Input.isKeyPressed(Input.KEY_K)) {
			vp[1].velocity.y = -PADDLE_SPEED;
		}
		else {
			vp[1].velocity.y = 0;
		}
		
		// Applying velocities
		tb.position.add(new Vector3f(vb.velocity.x, vb.velocity.y, 0));
		tp[0].position.add(new Vector3f(vp[0].velocity.x, vp[0].velocity.y, 0));
		tp[1].position.add(new Vector3f(vp[1].velocity.x, vp[1].velocity.y, 0));

		// Paddle Collision
		for (int i = 0; i < paddles.length; i++) {
			float closestX = MathUtil.clamp(tb.position.x, tp[i].position.x - sp[i].getWidth()/2, tp[i].position.x + sp[i].getWidth()/2);
			float closestY = MathUtil.clamp(tb.position.y, tp[i].position.y - sp[i].getHeight()/2, tp[i].position.y + sp[i].getHeight()/2);
			
			float distanceX = tb.position.x - closestX;
			float distanceY = tb.position.y - closestY;
			
			float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
			boolean collision = distanceSquared < sb.getWidth()/2 * sb.getWidth()/2;
			
			if (collision) {
				vb.velocity.x *= -1;
				vb.velocity.scale(SPEED_UP);
			}
		}
		
		// Wall Collision
		if (tb.position.y <= 0 || tb.position.y >= 400) {
			vb.velocity.y *= -1;
		}

		// Win Condition
		if (tb.position.x >= 400) {
			playerOneScore += 1;
			reset();
		}
		if (tb.position.x <= 0) {
			playerTwoScore += 1;
			reset();
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.clear();
		canvas.draw(paddles[0]);
		canvas.draw(paddles[1]);
		
		canvas.draw(ball);

		canvas.setFont(Resources.getFont("NES"));
		canvas.drawString(Integer.toString(playerOneScore), 100, 350, 0, 1);
		canvas.drawString(Integer.toString(playerTwoScore), 300, 350, 0, 1);
	}

	@Override
	public void onStateEnter() {
		playerOneScore = 0;
		playerTwoScore = 0;
		reset();
	}

	@Override
	public void onStateLeave() {
		// TODO Auto-generated method stub
		
	}
}
