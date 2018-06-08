package core.asset;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import core.math.Tools;
import core.utils.BufferUt;

public class GUITexture {

	private Vector2f position, scale;
	private Matrix4f transform;
	private Texture2D texture;
	
	public static int vaoID, vboID, iboID;
	private static boolean built = false;
	
	public GUITexture(Vector2f position, Vector2f scale, Texture2D texture) {
		this.position = position;
		this.scale = scale;
		this.texture = texture;
		
		if (!built)
			build();
		
		transform = Tools.getLocalTransformMatrix(new Vector3f(position.x, position.y, 0), new Vector3f(scale.x, scale.y, 1));
	}
	
	private void build() {
		vaoID  = GL30.glGenVertexArrays();
		vboID = GL15.glGenBuffers();
		iboID= GL15.glGenBuffers();
		
		GL30.glBindVertexArray(vaoID);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUt.createFlippedBuffer(quad), GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUt.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
		
		//position
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * 5, 0);
		
		//texture coord
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Float.BYTES * 5, Float.BYTES * 3);
		
		GL30.glBindVertexArray(0);
	}
	
	public void draw() {
		
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	
	
	public Matrix4f getTransform() {
		return transform;
	}



	private float[] quad = {
		0, 1, 1,
		0, 0,
		0, 0, 1,
		0, 1,
		1, 0, 1,
		1, 1,
		1, 1, 1,
		1, 0
	};
	
	
	private int[] indices =  {
		0, 1, 2, 2, 3, 0	
	};
}
