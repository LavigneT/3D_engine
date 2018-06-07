package core.component;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import core.math.Tools;

public class GameObject {

	private Vector3f position, rotation, scale;
	private Model model;
	
	public GameObject(Model model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
	}
	
	public void draw() {
		model.draw();
	}
	
	public void move(float xMove, float yMove, float zMove) {
		position.x += xMove;
		position.y += yMove;
		position.z += zMove;
	}
	
	public void rotate(float rX, float rY, float rZ) {
		rotation.x += rX;
		rotation.y += rY;
		rotation.z += rZ;
	}
	
	public Matrix4f getWorldTransform() {
		return Tools.getModelMatrix(position, rotation, scale);
	}
	
}
