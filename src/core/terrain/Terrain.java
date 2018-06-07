package core.terrain;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import core.kernel.Inputs;
import core.texture.Texture2D;
import core.utils.ImageLoader;

public class Terrain {
	
	private TerrainConfig config = new TerrainConfig();
	private Texture2D heightMap, texture, normalMap;
	private final static String BASE_PATH = "res/settings/terrain/";
	
	private TerrainQuadTree quadTree;
	private TerrainShader shader;
	private int[] lodsArr;
	private boolean flat = false;;
	
	public Terrain() {
		readConfig("default", config);
		quadTree = new TerrainQuadTree(config);
		shader = TerrainShader.getInstance();
		
		this.heightMap = ImageLoader.loadTexture("res/heightmap/island.png", false);
		this.texture = ImageLoader.loadTexture("res/heightmap/islandTex.jpg", false);
		this.normalMap = ImageLoader.loadTexture("res/heightmap/normal.png", false);
	}
	
	public Terrain(String file, String heightMap) {
		readConfig(file, config);
	}
	
	public void render() {
		shader.start();
		shader.sharedData(this);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, heightMap.getId());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap.getId());
		
		quadTree.render();
		shader.stop();
	}

	
	private long lastFlatChange;
	public void update() {
		quadTree.updateQuadTree();
		
		
		if(Inputs.isKeyDown(GLFW.GLFW_KEY_X)) {
			shader.start();
			shader.switchVertexMode();
			shader.stop();
		}
		
		if (Inputs.isKeyDown(GLFW.GLFW_KEY_C)) {
			if (System.currentTimeMillis() - lastFlatChange > 1000) {
				lastFlatChange = System.currentTimeMillis();
				
				flat = !flat;
			}
		}
	}

	private void readConfig(String path, TerrainConfig config) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(BASE_PATH + path + ".txt"));
			
			String line;
			ArrayList<Integer> lods = new ArrayList<>(); 
			while((line = reader.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				
				String tokens[] = line.split(" ");
				
				switch(tokens[0]) {
					
					case "size" :
						config.setSize(Integer.valueOf(tokens[1]));
						break;
					case "scaleY" : 
						config.setScaleY(Float.valueOf(tokens[1]));
						break;
					case "lod" : 
						lods.add(Integer.valueOf(tokens[1]));
						break;
				}
			}
			
			lodsArr = new int[lods.size()];
			for(int i = 0; i < lods.size(); i++) {
				lodsArr[i] = new Integer(lods.get(i));
			}
			config.setLod(lodsArr);
			
		} catch (IOException e) {e.printStackTrace();}
	}

	public TerrainConfig getConfig() {
		return config;
	}

	public TerrainQuadTree getQuadTree() {
		return quadTree;
	}

	public boolean isFlat() {
		return flat;
	}
	
	

}
