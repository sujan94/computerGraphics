/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */




import com.jogamp.opengl.*;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL4.*;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;

public class JOGL2_5_Cone extends JOGL2_4_Robot {
	float MV[] = new float [16]; // MODELVIEW matrix
	float PROJ[] = new float [16]; // PROJECTION matrix; 


	  public void reshape(
		      GLAutoDrawable glDrawable,
		      int x,
		      int y,
		      int w,
		      int h) {

		    WIDTH = w;
		    HEIGHT = h;

		    //gl.glMatrixMode(GL.GL_PROJECTION);
		    myLoadIdentity();

		    //1. make sure the cone is within the viewing volume
		    myOrtho(-w/2, w/2, -h/2, h/2, -w, w); // look at z near and far
			get_Matrix(PROJ); 
			
			// connect the PROJECTION matrix to the vertex shader
			int projLoc = gl.glGetUniformLocation(vfPrograms,  "proj_matrix"); 
			gl.glProgramUniformMatrix4fv(vfPrograms, projLoc,  1,  false,  PROJ, 0);



		    //gl.glMatrixMode(GL.GL_MODELVIEW);
		    myLoadIdentity();
		    
		    //2. This will enable depth test in general
		   	gl.glEnable(GL.GL_DEPTH_TEST);
		  }
	  
	  	// multiply the current matrix with the orthographic projection matrix
	    void myOrtho(float l, float r, float b, float t, float n, float f) {// look at z near and far
	    	float orthoMatrix[][] = {
	    			{2f/(r-l),  		0, 			0, 		-(r+l)/(r-l)}, 
	    			{       0,   2f/(t-b), 	  		0, 		-(t+b)/(t-b)}, 
	    			{       0,          0,  -2f/(f-n), 		-(f+n)/(f-n)}, 
	    			{       0, 		    0, 		    0, 		           1}
	    	}; 	    	
	    	myMultMatrix(orthoMatrix); 
	    } 

	    
	    public void display(GLAutoDrawable glDrawable) {

			cnt++;    
			cRadius += flip;
			if ((cRadius>(WIDTH/2))|| (cRadius<=1)) {
				depth++;
				depth = depth%7;
		        flip = -flip;
		     }
		   
			//3. clear both framebuffer and zbuffer
		    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	   
	    //4. GL_DEPTH_TEST for hidden-surface removal
	    	gl.glEnable(GL_DEPTH_TEST);
	    	//gl.glDisable(GL_DEPTH_TEST);
	    
	 
	   
	    //5. Test glPolygonMode 
	         gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL_FILL);
	    	 //gl.glPolygonMode(GL.GL_FRONT, GL_LINE);
	    	 //gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL_LINE);
	 
	    //6. Test glCullFace
	    	gl.glEnable(GL_CULL_FACE); 
	    	gl.glDisable(GL_CULL_FACE); 
	        //gl.glCullFace(GL_BACK); 
	        //gl.glCullFace(GL_FRONT);

	        
	    //6. draw a triangle for showing hidden surface removal
	    float 	v0[] = {-WIDTH/4, -WIDTH/4, -WIDTH/4}, 
	    		v1[] = {WIDTH/4, 0, WIDTH/4}, 
	    		v2[] = {WIDTH/4, HEIGHT/4, 0}; 
	    myPushMatrix();
	        myLoadIdentity();
	        myRotatef((float) cnt/50f, 0, 1, 0); 
//		    gl.glColor3f(0.5f, 0.5f, 0.5f);
		    drawtriangle(v0, v1, v2); 
	    myPopMatrix();
	    
	    // rotate 1 degree alone vector (1, 1, 1)
	    myRotatef(0.01f, 0.5f, 0.5f, 0.5f); // numerical drift if not rotating around the primary axis
	    myRotatef(0.01f, 1f, 0f, 0f);    
	    myPushMatrix();
	    myScalef(cRadius, cRadius, cRadius);
	    //myScalef(50, 50, 50);

