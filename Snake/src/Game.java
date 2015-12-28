import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import graphics.nim.volterra.Canvas;
import graphics.nim.volterra.Entity;
import graphics.nim.volterra.GameState;
import graphics.nim.volterra.Time;
import graphics.nim.volterra.Transform;
import graphics.nim.volterra.Window;
import graphics.nim.volterra.input.Input;
import graphics.nim.volterra.util.Vector2f;
import graphics.nim.volterra.util.Vector3f;

public class Game extends GameState {
	public static final float INITIAL_TAIL = 0.5f;
	
	private List<Entity> snakeTail = new ArrayList<Entity>();
	private List<Entity> apples = new ArrayList<Entity>();
	
	private Vector2f spawn = new Vector2f(200, 200);
	private Vector2f velocity = new Vector2f(3, 0);
	
	private int appleCount = 0;
	
	private float appleSpawn = 0;
	
	public Game(int id) {
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
		// Snake movement
		if (Input.isKeyPressed(Input.KEY_W) && velocity.y >= 0) {
			velocity.set(0, 3);
		}
		else if (Input.isKeyPressed(Input.KEY_S) && velocity.y <= 0) {
			velocity.set(0, -3);
		}
		else if (Input.isKeyPressed(Input.KEY_D) && velocity.x >= 0) {
			velocity.set(3, 0);
		}
		else if (Input.isKeyPressed(Input.KEY_A) && velocity.x <= 0) {
			velocity.set(-3, 0);
		}
		spawn.add(velocity);
		
		// Boundary wrap
		if (spawn.x > Window.width) {
			spawn.x = 0;
		}
		if (spawn.x < 0) {
			spawn.x = Window.width;
		}
		if (spawn.y > Window.height) {
			spawn.y = 0;
		}
		if (spawn.y < 0) {
			spawn.y = Window.height;
		}
		
		// Spawn new snake tails
		Entity newe = new Entity();
		newe.addComponent(new Transform(spawn.x, spawn.y, 0));
		newe.addComponent(new Destroy(INITIAL_TAIL + appleCount / 10.0f));
		snakeTail.add(newe);
		
		// Delete old snake tails
		Iterator<Entity> it = snakeTail.iterator();
		
		while (it.hasNext()) {
			Entity e = it.next();
			Destroy d = e.getComponent(Destroy.class);
			
			d.time -= Time.deltaTime;
			
			if (d.time < 0) {
				it.remove();
			}
		}
		
		// Spawn new apples
		appleSpawn += Time.deltaTime;
		
		if (appleSpawn > 1) {
			Entity apple = new Entity();
			apple.addComponent(new Transform((float) Math.random() * Window.width,
											 (float) Math.random() * Window.height, 0));
			apples.add(apple);
			appleSpawn = 0;
		}
		
		// Eat the apples!
		Iterator<Entity> ait = apples.iterator();
		
		while (ait.hasNext()) {
			Entity apple = ait.next();
			
			Transform t = apple.getComponent(Transform.class);
			
			if (Vector2f.distance(t.position, spawn) < 16) {
				ait.remove();
				appleCount++;
			}
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.setBounds(0, Window.width, 0, Window.height);
		//canvas.setCamera(camera);
		
		canvas.clear();
		canvas.drawString("Game", 100, 200, 0, 2);
		
		// Draw apples
		for (Entity apple: apples) {
			Transform t = apple.getComponent(Transform.class);
			
			canvas.setColor(1, 0, 0);
			canvas.drawRect(t.position.x, t.position.y, 16, 16);
		}
		
		// Draw snake
		for (Entity e: snakeTail) {
			Transform t = e.getComponent(Transform.class);

			canvas.setColor(1, 1, 1);
			canvas.drawRect(t.position.x, t.position.y, 16, 16);
		}
	}
}
