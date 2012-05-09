package org.a2r.terminal21.mesh ;

import java.lang.Math ;
import java.util.ArrayList ;


public class SphericalHarmonic extends Mesh {
	
	private static final short resolution = 100 ;
	private static final double scale = 1 ;
	private int[] m ;
	private ArrayList<Float> vertices ;
	private ArrayList<Float> vertexNormals ;
	private ArrayList<Short> faces ;
	
	public SphericalHarmonic(int m0, int m1, int m2, int m3, int m4, int m5, int m6, int m7) {
		this.vertices = new ArrayList<Float>() ;
		this.vertexNormals = new ArrayList<Float>() ;
		this.faces = new ArrayList<Short>() ;
		
		this.m = new int[8] ;
		this.m[0] = m0 ;
		this.m[1] = m1 ;
		this.m[2] = m2 ;
		this.m[3] = m3 ;
		this.m[4] = m4 ;
		this.m[5] = m5 ;
		this.m[6] = m6 ;
		this.m[7] = m7 ;
		
		this.computeVertices() ;
		this.computeFaces() ;
		this.computeVertexNormals() ;

		setVertices(vertices.toArray(new Float[this.vertices.size()])) ;
		setIndices(faces.toArray(new Short[this.faces.size()])) ;
		setVertexNormals(vertexNormals.toArray(new Float[this.vertexNormals.size()])) ;
	}
	
	private void computeVertexFor(double phi, double theta) {
		double r = 0 ;
		
		r += Math.pow(Math.sin(this.m[0] * theta), this.m[1]) ;
		r += Math.pow(Math.cos(this.m[2] * theta), this.m[3]) ;
		r += Math.pow(Math.sin(this.m[4] * phi), this.m[5]) ;
		r += Math.pow(Math.cos(this.m[6] * phi), this.m[6]) ;
		r *= SphericalHarmonic.scale ;
		
		double sinTheta = Math.sin(theta) ;
	
		this.vertices.add((float)(r * sinTheta * Math.cos(phi))) ;
		this.vertices.add((float)(r * Math.cos(theta))) ;
		this.vertices.add((float)(r * sinTheta * Math.sin(phi))) ;
	}
	
	private void computeVertices() {
		double phi = 0 ;
		double theta = 0 ;
		for (int i = 0; i < SphericalHarmonic.resolution; i++) {
			phi = Math.PI * 2 * ((double)i / (double)SphericalHarmonic.resolution) ;
			for (int j = 0; j < SphericalHarmonic.resolution; j++) {
				theta = Math.PI * ((double)j / (double)SphericalHarmonic.resolution) ;
				
				this.computeVertexFor(phi, theta) ;
			}
		}
	}
	
	private void computeFaces() {
		short j = 0 ;
		short numVertices = this.verticesLength() ;
		
		for (short i = 0; i < numVertices; i++) {
			j = numInMeridian(i) ;	// vertex number in meridian
			if (j > 0) {				// not first vertex in meridian

				// first face on vertex, compute against next meridian, except we are on the last one
				faces.add(i) ;
				faces.add((short)(i - 1)) ;
				faces.add((short)((i + SphericalHarmonic.resolution - 1) % numVertices)) ;
				
				// second face on vertex
				faces.add(i) ;
				faces.add((short)((i + SphericalHarmonic.resolution -1) % numVertices)) ;
				faces.add((short)((i + SphericalHarmonic.resolution) % numVertices)) ;
			}
		}
	}
	
