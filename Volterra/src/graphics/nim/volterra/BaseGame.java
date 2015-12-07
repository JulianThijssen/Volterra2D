package graphics.nim.volterra;

public abstract class BaseGame {
	private Window window;
	private Scene scene;
	private Canvas canvas;
	
	private long maxSkip = 15;
	private long skipTime;
	private long time = 0;
	
	private boolean started = false;
	
	private StateMachine gameState = new StateMachine();
	
	/* Window */
	private String title = "Volterra Game";
	private int width = 640;
	private int height = 480;
	// TODO private boolean fullscreen = false;
	private int frameRate = 60;
	
	public BaseGame() {
		init();
		start();
	}
	
	private void start() {
		window = new Window(title, width, height);
		canvas = new Canvas();
		skipTime = (long) 1e9 / frameRate;
		started = true;
		
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
		time = System.currentTimeMillis();
		
		while (!window.isClosed()) {
			window.poll();
			
			int skipped = 0;
			
			while (System.nanoTime() > nextUpdate  && skipped < maxSkip) {
				Time.deltaTime = (System.currentTimeMillis() - time) / 1000.0f;
				time = System.currentTimeMillis();
				
				update();
				nextUpdate += skipTime;
				skipped++;
			}
			
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
}
