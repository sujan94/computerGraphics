/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 * Demonstrate myPerspective
 * 
 */


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import static com.jogamp.opengl.GL.*;
import java.nio.FloatBuffer;



public class JOGL3_4_Diffuse extends JOGL3_3_Ambient {
  
	  float M_emission[] = {0.1f, 0.1f, 0.1f, 1}; // Material property: emission 
	  float L_ambient[] = {0.5f, 0.5f, 0.5f, 1}; // Light source property: ambient 
	  float M_ambient[] = {0.5f, 0.5f, 0.5f, 1}; // Material property: ambient 
	  float L_diffuse[] = {1, 1, 1, 1}; // Light source property: diffuse 
	  float M_diffuse[] = {1, 1, 1, 1}; // Material property: diffuse 
	  float L_position[] = {4*WIDTH, 4*WIDTH, 4*WIDTH, 1}; // Light source property: position
	  
	  // here we need the per-vertex normal, which should be assigned in drawSphere/Cone/Cylinder 
	  // when we get the vertex position. 

	  
	  void subdivideCone(float vPoints[], float vNormals[], float v1[],
			  float v2[], int depth) {
		  float v0[] = {0, 0, 0};
		  float v12[] = new float[3];

	    if (depth==0) {
	      //drawtriangle(v2, v1, v0);
	      // bottom cover of the cone

	      v0[2] = 1; // height of the cone, the tip on z axis
			// load vPoints with the triangle vertex values
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v1[i] + v0[i]; 
				vPoints[count++] = v1[i] ;
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v2[i] + v0[i]; 
				vPoints[count++] = v2[i] ;
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v2[i] + v1[i] + v0[i]; 
				vPoints[count++] = v0[i] ;     
			}			
            return;	    
	    }

	    for (int i = 0; i<3; i++) {
	      v12[i] = v1[i]+v2[i];
	    }
	    normalize(v12);

