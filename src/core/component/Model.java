package core.component;

import static org.lwjgl.assimp.Assimp.AI_SCENE_FLAGS_INCOMPLETE;
import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiProcess_FlipUVs;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.assimp.Assimp.aiTextureType_DIFFUSE;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import core.buffer.Mesh;
import core.texture.Texture2D;

public class Model {
	
	private ArrayList<Mesh> meshes = new ArrayList<>();;
	
	public Model(String file) {
		this.loadModel(file); 
	}
	
	public void loadModel(String path) {
		File file = new File(path);
		String fileDire = file.getParent();
		AIScene scene = aiImportFile(file.getAbsolutePath(), aiProcess_Triangulate | aiProcess_FlipUVs);
		
		if (scene == null || scene.mFlags() == AI_SCENE_FLAGS_INCOMPLETE  || scene.mRootNode() == null) {
			throw new IllegalStateException("An error occured during the loading of the model");
		}
		
		processNode(scene.mRootNode(), scene, fileDire);
	}
	
	private void processNode(AINode node, AIScene scene, String fileDir) {
		
		for(int i = 0; i < node.mNumMeshes(); i++) {
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
			meshes.add(processMesh(mesh, scene));
		}
		
		for(int j = 0; j < node.mNumChildren(); j++) {
			processNode(AINode.create(node.mChildren().get(j)), scene, fileDir);
		}
		
	}
	
	public Mesh processMesh(AIMesh mesh, AIScene scene) {
		
		float[] data = new float[mesh.mNumVertices() * 6 + mesh.mNumVertices() * 2];
		
		ArrayList<Integer> indices = new ArrayList<>();
		ArrayList<Texture2D> textures = new ArrayList<>();
		
		int index = 0;
		for(int i = 0; i < mesh.mNumVertices(); i++) {
			AIVector3D vertice = mesh.mVertices().get(i);
			AIVector3D normal = mesh.mNormals().get(i);
			AIVector3D texCoord = mesh.mTextureCoords(0).get(i); 
			
			data[index++] = vertice.x();
			data[index++] = vertice.y();
			data[index++] = vertice.z();
			
			data[index++] = normal.x();
			data[index++] = normal.y();
			data[index++] = normal.z();
			
			data[index++] = texCoord.x();
			data[index++] = texCoord.y();
		}
		
		for(int i  = 0; i < mesh.mNumFaces();i++) {
			
			AIFace face = mesh.mFaces().get(i);
			
			for(int j = 0; j < face.mNumIndices(); j++) {
				indices.add(face.mIndices().get(j));
			}
		}
		
		
		if (mesh.mMaterialIndex()  >= 0) {
			AIMaterial material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));
			
			AIColor4D colour = AIColor4D.create();
			
		    AIString path = AIString.calloc();
		    Assimp.aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
		    String textPath = path.dataString();
		    Texture2D texture = null;
		    if(textPath != null && textPath.length() > 0)  {
		    	
		    	
		    }
		    
		    AIColor4D color = AIColor4D.create();
		    Vector3f diffuseColor = null;
		    int result = Assimp.aiGetMaterialColor(material, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, color);
		    if (result == 0) {
		    	diffuseColor = new Vector3f(color.r(), color.g(), color.b());
		    }

		    /*Material material = new Material();
		    material.setDiffusemap(diffuseTexture);
		    material.setColor(diffuseColor);*/
		    
		}
		
		int[] indicesArray = new int[indices.size()];
		for(int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return new Mesh(data, indicesArray);
	}

	public void draw() {
		for(Mesh mesh : meshes) {
			mesh.draw();
		}
	}
	
	
}
