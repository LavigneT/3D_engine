package core.asset;

import java.util.Vector;

import org.joml.Vector3f;

public class GUITexture {

	private Vector3f position, scale;
	private Texture2D texture;
	
	public GUITexture(Vector3f position, Vector3f scale, Texture2D texture) {
		this.position = position;
		this.scale = scale;
		this.texture = texture;
	}
	
	
}
