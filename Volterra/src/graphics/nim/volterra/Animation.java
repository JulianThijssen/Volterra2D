package graphics.nim.volterra;

public class Animation {
	private float length = 1;
	
	private float time = 0;
	
	private boolean playing = false;
	private boolean looping = false;
	private boolean finished = false;
	
	public Animation(float clipLength, boolean looping) {
		this.length = clipLength;
		this.looping = looping;
	}
	
	public void setLooping(boolean looping) {
		this.looping = looping;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void setLength(float length) {
		this.length = length;
		
		// Reset time back to 0 to prevent bugs in update()
		time = 0;
	}
	
	public float progress() {
		return time / length;
	}
	
	public void play() {
		if (playing) {
			return;
		}
		time = 0;
		finished = false;
		playing = true;
	}
	
	public void stop() {
		playing = false;
		finished = true;
	}
	
	public void update() {
		if (playing) { // FIXME This check is currently double with the one in Sprite
			time += Time.renderTime;
			
			if (time > length) {
				time = length - 0.001f;
				
				if (looping) {
					stop();
					play();
				} else {
					stop();
				}
			}
		}
	}
}