		    drawCone();
	    myPopMatrix();

	  }

	    void drawtriangle(float v0[], float v1[], float v2[]) {
		// connect the modelview matrix
		    get_Matrix(MV); // get the modelview matrix from the matrix stack
		    int mvLoc = gl.glGetUniformLocation(vfPrograms,  "mv_matrix"); 
		    gl.glProgramUniformMatrix4fv(vfPrograms, mvLoc,  1,  false,  MV, 0);  
		    
		    count = 0; 
		    
			// load vPoints with the triangle vertex values
			for (int i = 0; i < 3; i++)  vPoints[count++] = v1[i] ;
			for (int i = 0; i < 3; i++)  vPoints[count++] = v2[i] ;
			for (int i = 0; i < 3; i++)  vPoints[count++] = v0[i] ;     


			// load vbo[0] with vertex data
			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
			FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
			gl.glBufferData(GL_ARRAY_BUFFER, 9*Float.BYTES,  //# of float * size of floats in bytes
					vBuf, // the vertex array
					GL_STATIC_DRAW); 
			gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer
					
			gl.glDrawArrays(GL_TRIANGLES, 0, 3); 
	    }
	    
	  private void subdivideCone(float vPoints[],float v1[],
			  float v2[], int depth) {
		  float v0[] = {0, 0, 0};
		  float v12[] = new float[3];

	    if (depth==0) {
	      // gl.glColor3d(v1[0]*v1[0], v1[1]*v1[1], 0);
	      // color is specified in the Fragment shader
	    	
	      //drawtriangle(v2, v1, v0);
	      // bottom cover of the cone

	      v0[2] = 1; // height of the cone, the tip on z axis
	      //drawtriangle(v1, v2, v0); // side cover of the cone
			// load vPoints with the triangle vertex values
			for (int i = 0; i < 3; i++)  vPoints[count++] = v1[i] ;
			for (int i = 0; i < 3; i++)  vPoints[count++] = v2[i] ;
			for (int i = 0; i < 3; i++)  vPoints[count++] = v0[i] ;     
			
            return;

	    
	    }

	    for (int i = 0; i<3; i++) {
	      v12[i] = v1[i]+v2[i];
	    }
	    normalize(v12);

	    subdivideCone(vPoints, v1, v12, depth-1);
	    subdivideCone(vPoints, v12, v2, depth-1);
	  }


	  public void drawCone() {
	    int numofTriangle= 4*(int)Math.pow(2,depth); // number of triangles after subdivision
	    float vPoints[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex

	    //System.out.println("vPoints[] is used to save all triangle vertex values, pervertex values to be sent to the vertex shader");	

	    count = 0; // start filling triangle array to be sent to vertex shader

	    subdivideCone(vPoints, cVdata[0], cVdata[1], depth);
	    subdivideCone(vPoints, cVdata[1], cVdata[2], depth);
	    subdivideCone(vPoints, cVdata[2], cVdata[3], depth);
	    subdivideCone(vPoints, cVdata[3], cVdata[0], depth);
	    
	    // send the current MODELVIEW matrix and the vertices to the vertex shader
	    // color is generated according to the logical coordinates   
	    get_Matrix(MV); // get the modelview matrix from the matrix stack
		
		// connect the modelview matrix
		int mvLoc = gl.glGetUniformLocation(vfPrograms,  "mv_matrix"); 
		gl.glProgramUniformMatrix4fv(vfPrograms, mvLoc,  1,  false,  MV, 0);  

		// load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex array
				GL_STATIC_DRAW); 
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer
				
		gl.glDrawArrays(GL_TRIANGLES, 0, vBuf.limit()/3); 
	  }


		public void init(GLAutoDrawable drawable) {
			
			gl = (GL4) drawable.getGL();
			String vShaderSource[], fShaderSource[] ;
						
			vShaderSource = readShaderSource("src/JOGL2_5_V.shader"); // read vertex shader
			fShaderSource = readShaderSource("src/JOGL2_5_F.shader"); // read fragment shader
			vfPrograms = initShaders(vShaderSource, fShaderSource);		
			
			// 1. generate vertex arrays indexed by vao
			gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
			gl.glBindVertexArray(vao[0]); // use handle 0
			
			// 2. generate vertex buffers indexed by vbo: here vertices and colors
			gl.glGenBuffers(vbo.length, vbo, 0);
			
			// 3. enable VAO with loaded VBO data
			gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
			
			// if you don't use it, you should not enable it
			//gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
						
			//4. specify drawing into only the back_buffer
			gl.glDrawBuffer(GL.GL_BACK); 
			
			// 5. Enable zbuffer and clear framebuffer and zbuffer
			gl.glEnable(GL.GL_DEPTH_TEST); 

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
 		}
	  
	  
  public static void main(String[] args) {
    new JOGL2_5_Cone();
  }

}
