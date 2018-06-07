package core.kernel;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import core.buffer.Mesh;
import core.component.Model;
import core.component.ObjectManager;
import core.component.Sun;
import core.math.Tools;
import core.shader.ObjectShader;
import core.terrain.Terrain;
import core.terrain.TerrainShader;

public class RenderEngine {

	private Terrain terrain;
	private ObjectManager manager;
	private Sun sun;
	
	public RenderEngine() {
		terrain = new Terrain();
		manager = new ObjectManager();
		sun = Sun.getInstance().setSun(new Vector3f(0, -1, 1), new Vector3f(1, 1, 1), 0.3f);
		init();
	}
	

	
	public void render() {
		
		prepare();
		terrain.render();
		manager.render();
		
	}
	
	



	public void update() {
		terrain.update();
	}
	
	private void prepare() {
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void init() {
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_DEPTH_TEST);     	
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
	}
	
	

	public void cleanUp() {
		TerrainShader.getInstance().clean();
		
	}



	
	
}
