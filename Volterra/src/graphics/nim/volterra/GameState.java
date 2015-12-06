package graphics.nim.volterra;


public abstract class GameState extends State {
	public GameState(int id) {
		super(id);
	}
	
	public abstract void init();
	
	public abstract void update();
	
	public abstract void render(Canvas canvas);
	
	public abstract void onStateEnter();
	
	public abstract void onStateLeave();
}
