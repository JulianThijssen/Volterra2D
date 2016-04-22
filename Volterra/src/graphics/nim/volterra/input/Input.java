package graphics.nim.volterra.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import graphics.nim.volterra.util.Vector2f;

public class Input {
	public static final int KEY_SPACE     = 32;
	public static final int KEY_PERIOD    = 46;
	public static final int KEY_COMMA     = 44;
	public static final int KEY_0         = 48;
	public static final int KEY_1         = 49;
	public static final int KEY_2         = 50;
	public static final int KEY_3         = 51;
	public static final int KEY_4         = 52;
	public static final int KEY_5         = 53;
	public static final int KEY_6         = 54;
	public static final int KEY_7         = 55;
	public static final int KEY_8         = 56;
	public static final int KEY_9         = 57;
	public static final int KEY_A         = 65;
	public static final int KEY_B         = 66;
	public static final int KEY_C         = 67;
	public static final int KEY_D         = 68;
	public static final int KEY_E         = 69;
	public static final int KEY_F         = 70;
	public static final int KEY_G         = 71;
	public static final int KEY_H         = 72;
	public static final int KEY_I         = 73;
	public static final int KEY_J         = 74;
	public static final int KEY_K         = 75;
	public static final int KEY_L         = 76;
	public static final int KEY_M         = 77;
	public static final int KEY_N         = 78;
	public static final int KEY_O         = 79;
	public static final int KEY_P         = 80;
	public static final int KEY_Q         = 81;
	public static final int KEY_R         = 82;
	public static final int KEY_S         = 83;
	public static final int KEY_T         = 84;
	public static final int KEY_U         = 85;
	public static final int KEY_V         = 86;
	public static final int KEY_W         = 87;
	public static final int KEY_X         = 88;
	public static final int KEY_Y         = 89;
	public static final int KEY_Z         = 90;
	public static final int KEY_BACKSLASH = 92;
	public static final int KEY_TILDE     = 96;
	public static final int KEY_ENTER     = 257;
	public static final int KEY_TAB       = 258;
	public static final int KEY_BACKSPACE = 259;
	public static final int KEY_CAPS      = 280;
	public static final int KEY_SHIFT     = 340;
	public static final int KEY_CTRL      = 341;
	public static final int KEY_ALT       = 342;
	public static final int KEY_WIN       = 343;
	public static final int KEY_ESC       = 256;
	public static final int KEY_F1        = 290;
	public static final int KEY_F2        = 291;
	public static final int KEY_F3        = 292;
	public static final int KEY_F4        = 293;
	public static final int KEY_F5        = 294;
	public static final int KEY_F6        = 295;
	public static final int KEY_F7        = 296;
	public static final int KEY_F8        = 297;
	public static final int KEY_F9        = 298;
	public static final int KEY_F10       = 299;
	public static final int KEY_F11       = 300;
	public static final int KEY_F12       = 301;
	
	private static List<KeyListener> keyListeners = new ArrayList<KeyListener>();
	private static List<MouseMoveListener> mouseMoveListeners = new ArrayList<MouseMoveListener>();
	private static boolean keys[] = new boolean[400];
	private static Queue<KeyEvent> events = new ArrayBlockingQueue<KeyEvent>(100);
	private static Queue<MouseMoveEvent> mouseMoveEvents = new ArrayBlockingQueue<MouseMoveEvent>(100);
	
	private static Vector2f mousePos = new Vector2f(0, 0);
	
	public static boolean isKeyPressed(int key) {
		return keys[key];
	}
	
	public static boolean hasEvents() {
		return events.size() > 0;
	}
	
	public static KeyEvent getKeyEvent() {
		return events.poll();
	}
	
	public static Vector2f getMousePos() {
		return mousePos;
	}
	
	public static void addKeyEvent(int key, boolean state) {
		if (key >= 32 && key < 400) {
			events.offer(new KeyEvent(key, state));
			for (KeyListener listener: keyListeners) {
				if (state) {
					listener.keyPressed(key);
				} else {
					listener.keyReleased(key);
				}
			}
			keys[key] = state;
		}
	}
	
	public static void addMouseMoveEvent(float x, float y) {
		mousePos.set(x, y);
		mouseMoveEvents.offer(new MouseMoveEvent(x, y));
		
		for (MouseMoveListener listener: mouseMoveListeners) {
			listener.mouseMoved(x, y);
		}
	}
	
	public static void addKeyListener(KeyListener listener) {
		keyListeners.add(listener);
	}
	
	public static void addMouseMoveListener(MouseMoveListener listener) {
		mouseMoveListeners.add(listener);
	}
}
