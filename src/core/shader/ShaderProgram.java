package core.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;



public abstract class ShaderProgram {
	
	protected HashMap<Integer, Integer> shaderIDs = new HashMap<>();
	protected int programID;	
	private HashMap<String, Integer> uniforms = new HashMap<>();
	
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram() {
		programID = GL20.glCreateProgram();
		
	}
	
	//private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	//****************** ADD SHADER *************************************************
	
	public void addVertexShader(String file) {
		addProgram(file, GL20.GL_VERTEX_SHADER);
	}
	
	public void addFragmentShader(String file) {
		addProgram(file, GL20.GL_FRAGMENT_SHADER);
	}
	
	public void addGeometryShader(String file) {
		addProgram(file, GL32.GL_GEOMETRY_SHADER);
	}
	
	public void addTessellationControlShader(String file)
	{
		addProgram(file, GL40.GL_TESS_CONTROL_SHADER);
	}
	
	public void addTessellationEvaluationShader(String file)
	{
		addProgram(file, GL40.GL_TESS_EVALUATION_SHADER);
	}
	
	public void addProgram(String file, int type) {
		int shaderID = loadShader(file, type);
		shaderIDs.put(type, shaderID);
		GL20.glAttachShader(programID, shaderID);
		
	}
	
	
	//****************** BINDING DATA - ATTRIBLIST / UNIFORMS ************************
	
	protected abstract void getAllUniforms();
	
	public void addUniform(String name) {
		uniforms.put(name, GL20.glGetUniformLocation(programID, name));
	}
	
	public void addUniformArray(String name, int number) {
		
		for(int i = 0;i < number; i++) {
			uniforms.put(name + String.valueOf(i), GL20.glGetUniformLocation(programID, name + "[" + i + "]"));
		}
	}
	
	
	//**************** LOADING DATA INTO UNIFORM ********************************
	
	
	protected void loadFloat(String name, float number) {
		GL20.glUniform1f(uniforms.get(name), number);
	}
	
	
	protected void loadVector(String name, Vector3f v) {
		if(v != null) {
			GL20.glUniform3f(uniforms.get(name), v.x, v.y, v.z);
		}
	}
	
	protected void loadVector(String name, Vector2f v) {
		if(v != null) {
			GL20.glUniform2f(uniforms.get(name), v.x, v.y);
		}
	}
	
	protected void loadInt(String name, int value) {
		GL20.glUniform1i(uniforms.get(name), value);
	}
	
	protected void loadMatrix(String name, Matrix4f value) {
		
		
		value.get(buffer);
		GL20.glUniformMatrix4fv(uniforms.get(name), false, buffer);
	}
	
	//*************************** 
	
	protected int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(programID, name);
	}
	
	/**
	 * Permet de faire le lien entre les attribList et les variables définient dans les shader
	 * @param attribID  ID de l'attribList
	 * @param variableName	Nom de la variable dans le shader
	 */
	protected void bindAttribList(int attribID, String variableName) {
		GL20.glBindAttribLocation(programID, attribID, variableName);
	}
	
	public void clean() {
		for(int shaID : shaderIDs.keySet()) {
			GL20.glDetachShader(programID, shaID);
			GL20.glDeleteShader(shaID);
		}
		
		GL20.glDeleteProgram(programID);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	//************* COMPILE *******************
	
	public void compileShader()
	{
		GL20.glLinkProgram(programID);

		if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0)
		{
			System.out.println(this.getClass().getName() + " " + GL20.glGetProgramInfoLog(programID, 1024));
			System.exit(1);
		}
		
		GL20.glValidateProgram(programID);
		
		if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0)
		{
			System.err.println(this.getClass().getName() +  " " + GL20.glGetProgramInfoLog(programID, 1024));
			System.exit(1);
		}
		
		
		getAllUniforms();
	}

	
	private int loadShader(String file, int type) {
		 StringBuilder shaderSource = new StringBuilder();
	        try{
	            BufferedReader reader = new BufferedReader(new FileReader(file + ".glsl"));
	            String line;
	            while((line = reader.readLine())!=null){
	                shaderSource.append(line).append("//\n");
	            }
	            reader.close();
	        }catch(IOException e){
	            e.printStackTrace();
	            System.exit(-1);
	        }
	        int shaderID = GL20.glCreateShader(type);
	        GL20.glShaderSource(shaderID, shaderSource);
	        GL20.glCompileShader(shaderID);
	        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
	            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
	            System.err.println("Could not compile shader!");
	            System.exit(-1);
	        }
	        return shaderID;
	}
	
}

