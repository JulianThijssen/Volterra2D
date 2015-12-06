package graphics.nim.volterra.util;

public class Timer {
	private long startTime = 0;
	private long endTime = 0;
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		endTime = System.currentTimeMillis();
	}
	
	public long getElapsed() {
		return endTime - startTime;
	}
}
