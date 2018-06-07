package core.component;

import org.joml.Vector3f;

public class Sun {
	
	private float ambient;
	private Vector3f direction, color;
	
	private static Sun instance= null;
	
	public static Sun getInstance() {
		if (instance == null) {
			instance = new Sun();
		}
		return instance;
	}
	
	private Sun() {
		
	}

	public Sun setSun(Vector3f direction, Vector3f color, float ambient) {
		this.ambient = ambient;
		this.direction = direction;
		this.color = color;
		return this;
	}

	public float getAmbient() {
		return ambient;
	}

	public void setAmbient(float ambient) {
		this.ambient = ambient;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
}
