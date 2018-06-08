package core.asset;

import java.util.HashMap;
import java.util.Vector;

import org.joml.Vector3f;

public class Material {
	
	private Vector3f diffuse;
	private HashMap<Integer, Texture2D> textures = new HashMap<>();
	
	public Material() {
		
	}
	
	public void setDiffuse(Vector3f diffuse) {
		this.diffuse = diffuse;
	}
	
	public void addTexture(int textureType, Texture2D texure) {
		textures.put(textureType, texure);
	}
	
	public HashMap<Integer, Texture2D> getTextures() {
		return textures;
	}
	
	public Texture2D getTexture(int textureType) {
		return textures.get(textureType);
	}

	public int bindTexture(int type) {
		Texture2D texture = textures.get(type);
		if (texture == null) {
			return 0;
		}
		return texture.getId();
	}
}
