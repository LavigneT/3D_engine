package core.kernel;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import core.asset.GUITexture;
import core.component.Sun;
import core.manager.GUIManager;
import core.manager.ObjectManager;
import core.terrain.Terrain;
import core.terrain.TerrainShader;
import core.utils.ImageLoader;

public class RenderEngine {

	private Terrain terrain;
	private ObjectManager manager;
	private Sun sun;
	private GUIManager guiManager;
	
	public RenderEngine() {
		terrain = new Terrain();
		manager = new ObjectManager();
		guiManager = new GUIManager();
		sun = Sun.getInstance().setSun(new Vector3f(1, -0.5f, 0), new Vector3f(1, 1, 1), 0.5f);
		init();
	}
	
	public void render() {
		
		prepare();
		terrain.render();
		manager.render();
		guiManager.render();
	}


	public void update() {
		terrain.update();
		changeSun(guiManager);
	}
	
	private long lastChange = 0;
	private int state = 0;
	private void changeSun(GUIManager manager) {
		if (System.currentTimeMillis() - lastChange >= 5000) {
			lastChange = System.currentTimeMillis();
			guiManager.getGuis().clear();
			if(state == 0) {
				state = 1;
				sun.setDirection(new Vector3f(1, 0, 0));
				
				guiManager.getGuis().add(new GUITexture(
						new Vector2f(0.7f, 0.7f),
						new Vector2f(0.3f, 0.3f), 
						ImageLoader.loadTexture("res/gui/arrow/left-arrow.png", false)));
			} else if (state == 1) {
				state = 2;
				sun.setDirection(new Vector3f(-1, 0, 0));
				
				guiManager.getGuis().add(new GUITexture(
						new Vector2f(0.7f, 0.7f),
						new Vector2f(0.3f, 0.3f), 
						ImageLoader.loadTexture("res/gui/arrow/right"
								+ "-arrow.png", false)));
			} else if (state == 2) {
				state = 3;
				sun.setDirection(new Vector3f(0, 0, 1));
				
				guiManager.getGuis().add(new GUITexture(
						new Vector2f(0.7f, 0.7f),
						new Vector2f(0.3f, 0.3f), 
						ImageLoader.loadTexture("res/gui/arrow/up-arrow.png", false)));
			} else if (state == 3) {
				state = 0;
				sun.setDirection(new Vector3f(0, 0, -1));
				
				guiManager.getGuis().add(new GUITexture(
						new Vector2f(0.7f, 0.7f),
						new Vector2f(0.3f, 0.3f), 
						ImageLoader.loadTexture("res/gui/arrow/bottom-arrow.png", false)));
			}
		}
		
	}



	private void prepare() {
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void init() {
		GL11.glFrontFace(GL11.GL_CCW);
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
