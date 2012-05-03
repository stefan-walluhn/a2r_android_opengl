package org.a2r.terminal21.mesh ;


public class Cube extends Mesh {
	private float normal = 0.577350269f ;
	
	public Cube (float width, float height, float depth) {
		width /= 2 ;
		height /= 2 ;
		depth /= 2 ;
		
		float vertices[] = {	// ToDo: Implement push in Mesh
				-width, -height, -depth,
				width, -height, -depth,
				width, height, -depth,
				-width, height, -depth,
				-width, -height, depth,
				width, -height, depth,
				width, height, depth,
				-width, height, depth,
		} ;
		
		float vertexNormals[] = {
				-1 * normal, -1 * normal, -1 * normal,
				normal, -1 * normal, -1 * normal,
				normal, normal, -1 * normal,
				-1 * normal, normal, -1 * normal,
				-1 * normal, -1 * normal, normal,
				normal, -1 * normal, normal,
				normal, normal, normal,
				-1 * normal, normal, normal,
		} ;
		
		short indices[] = {		// ToDo: Implement push in Mesh
				0, 4, 5,
				0, 5, 1,
				1, 5, 6,
				1, 6, 2,
				2, 6, 7,
				2, 7, 3,
				3, 7, 4,
				3, 4, 0,
				4, 7, 6,
				4, 6, 5,
				3, 0, 1,
				3, 1, 2,
		} ;
		
		setIndices(indices) ;
		setVertices(vertices) ;
		setVertexNormals(vertexNormals) ;
	}
}