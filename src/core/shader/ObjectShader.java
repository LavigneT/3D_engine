package core.shader;

import org.joml.Matrix4f;

import core.component.Sun;
import core.kernel.Camera;

public class ObjectShader extends ShaderProgram {
	
	private static ObjectShader instance = null;
	
	public static ObjectShader getInstance() {
		if(instance == null) {
			instance = new ObjectShader();
		}
		return instance;
	}
	
	public ObjectShader() {
		addVertexShader("shader/object/Object_VS");
		addFragmentShader("shader/object/Object_FS");
		
		compileShader();
	}

	@Override
	protected void getAllUniforms() {
		
		addUniform("projectionMatrix");
		addUniform("worldMatrix");
		addUniform("viewMatrix");
		
		//sun
		addUniform("sun.ambientStrengh");
		addUniform("sun.color");
		addUniform("sun.direction");
		
		addUniform("camPos");
		addUniform("textureImage");
	}
	
	public void update(Matrix4f proj, Matrix4f world, Matrix4f view) {
		loadMatrix("viewMatrix", view);
		loadMatrix("worldMatrix", world);
		loadMatrix("projectionMatrix", proj);
		
		Sun sun = Sun.getInstance();
		loadVector("sun.color", sun.getColor());
		loadVector("sun.direction", sun.getDirection());
		loadFloat("sun.ambientStrengh", sun.getAmbient());
		
		loadVector("camPos", Camera.getCamera().getPosition());
		
		loadInt("textureImage", 0);
	}

}
