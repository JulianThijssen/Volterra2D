package graphics.nim.volterra;

public abstract class BaseGame {
	private Window window;
	private Scene scene;
	private Canvas canvas;
	
	private long maxSkip = 15;
	private long skipTime;
	private long updateTime = 0;
	private long renderTime = 0;
	
	private StateMachine gameState = new StateMachine();
	
	/* Window */
	private String title = "Volterra Game";
	private int width = 640;
	private int height = 480;
	// TODO private boolean fullscreen = false;
	private int frameRate = 60;
	
	public BaseGame() {
		start();
	}
	
	private void start() {
		init();
		
		window = new Window(title, width, height);
		canvas = new Canvas();
		skipTime = (long) 1e9 / frameRate;
		
		load();
		tick();
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	private void tick() {
		for (State s: gameState.getStates()) {
			GameState state = (GameState) s;
			state.init();
		}
		
		long nextUpdate = System.nanoTime();
		updateTime = System.nanoTime();
		renderTime = System.nanoTime();
		
		while (!window.isClosed()) {
			window.poll();
			
			int skipped = 0;
			
			while (System.nanoTime() > nextUpdate  && skipped < maxSkip) {
				Time.deltaTime = (System.nanoTime() - updateTime) / 1000000000.0f;
				updateTime = System.nanoTime();
				
				update();
				nextUpdate += skipTime;
				skipped++;
			}
			
			Time.renderTime = (System.nanoTime() - renderTime) / 1000000000.0f;
			renderTime = System.nanoTime();

			render(canvas);
			
			window.update();
		}
	}
	
	public abstract void init();
	
	public abstract void load();
	
	public abstract void update();
	
	public abstract void render(Canvas canvas);
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setResolution(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setTargetFrameRate(int rate) {
		if (rate > 0) {
			frameRate = rate;
		}
	}
	
	public void addGameState(State state) {
		gameState.addState(state);
	}
	
	public State getCurrentState() {
		return gameState.getState();
	}
	
	public void setState(int state) {
		gameState.setState(state);
	}
	
	public void takeScreenshot() {
		canvas.takeScreenshot();
	}
}
