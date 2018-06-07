package core.terrain;

import javax.tools.Tool;


import org.joml.Vector2f;
import org.joml.Vector3f;

import core.buffer.PatchVBO;
import core.buffer.VBO;
import core.component.Node;
import core.kernel.Camera;
import core.kernel.Game;
import core.math.Tools;

public class TerrainNode extends Node {
	
	private Vector3f worldPosition;
	private Vector3f[] vertices;
	private Vector2f location, index;
	
	private int lod;
	private boolean isLeaf;
	private float gap;
	

	
	private TerrainConfig config;
	private PatchVBO buffer;
	
	public TerrainNode(TerrainConfig config,Vector2f index, Vector2f localOffset,float scale, int lod, PatchVBO buffer) {
		this.config = config;
		this.lod = lod;
		this.gap = scale;
		this.buffer = buffer;
		
		this.location = localOffset;
		this.index = index;
		
		worldPosition = new Vector3f(localOffset.x * config.getSize(), 0, localOffset.y * config.getSize());
		Vector3f p00 = worldPosition;
		Vector3f p01  = new Vector3f().add(worldPosition).add(0, 0, gap * config.getSize());
		Vector3f p10  = new Vector3f().add(worldPosition).add(gap * config.getSize(), 0, 0);
		Vector3f p11  = new Vector3f().add(worldPosition).add(gap * config.getSize(), 0, gap * config.getSize());
		
		vertices = new Vector3f[]{
			p00, p01, p10, p11	
		};
		
		/*worldPosition = new Vector3f(
				(localOffset.x + gap/2f ) * config.getSize() - config.getSize()/2f, 
				0, 
				(localOffset.y + gap/2f ) * config.getSize() - config.getSize()/2f);*/

		//computeWorldPos();
		setLocalTransform(Tools.getLocalTransformMatrix(new Vector3f(localOffset.x, 0, localOffset.y), new Vector3f(scale, 0, scale)));
		
		updateQuadTree();
	}
	
	public void render() {
		if (isLeaf) {
			TerrainShader.getInstance().updateUniform(this);
			buffer.draw();
		}
		
		for(Node node : getChildren()) {
			((TerrainNode) node).render();
		}
	}

	public void updateQuadTree() {
		
		updateChildNode();
		
		for(Node node : getChildren()) {
			((TerrainNode)node).updateQuadTree();
		}
	}

	private void updateChildNode() {
		boolean hasChildren = false;
		for(Vector3f vertex : vertices) {
			Vector3f dVec = new Vector3f(Camera.getCamera().getPosition());
			dVec.sub(vertex);
			float distance  = dVec.length();
			if (distance < config.getLod()[lod]) {
				hasChildren = true;
				if(getChildren().size() == 0) {
					add4children();
				}
				break;
			} 
		}
		if (!hasChildren) {
			removeChildren();
		}
	}

	private void add4children() {
		if (isLeaf) {
			isLeaf = false;
		}
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				int newLod = lod + 1;
				
				addChild(new TerrainNode(config,
										new Vector2f(j, i),
										new Vector2f(this.location.x + gap * j/2f, this.location.y + gap * i/2f),
										gap/2f,
										newLod, 
										buffer));
			}
			//new Vector2f( tSize * i * (scale/2f) + localOffset.x, tSize * j * (scale/2f) + localOffset.y)
		}
		
	}

	private void removeChildren() {
		if (!isLeaf) {
			isLeaf = true;
		}
		if (getChildren().size() != 0) {
			getChildren().clear();
		}
	}
	public void computeWorldPos(){
		
	Vector2f loc1 = new Vector2f(location);
		Vector2f loc = loc1.add(gap/2f, gap/2f).mul(config.getSize()).sub(config.getSize()/2f, config.getSize()/2f);
		
		this.worldPosition = new Vector3f(loc.x,0,loc.y);
	}
	
	//Getters and setters

	public Vector2f getIndex() {
		return index;
	}

	public int getLod() {
		return lod;
	}

	public TerrainConfig getConfig() {
		return config;
	}

	public float getGap() {
		return gap;
	}

	public Vector2f getLocation() {
		return location;
	}
	
}
