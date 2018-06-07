package core.terrain;

import java.util.ArrayList;

public class TerrainConfig {

	private int size;
	private float scaleY;
	private int[] lod;
	
	public TerrainConfig() {
		
	}

	public int getSize() {
		return size;
	}

	public float getScaleY() {
		return scaleY;
	}

	public int[] getLod() {
		return lod;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public void setLod(int[] lods) {

		this.lod = lods;

	}
	
	
	
}
