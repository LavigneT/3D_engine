package core.manager;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.assimp.Assimp;

import core.asset.Material;
import core.asset.Model;
import core.buffer.Mesh;
import core.component.GameObject;
import core.kernel.Camera;
import core.math.Tools;
import core.shader.ObjectShader;
import core.utils.ImageLoader;

public class ObjectManager {
	
	private ArrayList<GameObject> objects = new ArrayList<>();
	private ObjectShader shader;
	
	public ObjectManager() {
		init();
		shader = ObjectShader.getInstance();
	}

	private void init() {
		
		//Model treeModel = new Model("res/obj/tree/tree.obj");
		//objects.add(new GameObject(treeModel, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(8, 8, 8)));
		
		Model missile = new Model("res/obj/car/missile.obj");
		objects.add(new GameObject(missile, new Vector3f(50, 170,100), new Vector3f(0, 0, 0), new Vector3f(5, 5, 5)));
		Material material = new Material();
		material.addTexture(Assimp.aiTextureType_DIFFUSE, ImageLoader.loadTexture("res/obj/car/Texture.png", true));
		for(Mesh mesh : missile.getMeshes())
			mesh.setMaterial(material);
		
		Model nanoModel = new Model("res/obj/nanosuit/nanosuit.obj");
		objects.add(new GameObject(nanoModel, new Vector3f(50, 100, 100), new Vector3f(0, 0, 0), new Vector3f(8, 8, 8)));
		
	}
	
	public void render() {
		shader.start();
		for(GameObject object: objects) {
			shader.update(Tools.getProjectionMatrix(), object.getWorldTransform(), Camera.getCamera().getViewMatrix());
			object.draw();
		}
		shader.stop();
	}

}
