package core.manager;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import core.asset.GUITexture;
import core.asset.Texture2D;
import core.shader.GUIShader;
import core.utils.ImageLoader;

public class GUIManager {
	
	private ArrayList<GUITexture> guis = new ArrayList<>();
	private GUIShader shader;
	
	public GUIManager() {
		init();
		shader = GUIShader.getInstance();
	}

	private void init() {
		
	}
	
	public void render() {
		shader.start();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(GUITexture gui : guis) {
			shader.updateUniform(gui.getTransform());
			gui.draw();
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.stop();
	}

	public ArrayList<GUITexture> getGuis() {
		return guis;
	}
	
	
	
}
