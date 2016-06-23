package graphics.nim.volterra.util;

public class Timer {
	private long startTime = 0;
	private long endTime = 0;
	
	public void start() {
		startTime = System.nanoTime();
	}
	
	public void stop() {
		endTime = System.nanoTime();
	}
	
	public long getElapsedMillis() {
		return (endTime - startTime) / 1000000;
	}
	
	public long getElapsedNano() {
		return endTime - startTime;
	}
}
