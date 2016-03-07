package graphics.nim.volterra;

public abstract class GameState extends State {
	private BaseGame game;
	
	public GameState(int id, BaseGame game) {
		super(id);
		this.game = game;
	}
	
	public abstract void init();
	
	public abstract void update();
	
	public abstract void render(Canvas canvas);
	
	public abstract void onStateEnter();
	
	public abstract void onStateLeave();
	
	public void setState(int state) {
		game.setState(state);
	}
}