	    subdivideCone(vPoints, vNormals, v1, v12, depth-1);
	    subdivideCone(vPoints, vNormals, v12, v2, depth-1);
	  }


	  public void drawCone() {
	    int numofTriangle= 4*(int)Math.pow(2,depth); // number of triangles after subdivision
	    float vPoints[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex
	    float vNormals[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex

	    count = 0; // start filling triangle array to be sent to vertex shader

	    subdivideCone(vPoints, vNormals, cVdata[0], cVdata[1], depth);
	    subdivideCone(vPoints, vNormals, cVdata[1], cVdata[2], depth);
	    subdivideCone(vPoints, vNormals, cVdata[2], cVdata[3], depth);
	    subdivideCone(vPoints, vNormals, cVdata[3], cVdata[0], depth);
	    
	    // send the current MODELVIEW matrix and the vertices to the vertex shader
	    // color is generated according to the logical coordinates   
	    uploadMV(); // get the modelview matrix from the matrix stack
		

		// load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW); 
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

		// load vbo[1] with normal data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 0 		
		 vBuf = Buffers.newDirectFloatBuffer(vNormals);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW); 
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active VAO buffer
				
		gl.glDrawArrays(GL_TRIANGLES, 0, vBuf.limit()/3); 
	  }

	  
	  
	  void subdivideCylinder(float vPoints[],float vNormals[],float v1[],
			  float v2[], int depth) {
		  float v0[] = {0, 0, 0};
		  float v12[] = new float[3];

	    if (depth==0) {	    	
	      //drawtriangle(v2, v1, v0);
	      // bottom cover of the cone
		  // load vPoints with the triangle vertex values
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = 0; 
				vPoints[count++] = v2[i] ;
			}
			vNormals[count-1] = -1; 
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = 0; 
				vPoints[count++] = v1[i] ;
			}
			vNormals[count-1] = -1; 
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = 0; 
				vPoints[count++] = v0[i] ;     
			}
			vNormals[count-1] = -1; 

			  float v11[] = new float[3];
			  float v22[] = new float[3];
		      for (int i = 0; i<2; i++) {
			        v22[i] = v2[i];
			        v11[i] = v1[i];
			  }
		      
		    v11[2] = v22[2] = v0[2] = 1.0f;
		      
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v1[i] ; 
				vPoints[count++] = v1[i] ;
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v2[i] ; 
				vPoints[count++] = v2[i] ;
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v2[i] ; 
				vPoints[count++] = v22[i] ;     
			}

			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v1[i] ; 
				vPoints[count++] = v1[i] ;
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v2[i] ; 
				vPoints[count++] = v22[i] ;
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v1[i] ; 
				vPoints[count++] = v11[i] ;     
			}		
            return;	    
	    }

	    for (int i = 0; i<3; i++) {
	      v12[i] = v1[i]+v2[i];
	    }
	    normalize(v12);

	    subdivideCylinder(vPoints, vNormals, v1, v12, depth-1);
	    subdivideCylinder(vPoints, vNormals, v12, v2, depth-1);	    
	  }


	  public void drawCylinder() {
	    int numofTriangle= 3*4*(int)Math.pow(2,depth); // number of triangles after subdivision
	    float vPoints[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex
	    float vNormals[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex

	    count = 0; // start filling triangle array to be sent to vertex shader

	    subdivideCylinder(vPoints, vNormals, cVdata[0], cVdata[1], depth);
	    subdivideCylinder(vPoints, vNormals, cVdata[1], cVdata[2], depth);
	    subdivideCylinder(vPoints, vNormals, cVdata[2], cVdata[3], depth);
	    subdivideCylinder(vPoints, vNormals, cVdata[3], cVdata[0], depth);
	    // send the current MODELVIEW matrix and the vertices to the vertex shader
	    // color is generated according to the logical coordinates   
	    uploadMV(); // get the modelview matrix from the matrix stack
		
		// load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW); 
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

		// load vbo[0] with vertex normal data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 0 		
		vBuf = Buffers.newDirectFloatBuffer(vNormals);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW); 
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer
				
		gl.glDrawArrays(GL_TRIANGLES, 0, vBuf.limit()/3); 
	  }

	  void subdivideSphere(float vPoints[],float vNormals[], float v1[],
			  float v2[], float v3[], int depth) {
		  float v12[] = new float[3];
		  float v23[] = new float[3];
		  float v31[] = new float[3];

	    if (depth==0) {
			// load vPoints with the triangle vertex values
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v1[i] ;
				vPoints[count++] = v1[i];
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v2[i] ;
				vPoints[count++] = v2[i];
			}
			for (int i = 0; i < 3; i++)  {
				vNormals[count] = v3[i] ;
				vPoints[count++] = v3[i];
			}			
			return; 
	    }
	    
	    for (int i = 0; i<3; i++) {
	        v12[i] = v1[i]+v2[i];
	        v23[i] = v2[i]+v3[i];
	        v31[i] = v3[i]+v1[i];
	      }
	      normalize(v12);
	      normalize(v23);
	      normalize(v31);
	      subdivideSphere(vPoints, vNormals, v1, v12, v31, depth-1);
	      subdivideSphere(vPoints, vNormals, v2, v23, v12, depth-1);
	      subdivideSphere(vPoints, vNormals, v3, v31, v23, depth-1);
	      subdivideSphere(vPoints, vNormals, v12, v23, v31, depth-1);	    
	  }


	  public void drawSphere() {
	    int numofTriangle= 8*(int)Math.pow(4,depth); // number of triangles after subdivision
	    float vPoints[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex
	    float vNormals[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex

	    count = 0; // start filling triangle array to be sent to vertex shader

	    subdivideSphere(vPoints, vNormals, sVdata[0], sVdata[1], sVdata[2], depth);
	    subdivideSphere(vPoints, vNormals, sVdata[0], sVdata[2], sVdata[4], depth);
	    subdivideSphere(vPoints, vNormals, sVdata[0], sVdata[4], sVdata[5], depth);
	    subdivideSphere(vPoints, vNormals, sVdata[0], sVdata[5], sVdata[1], depth);

	    subdivideSphere(vPoints, vNormals, sVdata[3], sVdata[1], sVdata[5], depth);
	    subdivideSphere(vPoints, vNormals, sVdata[3], sVdata[5], sVdata[4], depth);
	    subdivideSphere(vPoints, vNormals, sVdata[3], sVdata[4], sVdata[2], depth);
	    subdivideSphere(vPoints, vNormals, sVdata[3], sVdata[2], sVdata[1], depth);

	    
	    // send the current MODELVIEW matrix and the vertices to the vertex shader
	    uploadMV(); // get the modelview matrix from the matrix stack
		// connect the modelview matrix

		// load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW); 
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer
				
		// load vbo[1] with vertex normal data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1 		
		FloatBuffer nBuf = Buffers.newDirectFloatBuffer(vNormals);
		gl.glBufferData(GL_ARRAY_BUFFER, nBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				nBuf, // the vertex array
				GL_STATIC_DRAW); 
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active VAO buffer

		gl.glDrawArrays(GL_TRIANGLES, 0, vBuf.limit()/3);
	  }


	  
	  public void display(GLAutoDrawable glDrawable) {
		  // send the material property to the vertex shader
	 		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(M_emission);

			//Connect JOGL variable with shader variable by name
			int colorLoc = gl.glGetUniformLocation(vfPrograms,  "Me"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);


	 		cBuf = Buffers.newDirectFloatBuffer(L_ambient);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "La"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

	 		cBuf = Buffers.newDirectFloatBuffer(M_ambient);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ma"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
			
	 		cBuf = Buffers.newDirectFloatBuffer(L_diffuse);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ld"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

	 		cBuf = Buffers.newDirectFloatBuffer(M_diffuse);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Md"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
			
	 		cBuf = Buffers.newDirectFloatBuffer(L_position);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Lsp"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

			// draw the models
			    super.display(glDrawable);
	  }
  
	  
	  
	  public void init(GLAutoDrawable drawable) {
			
			gl = (GL4) drawable.getGL();
			String vShaderSource[], fShaderSource[] ;
						
			vShaderSource = readShaderSource("src/JOGL3_4_V.shader"); // read vertex shader
			fShaderSource = readShaderSource("src/JOGL3_2_F.shader"); // read fragment shader
			vfPrograms = initShaders(vShaderSource, fShaderSource);		
			
			// 1. generate vertex arrays indexed by vao
			gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
			gl.glBindVertexArray(vao[0]); // use handle 0
			
			// 2. generate vertex buffers indexed by vbo: here vertices and colors
			gl.glGenBuffers(vbo.length, vbo, 0);
			
			// 3. enable VAO with loaded VBO data
			gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
			
			// if you don't use it, you should not enable it
			gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
						
			//4. specify drawing into only the back_buffer
			gl.glDrawBuffer(GL_BACK); 
			
			// 5. Enable zbuffer and clear framebuffer and zbuffer
			gl.glEnable(GL_DEPTH_TEST); 

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 		}

	  
	  public static void main(String[] args) {
	    new JOGL3_4_Diffuse();
	  }

}
