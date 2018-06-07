package core.component;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.joml.Vector3f;

import core.kernel.Camera;
import core.math.Tools;
import core.shader.ObjectShader;

public class ObjectManager {
	
	private ArrayList<GameObject> objects = new ArrayList<>();
	private ObjectShader shader;
	
	public ObjectManager() {
		init();
		shader = ObjectShader.getInstance();
	}

	private void init() {
		
		Model treeModel = new Model("res/obj/tree/tree.obj");
		objects.add(new GameObject(treeModel, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(8, 8, 8)));
		
		Model planeModel = new Model("res/obj/plane.obj");
		objects.add(new GameObject(planeModel, new Vector3f(30, 0, 0), new Vector3f(0, 0, 0), new Vector3f(8, 8, 8)));
		
		Model nanoModel = new Model("res/obj/nanosuit/nanosuit.obj");
		objects.add(new GameObject(nanoModel, new Vector3f(50, 30, 20), new Vector3f(0, 0, 0), new Vector3f(8, 8, 8)));
		
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
