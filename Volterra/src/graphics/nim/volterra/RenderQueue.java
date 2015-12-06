package graphics.nim.volterra;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RenderQueue {
	private Queue<RenderTask> tasks = new LinkedBlockingQueue<RenderTask>();
	
	public void add(RenderTask task) {
		tasks.offer(task);
	}
	
	public boolean isEmpty() {
		return tasks.isEmpty();
	}
	
	public RenderTask pop() {
		return tasks.poll();
	}
}
