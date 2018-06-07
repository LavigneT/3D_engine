package core.kernel;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public class Window {
	
	public static Window instance = null;
	
	private GLFWErrorCallback callback;
	private long windowID;
	
	public static int WIDTH = 800, HEIGHT = 800;
	
	private Window() {
		initWindow();
	}
	
	public static Window getWindow() {
		if(instance == null) {
			instance = new Window();
		}
		return instance;
	}
	
	private void initWindow() {
		callback = GLFWErrorCallback.createPrint(System.err);
		glfwSetErrorCallback(callback);
		
		boolean result = glfwInit();
		
		if(!result) {
			throw new IllegalStateException("Fail");
		}
		
		windowID = glfwCreateWindow(WIDTH, HEIGHT, "title", MemoryUtil.NULL,  MemoryUtil.NULL);
		
		if(windowID == MemoryUtil.NULL) {
			throw new IllegalStateException("fail");
		}
		
		glfwMakeContextCurrent(windowID);
		glfwSwapInterval(1);
		
		glfwShowWindow(windowID);
		
		GL.createCapabilities();
	}

	public long getWindowID() {
		return windowID;
	}
	
	public static float getAspectRatio() {
		return WIDTH / HEIGHT;
	}

}
