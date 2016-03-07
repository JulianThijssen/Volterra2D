import graphics.nim.volterra.BaseGame;
import graphics.nim.volterra.Canvas;
import graphics.nim.volterra.GameState;
import graphics.nim.volterra.Resources;
import graphics.nim.volterra.input.Input;
import graphics.nim.volterra.input.KeyListener;

public class Application extends BaseGame implements KeyListener {
	public static final int MENU = 0;
	public static final int GAME = 1;
	
	@Override
	public void init() {
		addGameState(new Menu(MENU));
		addGameState(new Game(GAME));
		
		Input.addKeyListener(this);
		setTargetFrameRate(60);
		setResolution(400, 400);
	}

	@Override
	public void load() {
		Resources.addFont("NES", "res/NESCyrillic.ttf", 60, false);
	}

	@Override
	public void update() {
		GameState state = (GameState) getCurrentState();
		
		state.update();
	}
	
	@Override
	public void render(Canvas canvas) {
		GameState state = (GameState) getCurrentState();
		
		state.render(canvas);
	}
	
	public static void main(String[] args) {
		new Application();
	}

	@Override
	public void keyPressed(int key) {
		if (getCurrentState().getId() == MENU) {
			setState(GAME);
		}
		if (getCurrentState().getId() == GAME) {
			if (key == Input.KEY_ESC) {
				setState(MENU);
			}
		}
	}

	@Override
	public void keyReleased(int key) {
		
	}
}
