package core.utils;

import java.util.HashMap;

import core.asset.Texture2D;

public class TextureCache {
	
	private HashMap<String, Texture2D> textures = new HashMap<>();
	
	private static TextureCache instance = null;
	
	public static TextureCache getInstance() {
		if (instance == null) {
			instance = new TextureCache();
		}
		return instance;
	}
	
	public Texture2D getTexture(String path) {
		Texture2D texture = textures.get(path);
		
		if (texture == null) {
			texture = ImageLoader.loadTexture(path, false);
			textures.put(path, texture);
		}
		
		return texture;
	}
	
}