	private void computeVertexNormals() {
		short j = 0 ;
		short numVertices = this.verticesLength();
		short[] surroundVertices = new short[6] ;
		
		float nx, ny, nz ;	// vertex normal x, y, z ;
		
		for (short i = 0; i < this.vertices.size(); i+=3) {
			j = numInMeridian(i/3) ;
			nx = 0 ;
			ny = 0 ;
			nz = 0 ;
						
			switch(j) {
			case 0:
				nx = 1 ; // dummy normals, fixme ;
				ny = 1 ;
				nz = 1 ;
				break ;
			case (short)(SphericalHarmonic.resolution-1):
				nx = 1 ; // dummy normals, fixme ;
				ny = 1 ;
				nz = 1 ;
				break ;
			default:
				surroundVertices[0] = (short)(i + 3) ;	// finding surrounding vertices, that span a face
				surroundVertices[1] = (short)((i + SphericalHarmonic.resolution * 3) % this.vertices.size()) ;
				surroundVertices[2] = (short)((i + SphericalHarmonic.resolution * 3 - 3) % this.vertices.size()) ;
				surroundVertices[3] = (short)(i - 3) ;
				surroundVertices[4] = (short)(i - SphericalHarmonic.resolution * 3) ;
				surroundVertices[5] = (short)(i - SphericalHarmonic.resolution * 3 + 3 ) ;
				
				if (surroundVertices[4] < 0) surroundVertices[4] += this.vertices.size() ;	// first meridian
				if (surroundVertices[5] < 0) surroundVertices[5] += this.vertices.size() ;
				
				for (short k = 0; k < 6; k++) {		// 6 faces per vertex
					nx += (this.vertices.get(surroundVertices[k]+1) * this.vertices.get(surroundVertices[(k+1)%6]+2)) ;
					nx -= (this.vertices.get(surroundVertices[k]+1) * this.vertices.get(i+2)) ;
					nx -= (this.vertices.get(surroundVertices[(k+1)%6]+2) * this.vertices.get(i+1)) ;
					nx -= (this.vertices.get(surroundVertices[k]+2) * this.vertices.get(surroundVertices[(k+1)%6]+1)) ;
					nx += (this.vertices.get(surroundVertices[k]+2) * this.vertices.get(i+1)) ;
					nx += (this.vertices.get(surroundVertices[(k+1)%6]+1) * this.vertices.get(i+2)) ;
					
					ny += (this.vertices.get(surroundVertices[k]+2) * this.vertices.get(surroundVertices[(k+1)%6])) ;
					ny -= (this.vertices.get(surroundVertices[k]+2) * this.vertices.get(i)) ;
					ny -= (this.vertices.get(surroundVertices[(k+1)%6]) * this.vertices.get(i+2)) ;
					ny -= (this.vertices.get(surroundVertices[k]) * this.vertices.get(surroundVertices[(k+1)%6]+2)) ;
					ny += (this.vertices.get(surroundVertices[k]) * this.vertices.get(i+2)) ;
					ny += (this.vertices.get(surroundVertices[(k+1)%6]+2) * this.vertices.get(i)) ;

					nz += (this.vertices.get(surroundVertices[k]) * this.vertices.get(surroundVertices[(k+1)%6]+1)) ;
					nz -= (this.vertices.get(surroundVertices[k]) * this.vertices.get(i+1)) ;
					nz -= (this.vertices.get(surroundVertices[(k+1)%6]+1) * this.vertices.get(i)) ;
					nz -= (this.vertices.get(surroundVertices[k]+1) * this.vertices.get(surroundVertices[(k+1)%6])) ;
					nz += (this.vertices.get(surroundVertices[k]+1) * this.vertices.get(i)) ;
					nz += (this.vertices.get(surroundVertices[(k+1)%6]) * this.vertices.get(i+1)) ;
					
				}
			}
			
			this.vertexNormals.add((float)(-1 * nx / Math.sqrt(Math.pow(nx, 2) + Math.pow(ny, 2) + Math.pow(nz, 2)))) ;		// normalize it
			this.vertexNormals.add((float)(-1 * ny / Math.sqrt(Math.pow(nx, 2) + Math.pow(ny, 2) + Math.pow(nz, 2)))) ;
			this.vertexNormals.add((float)(-1 * nz / Math.sqrt(Math.pow(nx, 2) + Math.pow(ny, 2) + Math.pow(nz, 2)))) ;					
		}
	}
	
	protected short verticesLength() {			// Number of vertices
		return (short)(this.vertices.size() / 3) ;
	}
	
	protected short numInMeridian(int i) {	// Number of vertex in Meridian 
		return (short)(i % SphericalHarmonic.resolution) ;
	}
}