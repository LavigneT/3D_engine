package core.kernel;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import core.math.Tools;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
	
	public static Camera INSTANCE = null;
	private static boolean hasMooved = false;
	
	private Vector3f position;
	private Vector2f rotation;
	
	private final static float moveSpeed = 5, rotSpeed = 1;
	
	public static Camera getCamera() {
		if (INSTANCE == null) {
			INSTANCE = new Camera();
		}
		return INSTANCE;
	}
	
	private Camera() {

	}

	public void setCamera(Vector3f position, Vector2f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public void update() {
		checkedInputs();
	}

	private void checkedInputs() {
		
		float moveX = 0, moveY = 0;
		float rotX = 0, rotY = 0;
		if(Inputs.isKeyDown(GLFW_KEY_W)) {
			moveX = -moveSpeed;
		}
		if (Inputs.isKeyDown(GLFW_KEY_S)) {
			moveX = moveSpeed;
		}
		if (Inputs.isKeyDown(GLFW_KEY_D)) {
			rotY = rotSpeed;
		}
		if (Inputs.isKeyDown(GLFW_KEY_A)) {
			rotY = -rotSpeed;
		}
		
		if (Inputs.isKeyDown(GLFW_KEY_UP)) {
			rotX = rotSpeed;
		}
		if (Inputs.isKeyDown(GLFW_KEY_DOWN)) {
			rotX = -rotSpeed;
		}
		
		if(Inputs.isKeyDown(GLFW_KEY_SPACE))
			moveY = moveSpeed;
		if(Inputs.isKeyDown(GLFW_KEY_LEFT_SHIFT))
			moveY = -moveSpeed;
		
		
		rotation.x += rotX;
		rotation.y += rotY;
		
		if (moveX != 0) {
			hasMooved = true;
		} else {
			hasMooved = false;
		}
		
		processForwardMove(moveX, moveY);
	}
	
	private void processForwardMove(float moveX, float moveY) {
		float theta = rotation.y;
		
		
		float xDist = (float) (Math.sin(Math.toRadians(theta)) * moveX);
		float zDist = (float) (Math.cos(Math.toRadians(theta)) * moveX);
		
		position.x -= xDist;
		position.y += moveY;
		position.z += zDist;
	}

	public Matrix4f getViewMatrix() {
		return Tools.getViewMatrix(position, rotation);
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector2f getRotation() {
		return rotation;
	}
	
	public static boolean hasMooved() {
		return hasMooved;
	}
	
	
}
