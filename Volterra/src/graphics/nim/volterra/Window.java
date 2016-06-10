package graphics.nim.volterra;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import graphics.nim.volterra.input.Input;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.opengl.GL;

public class Window {
	public static final String DEFAULT_TITLE = "Window";
	public static final int    DEFAULT_WIDTH = 640;
	public static final int    DEFAULT_HEIGHT = 480;
	
	public static String title;
	public static int width;
	public static int height;
	private long window;
	
	private KeyCallback keyCallback;
	private MouseMoveCallback mouseMoveCallback;
	private MouseClickCallback mouseClickCallback;
	
	public Window() {
		this(DEFAULT_TITLE);
	}
	
	public Window(String title) {
		this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public Window(String title, int width, int height) {
		Window.title = title;
		Window.width = width;
		Window.height = height;
		
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		
		window = glfwCreateWindow(width, height, title, 0, 0);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glfwSwapInterval(0);
		
		keyCallback = new KeyCallback();
		glfwSetKeyCallback(window, keyCallback);
		mouseMoveCallback = new MouseMoveCallback();
		glfwSetCursorPosCallback(window, mouseMoveCallback);
		mouseClickCallback = new MouseClickCallback();
		glfwSetMouseButtonCallback(window, mouseClickCallback);
	}

	public void setTitle(String title) {
		Window.title = title;
	}
	
	public void setSize(int width, int height) {
		Window.width = width;
		Window.height = height;
		glfwSetWindowSize(window, width, height);
	}
	
	public void update() {
		glfwSwapBuffers(window);
	}
	
	public void poll() {
		glfwPollEvents();
	}
	
	public boolean isClosed() {
		if (glfwWindowShouldClose(window) == GL_FALSE) {
			return false;
		}
		return true;
	}
	
	public void close() {
		glfwSetWindowShouldClose(window, GL_TRUE);
	}
	
	public void destroy() {
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	private class KeyCallback extends GLFWKeyCallback {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (action == GLFW_PRESS) {
				Input.addKeyEvent(key, true);
			}
			if (action == GLFW_RELEASE) {
				Input.addKeyEvent(key, false);
			}
		}
	}
	
	private class MouseMoveCallback extends GLFWCursorPosCallback {
		@Override
		public void invoke(long window, double x, double y) {
			Input.addMouseMoveEvent((float) x, (float) y);
		}
	}
	
	private class MouseClickCallback extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			Input.addMouseClickEvent(button, action);
		}
	}
}
