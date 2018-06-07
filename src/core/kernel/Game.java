package core.kernel;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Game {
	
	private Window window;
	private RenderEngine renderEngine;
	private Camera camera;
	
	
	public void init() {
		window = Window.getWindow();
		Inputs.getInputs().setWindow(window);
		
		camera = Camera.getCamera();
		camera.setCamera(new Vector3f(0, 10, 0), new Vector2f(0, 0));
		
		renderEngine = new RenderEngine();
		if (enableCount) {
			System.out.println(count);
			count= 0;
		}
	}

	public static int count = 0;
	public static boolean enableCount = false;

	public void start() {
		long lastTime = System.currentTimeMillis();

		while (!glfwWindowShouldClose(Window.getWindow().getWindowID())) {
			
			update();
			
			render();

			glfwSwapBuffers(Window.getWindow().getWindowID());
			glfwPollEvents();
			lastTime = System.currentTimeMillis();
			
			if (enableCount) {
				System.out.println(count);
				count= 0;
			}
		}
		
		cleanUp();
	}

	private void render() {
		renderEngine.render();
		
	}

	private void update() {
		camera.update();
		renderEngine.update();
	}

	private void cleanUp() {
		renderEngine.cleanUp();
		GLFW.glfwDestroyWindow(window.getWindowID());
		GLFW.glfwTerminate();
	}
}
