package org.a2r.terminal21;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.a2r.terminal21.mesh.Cube;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

// OpenGLRenderer by http://blog.jayway.com/2009/12/03/opengl-es-tutorial-for-android-part-i/
public class OpenGLRenderer implements Renderer {
	
	private Cube cube ;
	private float[] light0Position ;
	private float[] light1Position ;
	
	private float angle = 0 ;
	
	public OpenGLRenderer() {
		cube = new Cube(2, 2, 2) ;
		light0Position = new float[]{3f, 3f, 5f, 0} ;
		light1Position = new float[]{-3f, -3f, 5f, 0} ;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f) ;
		gl.glShadeModel(GL10.GL_SMOOTH) ;
		gl.glClearDepthf(1.0f) ;
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light0Position, 0) ;
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[]{1f, 0f, 0f, 1f}, 0) ;
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, light1Position, 0) ;
		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, new float[]{0f, 0f, 1f, 1f}, 0) ;
		gl.glEnable(GL10.GL_LIGHTING) ;
		gl.glEnable(GL10.GL_LIGHT0) ;
		gl.glEnable(GL10.GL_LIGHT1) ;
		gl.glEnable(GL10.GL_DEPTH_TEST) ;
		gl.glDepthFunc(GL10.GL_LEQUAL) ;
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST) ;
	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT) ;
		gl.glLoadIdentity() ;
		gl.glTranslatef(0, 0, -10) ;
		
		gl.glPushMatrix() ;
		cube.rx = angle ;
		cube.ry = angle/10 ;
		cube.rz = angle/5 ;
		cube.draw(gl) ;
		gl.glPopMatrix() ;
		
		angle++ ;
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height) ;
		gl.glMatrixMode(GL10.GL_PROJECTION) ;
		gl.glLoadIdentity() ;
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f) ;
		gl.glMatrixMode(GL10.GL_MODELVIEW) ;
		gl.glLoadIdentity() ;
	}
}