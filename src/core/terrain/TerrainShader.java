package core.terrain;

import java.text.BreakIterator;

import javax.swing.text.View;


import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import core.component.Sun;
import core.kernel.Camera;
import core.math.Tools;
import core.shader.ShaderProgram;

public class TerrainShader extends ShaderProgram {
	
	private static TerrainShader instance = null;
	
	public static TerrainShader getInstance() {
		if (instance == null) {
			instance = new TerrainShader();
		}
		return instance;
	}

	protected TerrainShader() {
		addVertexShader("shader/terrain/Terrain_VS");
		addTessellationControlShader("shader/terrain/Terrain_TC");
		addTessellationEvaluationShader("shader/terrain/Terrain_TE");
		addGeometryShader("shader/terrain/Terrain_GS");
		addFragmentShader("shader/terrain/Terrain_FS");
		
		compileShader();
	}

	@Override
	protected void getAllUniforms() {
		addUniform("index");
		addUniform("gap");
		addUniform("scaleY");
		addUniform("lod");
		addUniform("location");
		addUniform("lodMinus");
		
		addUniform("worldMatrix");
		addUniform("localMatrix");
		addUniform("projectionMatrix");
		addUniform("viewMatrix");
		addUniform("cameraPosition");
		addUniformArray("morph_area", 8);
		
		addUniform("heightMap");
		addUniform("mapTexture");
		addUniform("normalMap");
		
		addUniform("flatTerrain");
		
		
		//sun
		addUniform("sun.ambientStrengh");
		addUniform("sun.color");
		addUniform("sun.direction");
			
	}
	
	public void updateUniform(TerrainNode terrainNode) {
		
		loadVector("index", terrainNode.getIndex());
		loadFloat("gap", terrainNode.getGap());
		loadMatrix("localMatrix", terrainNode.getLocalTransform());
		loadInt("lod", terrainNode.getLod());
		loadVector("location", terrainNode.getLocation());
		
		
		if (terrainNode.getLod() != 0) {
			int lodMinus = terrainNode.getConfig().getLod()[terrainNode.getLod()-1];
			loadInt("lodMinus", lodMinus);
		}
	}
	
	public void sharedData(Terrain terrain) {
		TerrainQuadTree quadTree = terrain.getQuadTree();
		for(int i = 0; i < 8; i++) {
			if (i < quadTree.getConfig().getLod().length) {
				loadInt("morph_area" + i, quadTree.getConfig().getLod()[i]);
			}
		}
		
		loadMatrix("projectionMatrix", Tools.getProjectionMatrix());
		loadMatrix("viewMatrix", Camera.getCamera().getViewMatrix());
		loadVector("cameraPosition", Camera.getCamera().getPosition());
		loadFloat("scaleY", quadTree.getConfig().getScaleY());
		loadMatrix("worldMatrix", quadTree.getWorldMatrix());
		
		loadInt("heightMap", 0);
		loadInt("mapTexture", 1);
		loadInt("normalMap", 2);
		
		int flat = 0;
		if (terrain.isFlat()) {
			flat = 1;
		}
		loadInt("flatTerrain", flat);
		
		Sun sun = Sun.getInstance();
		loadVector("sun.color", sun.getColor());
		loadVector("sun.direction", sun.getDirection());
		loadFloat("sun.ambientStrengh", sun.getAmbient());
		
	}
	
	private boolean vertexMode = false;
	private long lastTime;
	
	public void switchVertexMode() {
		
		boolean exec =  System.currentTimeMillis() - lastTime > 1000;
		
		if (exec) {
			int shaID = shaderIDs.get(GL32.GL_GEOMETRY_SHADER);
			GL20.glDetachShader(programID, shaID);
			GL20.glDeleteShader(shaID);
			lastTime = System.currentTimeMillis();
			
			if (!vertexMode) {
				vertexMode = true;
				addGeometryShader("shader/terrain/Terrain_GS_vertices");
			} else if (vertexMode) {
				vertexMode = false;
				addGeometryShader("shader/terrain/Terrain_GS");	
			}
		}
		
		
		
		compileShader();
	}
	
}
