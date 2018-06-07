package core.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import core.texture.Texture2D;

public class ImageLoader2 {
	
	public static ByteBuffer loadImage2(String path) {
		
		try {
			BufferedImage image = ImageIO.read(new File(path));
			byte[] array = ((DataBufferByte)image.getData().getDataBuffer()).getData();
			ByteBuffer buffer = ByteBuffer.wrap(array);
			buffer.flip();
			return buffer;
		} catch (IOException e) {e.printStackTrace();}
		return null;
	}
	

	public static Texture2D loadTexture(String file) {
		
		ByteBuffer imageBuffer;
        try {
            imageBuffer = ioResourceToByteBuffer(file, 128 * 128);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);

        // Use info to read image metadata without decoding the entire image.
        if (!STBImage.stbi_info_from_memory(imageBuffer, w, h, c)) {
            throw new RuntimeException("Failed to read image information: " + STBImage.stbi_failure_reason());
        }
        
        // Decode the image
        ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, w, h, c, 0);
        if (image == null) {
            throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
        }
       
        int width = w.get(0);
        int height = h.get(0);
        int comp = c.get(0);
        
        int texId = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        
        if (comp == 3) {
            if ((width & 3) != 0) {
            	GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 2 - (width & 1));
            }
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
        } else {
        	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        }
        
        STBImage.stbi_image_free(image);
        
        int[] data = {texId, w.get(), h.get()};
        
		return new Texture2D(width, height, texId);
	}
	
	public static ByteBuffer loadImageToByteBuffer(String file){
		ByteBuffer imageBuffer;
        try {
            imageBuffer = ioResourceToByteBuffer(file, 128 * 128);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        IntBuffer w    = BufferUtils.createIntBuffer(1);
        IntBuffer h    = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);

        // Use info to read image metadata without decoding the entire image.
        if (!STBImage.stbi_info_from_memory(imageBuffer, w, h, c)) {
            throw new RuntimeException("Failed to read image information: " + STBImage.stbi_failure_reason());
        }
  
//        System.out.println("Image width: " + w.get(0));
//        System.out.println("Image height: " + h.get(0));
//        System.out.println("Image components: " + c.get(0));
//        System.out.println("Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

        // Decode the image
        ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, w, h, c, 0);
        if (image == null) {
            throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
        }
        
        return image;
	}
	
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
                while (fc.read(buffer) != -1) {
                    ;
                }
            }
        } else {
            try (
                    InputStream source = Thread.class.getClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
                ) {
                    buffer = BufferUtils.createByteBuffer(bufferSize);

                    while (true) {
                        int bytes = rbc.read(buffer);
                        if (bytes == -1) {
                            break;
                        }
                        if (buffer.remaining() == 0) {
                            buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                        }
                    }
                }
            }

            buffer.flip();
            return buffer;
    }
	
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
	}
}
