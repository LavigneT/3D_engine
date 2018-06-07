package core.math;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import core.kernel.Window;

public class Tools {
	
	public static final float FOV = 70, Z_NEAR = 0.1f, Z_FAR = 6000;

	public static Matrix4f getModelMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
		
		Matrix4f model = new Matrix4f();
		model.scale(scale);
		model.translate(translation);
		model.
		rotateX((float) Math.toRadians(rotation.x)).
		rotateY((float) Math.toRadians(rotation.y)).
		rotateZ((float) Math.toRadians(rotation.z));
		
		return model;
	}
	
	public static Matrix4f getLocalTransformMatrix(Vector3f translation, Vector3f scale) {
		
		Matrix4f model = new Matrix4f();
		
		
		model.translate(translation);
		model.scale(new Vector3f(scale.x,scale.y, scale.z));
		
		return model;
	}
	
	public static Matrix4f getViewMatrix(Vector3f translation, Vector2f rotation) {
		
		Matrix4f model = new Matrix4f();
		
		model.
		rotateX((float) Math.toRadians(rotation.x)).
		rotateY((float) Math.toRadians(rotation.y)).
		rotateZ((float) Math.toRadians(0));
		
		model.translate(-translation.x, -translation.y, -translation.z);
		
		return model;
	}
	public static Matrix4f getProjectionMatrix() {
		Matrix4f projection = new Matrix4f();
		float aspectRatio = (float) Window.getAspectRatio();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale /aspectRatio;
		float frustum_length = Z_FAR - Z_NEAR;
		
		projection = new Matrix4f();
		projection._m00(x_scale);
		projection.m11(y_scale);
		projection.m22(-((Z_FAR + Z_NEAR) / frustum_length));
		projection.m23(-1);
		projection.m32(-((2 * Z_NEAR * Z_FAR) / frustum_length));
		projection.m33(0);
		return projection;
	}
	
	private void getProj() {
		//projection.perspective((float) Math.toRadians(FOV), Window.getAspectRatio(), Z_NEAR, Z_FAR);
	}
	
}
