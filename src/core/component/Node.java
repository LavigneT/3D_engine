package core.component;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Node {

	private Node parent;
	private ArrayList<Node> childrens = new ArrayList<>();
	
	private Matrix4f localTransform, worldTransform;
	
	public void addChild(Node child) {
		childrens.add(child);
		child.setParent(this);
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Matrix4f getLocalTransform() {
		return localTransform;
	}

	public Matrix4f getWorldTransform() {
		return worldTransform;
	}

	public void setLocalTransform(Matrix4f localTransform) {
		this.localTransform = localTransform;
	}

	public void setWorldTransform(Matrix4f worldTransform) {
		this.worldTransform = worldTransform;
	}

	public ArrayList<Node> getChildren() {
		return childrens;
	}

	
	
}
