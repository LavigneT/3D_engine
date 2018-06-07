package core.utils;

/**
 * 
 * @author lav
 *
 *	Contient seulement les données contenuent dans la VAO par le biais du vaoID
 */
public class ModelData {
	
	private int vaoID;
	private int vertexCount;

	public ModelData(int vaoID, int vertexCount) {
		super();
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	 

}
