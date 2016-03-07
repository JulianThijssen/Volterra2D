import graphics.nim.volterra.Canvas;
import graphics.nim.volterra.GameState;
import graphics.nim.volterra.Resources;

public class Menu extends GameState {
	public Menu(int id) {
		super(id);
	}

	@Override
	public void init() {
		
	}

	@Override
	public void render(Canvas canvas) {
		canvas.clear();
		canvas.setFontColor(1, 1, 1);
		canvas.setFont(Resources.getFont("NES"));
		canvas.drawString("Pong", 70, 230, 0, 3);
		canvas.drawString("Press any button to start..", 30, 150, 0, 0.5f);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStateEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStateLeave() {
		// TODO Auto-generated method stub
		
	}
}
