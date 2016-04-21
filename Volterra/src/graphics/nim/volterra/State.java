package graphics.nim.volterra;

public abstract class State {
	private int id;
	
	public State(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
