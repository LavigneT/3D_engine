package core.shader;

import org.joml.Matrix4f;

public class GUIShader extends ShaderProgram {

	
	private static GUIShader instance = null;
	
	public static GUIShader getInstance() {
		if(instance == null) {
			instance = new GUIShader();
		}
		return instance;
	}
	
	public GUIShader() {
		addVertexShader("shader/gui/Gui_VS");
		addFragmentShader("shader/gui/Gui_FS");
		
		compileShader();
	}
	
	@Override
	protected void getAllUniforms() {
		addUniform("transform");
		addUniform("writing");
	}
	
	public void updateUniform(Matrix4f transform) {
		loadMatrix("transform", transform);
		loadInt("writing", 0);
	}

}
