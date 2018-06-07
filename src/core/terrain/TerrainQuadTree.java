package core.terrain;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import core.buffer.PatchVBO;
import core.component.Node;
import core.math.Tools;

public class TerrainQuadTree extends Node {
	
	private Matrix4f worldMatrix;
	private TerrainConfig config;
	
	public static int rootNodes = 8;
	
	public TerrainQuadTree(TerrainConfig config) {
		this.config = config;
		
		PatchVBO buffer = new PatchVBO();
		buffer.allocate(generatePatch(), 16);
		
		worldMatrix = Tools.getLocalTransformMatrix(new Vector3f(0, 0, 0), new Vector3f(config.getSize(), 0, config.getSize()));
		
		for(int i = 0; i < rootNodes; i++) {
			for(int j = 0; j < rootNodes; j++) {
				addChild(new TerrainNode(config, new Vector2f(j, i), new Vector2f(j/(float)rootNodes, i/(float)rootNodes), 1/(float)rootNodes,0, buffer));
			}
		}
	}
	
	public Vector2f[] generatePatch(){
		
		// 16 vertices for each patch
		Vector2f[] vertices = new Vector2f[16];
		
		int index = 0;
		
		vertices[index++] = new Vector2f(0,0);
		vertices[index++] = new Vector2f(0.333f,0);
		vertices[index++] = new Vector2f(0.666f,0);
		vertices[index++] = new Vector2f(1,0);
		
		vertices[index++] = new Vector2f(0,0.333f);
		vertices[index++] = new Vector2f(0.333f,0.333f);
		vertices[index++] = new Vector2f(0.666f,0.333f);
		vertices[index++] = new Vector2f(1,0.333f);
		
		vertices[index++] = new Vector2f(0,0.666f);
		vertices[index++] = new Vector2f(0.333f,0.666f);
		vertices[index++] = new Vector2f(0.666f,0.666f);
		vertices[index++] = new Vector2f(1,0.666f);
	
		vertices[index++] = new Vector2f(0,1);
		vertices[index++] = new Vector2f(0.333f,1);
		vertices[index++] = new Vector2f(0.666f,1);
		vertices[index++] = new Vector2f(1,1);
		
		return vertices;
	}

	public void render() {
		for(Node node : getChildren()) {
			((TerrainNode) node).render();
		}
	}
	
	public void updateQuadTree() {
		for(Node node : getChildren()) {
			((TerrainNode) node).updateQuadTree();
		}
	}

	public TerrainConfig getConfig() {
		return config;
	}

	public void setConfig(TerrainConfig config) {
		this.config = config;
	}

	public Matrix4f getWorldMatrix() {
		return worldMatrix;
	}
	
}
