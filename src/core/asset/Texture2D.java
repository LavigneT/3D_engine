package core.asset;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture2D {
	
	private int width, height, id;

	public Texture2D(int width, int height, int id) {
		this.width = width;
		this.height = height;
		this.id = id;
	}
	
	
	
	
	public void bind(int offset)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + offset);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getId() {
		return id;
	}
	
	

}
