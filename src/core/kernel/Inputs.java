package core.kernel;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Inputs extends GLFWKeyCallback {
	
	public static Inputs INSTANCE = null;
	private Window window;
	public static boolean[] keys = new boolean[65536], pressed = new boolean[1000];

	public static Inputs getInputs() {
		if(INSTANCE == null) {
			INSTANCE = new Inputs();
		}
		return INSTANCE;
	}
	
	private Inputs() {
			
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {

		keys[key] = action != GLFW.GLFW_RELEASE;
		pressed[key] = action == GLFW.GLFW_PRESS;
	}
	
	public static boolean isKeyDown(int keycode) {
	    return keys[keycode];
	  }
	
	public static boolean keyPress(int keycode) {
		return pressed[keycode];
	}

	public void setWindow(Window window) {
		this.window = window;
		this.window = window;
		GLFW.glfwSetKeyCallback(window.getWindowID(), this);
	}
	
	
	
}
