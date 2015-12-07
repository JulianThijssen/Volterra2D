import graphics.nim.volterra.Canvas;
import graphics.nim.volterra.GameState;


public class Game extends GameState {
	public Game(int id) {
		super(id);
	}

	@Override
	public void init() {

	}

	@Override
	public void onStateEnter() {

	}

	@Override
	public void onStateLeave() {

	}

	@Override
	public void render(Canvas canvas) {
		canvas.clear();
		canvas.drawString("Game", 200, 200, 0, 2);
		canvas.drawRect(100, 200, 32, 32);
	}

	@Override
	public void update() {

	}
}
