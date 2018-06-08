package core.utils;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import core.asset.Texture2D;

public class ImageLoader {
	
	public static Texture2D loadTexture(String filename, boolean mitMap) {
		int textureID = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		File file = new File(filename);
		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		
		String format = file.getName().split("\\.")[1].toLowerCase();
		
		ByteBuffer buffer = null;
		try {
			buffer = getBuffer(file, w, h, comp);
		} catch (IOException e) {e.printStackTrace();}
		
		int width = w.get(), height = h.get();
		if(format.equals("png")) {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
			
		} else if(format.equals("bmp") || format.equals("jpg") || format.equals("jpeg")) {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0,
					GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
			
		} else {
			System.err.println("Unsupported format : " + filename);
		}

		if(mitMap) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_MAX_TEXTURE_LOD_BIAS, 0.4f);
		}
		
		
		return new Texture2D(width, height, textureID);
	}
	
	private static ByteBuffer getBuffer(File file, IntBuffer... data) throws IOException {
		String format = file.getName().split("\\.")[1].toLowerCase();
		ByteBuffer buffer = null;
		if(format.equals("bmp") || format.equals("jpg") || format.equals("jpeg")) {
			buffer =  STBImage.stbi_load(file.getAbsolutePath(), data[0], data[1], data[2], 3);
		} else if (format.equals("png")) {
			buffer =  STBImage.stbi_load(file.getAbsolutePath(), data[0], data[1], data[2], 4);
		}
		
		if(buffer == null) {
		    throw new IOException(STBImage.stbi_failure_reason());
		}
		return buffer;
	}
}
