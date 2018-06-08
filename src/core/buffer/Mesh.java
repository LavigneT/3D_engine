package core.buffer;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import core.asset.Material;
import core.asset.Texture2D;
import core.utils.BufferUt;

public class Mesh implements VBO {
	
	private int vaoID, vboID, iboID, size;
	private Material material;

	public Mesh(float[] data, int[] indices, Material material) {
		this.size = indices.length;
		this.material = material;
		
		build(data, indices);
	}
	
	public void build(float[] data, int[] indices) {
		vaoID = GL30.glGenVertexArrays();
		vboID = GL15.glGenBuffers();
		iboID = GL15.glGenBuffers();
		
		GL30.glBindVertexArray(vaoID);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUt.createFlippedBuffer(data), GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUt.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
		
		//vertex positions
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * 8, 0);
				
		//normals
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, Float.BYTES * 8, Float.BYTES * 3);
						
		//textures coordinates
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, Float.BYTES * 8, Float.BYTES * 6);
		
		GL30.glBindVertexArray(0);
		
	}

	@Override
	public void draw() {
		GL30.glBindVertexArray(vaoID);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		//texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.bindTexture(Assimp.aiTextureType_DIFFUSE));
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, size, GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0);
	}

	@Override
	public void delete() {
		glBindVertexArray(vaoID);
		glDeleteBuffers(vboID);
		glDeleteVertexArrays(vaoID);
		glBindVertexArray(0);
	}

	
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
	

}
