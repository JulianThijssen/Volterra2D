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
	public void onStateEnter() {

	}

	@Override
	public void onStateLeave() {

	}

	@Override
	public void update() {

	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.clear();
		canvas.setFont(Resources.getFont("NES"));
		canvas.drawString("Hello", 200, 200, 0, 1);
	}
}
