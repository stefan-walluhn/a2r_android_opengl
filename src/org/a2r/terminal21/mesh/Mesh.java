package org.a2r.terminal21.mesh ;

import java.nio.ByteBuffer ;
import java.nio.ByteOrder; 
import java.nio.FloatBuffer ;
import java.nio.ShortBuffer ;

import javax.microedition.khronos.opengles.GL10;

public class Mesh {
	
	public float rx = 0 ;	// ToDo: I like to have a 3D-Vector
	public float ry = 0 ;
	public float rz = 0 ;
	
	private FloatBuffer verticesBuffer = null ;
	private FloatBuffer vertexNormalsBuffer = null ;
	private ShortBuffer indicesBuffer = null ;
	
	private int numOfIndices = -1 ;
	
	private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f} ;
	
	protected void setVertices(float[] vertices) {	//ToDo: I like to have a push function
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4) ;
		vbb.order(ByteOrder.nativeOrder()) ;
		verticesBuffer = vbb.asFloatBuffer() ;
		verticesBuffer.put(vertices) ;
		verticesBuffer.position(0) ;
	}
	
	protected void setIndices(short[] indices) {	//Todo: I like to have a push function
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2) ;
		ibb.order(ByteOrder.nativeOrder()) ;
		indicesBuffer = ibb.asShortBuffer() ;
		indicesBuffer.put(indices) ;
		indicesBuffer.position(0) ;
		numOfIndices = indices.length ;
	}
	
	protected void setVertexNormals(float[] normals) {	//ToDo: compute normals
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4) ;
		nbb.order(ByteOrder.nativeOrder()) ;
		vertexNormalsBuffer = nbb.asFloatBuffer() ;
		vertexNormalsBuffer.put(normals) ;
		vertexNormalsBuffer.position(0) ;
	}
	
	protected void setColor(float red, float green, float blue, float alpha) {
		rgba[0] = red ;
		rgba[1] = green ;
		rgba[2] = blue ;
		rgba[3] = alpha ;
	}
	
	public void draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CW) ;
		gl.glEnable(GL10.GL_CULL_FACE) ;
		gl.glCullFace(GL10.GL_BACK) ;
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY) ;
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY) ;
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer) ;
		gl.glNormalPointer(GL10.GL_FLOAT, 0, vertexNormalsBuffer) ;
		gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]) ;
		gl.glRotatef(rx, 1, 0, 0) ;
		gl.glRotatef(ry, 0, 1, 0) ;
		gl.glRotatef(rz, 0, 0, 1) ;
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer) ;
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY) ;
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY) ;
		gl.glDisable(GL10.GL_CULL_FACE) ;
	}
}