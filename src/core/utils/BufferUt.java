package core.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class BufferUt {
	public static FloatBuffer createFlippedBuffer(Vector2f[] vector)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vector.length * 2);
		
		for (int i = 0; i < vector.length; i++)
		{
			buffer.put(vector[i].x());
			buffer.put(vector[i].y());	
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static IntBuffer createFlippedBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
