package core.texture;

import java.awt.image.BufferedImage;


import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.plaf.synth.SynthStyle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class ImageLoader {
	
	public static Texture2D loadTexture(String filename, boolean mitMap) {
		int textureID = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		String tokens[] = filename.split("\\.");
		
		TextureData data = extract(filename);
		
		if (tokens[tokens.length-1].equals("png")) {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		} else {
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, data.getWidth(), data.getHeight(), 0, GL11.GL_RGB, 
					GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		if(mitMap) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_MAX_TEXTURE_LOD_BIAS, 0.4f);
		}
		
		
		return new Texture2D(data.getWidth(), data.getHeight(), textureID);
	}
	
	public Texture2D loadTexture2(String filename, boolean mitMap, int offset) {
		int textureID = GL11.glGenTextures();
		
		//GL13.glActiveTexture(GL13.GL_TEXTURE0 + offset);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		TextureData dataObj = extract(filename);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, dataObj.getWidth(), dataObj.getHeight(), 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, dataObj.getBuffer());
		
		if(mitMap) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_MAX_TEXTURE_LOD_BIAS, 0.4f);
		}
		
		
		return new Texture2D(dataObj.getWidth(), dataObj.getHeight(), textureID);
	}

	/**
	 * Load PNG Image in a ByteBuffer, return a TextureData
	 * @param fileName
	 * @return
	 */
	public static TextureData extract(String fileName) {
		InputStream is;
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			is = new FileInputStream("res/" + fileName + ".png");
			PNGDecoder d = new PNGDecoder(is);
			
			width = d.getWidth();
			height = d.getHeight();
			
			buffer = ByteBuffer.allocateDirect(4 * width  * height);
			d.decode(buffer, d.getHeight()*4, Format.RGBA);
			buffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new TextureData(width, height, buffer);
	}
	

public static class TextureData {

	private final int WIDTH, HEIGHT;
	private final ByteBuffer buffer;
	public TextureData(int width, int height, ByteBuffer buffer) {
		WIDTH = width;
		HEIGHT = height;
		this.buffer = buffer;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	public int getHeight() {
		return HEIGHT;
	}
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	
	
}
}
