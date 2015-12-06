package graphics.nim.volterra;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
	private List<State> states = new ArrayList<State>();
	private State currentState;
	
	public void addState(State state) {
		if (currentState == null) {
			currentState = state;
		}
		states.add(state);
	}
	
	public State getState() {
		return currentState;
	}
	
	public List<State> getStates() {
		return states;
	}
	
	public void setState(int state) {
		for (State s: states) {
			if (s.getId() == state) {
				currentState = s;
				return;
			}
		}
	}
}
