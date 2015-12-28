import graphics.nim.volterra.Canvas;
import graphics.nim.volterra.GameState;
import graphics.nim.volterra.Resources;
import graphics.nim.volterra.Window;

public class Menu extends GameState {
	public Menu(int id) {
		super(id);
	}

	@Override
	public void init() {
		camera.setPosition(Window.width/2, Window.height/2);
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
		canvas.setBounds(0, Window.width, 0, Window.height);
		//canvas.setCamera(camera);
		
		canvas.clear();
		canvas.setFont(Resources.getFont("NES"));
		canvas.setFontColor((float) Math.random(), (float) Math.random(), (float) Math.random());
		canvas.drawString("Snake", 80, 200, 0, 2);
	}
}
