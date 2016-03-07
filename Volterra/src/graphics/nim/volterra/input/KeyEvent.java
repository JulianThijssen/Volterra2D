package graphics.nim.volterra.input;

public class KeyEvent {
	public int key;
	public boolean state;
	
	public KeyEvent(int key, boolean state) {
		this.key = key;
		this.state = state;
	}
}
