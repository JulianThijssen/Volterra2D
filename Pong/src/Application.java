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
		addGameState(new Menu(MENU, this));
		addGameState(new Game(GAME, this));
		//setState(GAME);
		
		Input.addKeyListener(this);
		
		setTitle("Pong");
		setResolution(400, 400);
	}

	@Override
	public void load() {
		Resources.addFont("NES", "res/NESCyrillic.ttf", 60, false);
	}
	
	@Override
	public void update() {
		GameState currentState = (GameState) getCurrentState();
		
		currentState.update();
	}
	
	@Override
	public void render(Canvas canvas) {
		GameState currentState = (GameState) getCurrentState();
		
		currentState.render(canvas);
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
	
	public static void main(String[] args) {
		new Application();
	}
}
