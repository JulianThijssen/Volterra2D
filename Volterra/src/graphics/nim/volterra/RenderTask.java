package graphics.nim.volterra;

import graphics.nim.volterra.util.Vector3f;

public class RenderTask {
	public enum TaskType {
		IMAGE, TEXT;
	}
	
	private TaskType type;
	public Sprite sprite;
	public Transform transform;
	public String text;
	public Font font;
	
	public TaskType getType() {
		return type;
	}
	
	public RenderTask(Sprite sprite, Transform transform) {
		type = TaskType.IMAGE;
		this.sprite = sprite;
		this.transform = transform;
	}
	
	public RenderTask(Font font, String text, Vector3f position, float size) {
		type = TaskType.TEXT;
		this.font = font;
		this.text = text;
		this.transform = new Transform(position.x, position.y, position.z);
		this.transform.scale.set(size, size);
	}
}
