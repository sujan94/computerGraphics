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
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import static com.jogamp.opengl.GL.*;

import java.io.File;
import java.nio.FloatBuffer;


public class JOGL4_7_TexObjects extends JOGL4_6_Texture {
	
	// used for texture objects
	int	gmuTexture; 
	int	vseTexture; 
	int cubeTexture; 
	int cube1Texture; 


	  public void drawCone() {
	    int drawCone = 1; 
	    
		int cLoc = gl.glGetUniformLocation(vfPrograms,  "dCone"); 
		gl.glProgramUniform1i(vfPrograms,  cLoc,  drawCone);

		super.drawCone();
		
		drawCone = 0; // inform finishing draw cone
		cLoc = gl.glGetUniformLocation(vfPrograms,  "dCone"); 
		gl.glProgramUniform1i(vfPrograms,  cLoc,  drawCone);

	  }



	  public void drawSphere() {
	    int numofTriangle= 8*(int)Math.pow(4,depth); // number of triangles after subdivision
	    float vPoints[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex
	    float vNormals[] = new float[3*3*numofTriangle]; // 3 vertices each triangle, and 3 values each vertex
	    int drawSphere = 1; 

	    // start drawing a sphere
		int cLoc = gl.glGetUniformLocation(vfPrograms,  "dSphere"); 
		gl.glProgramUniform1i(vfPrograms,  cLoc,  drawSphere);

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
		
		// stop drawing a sphere
		drawSphere = 0; 
	    
		cLoc = gl.glGetUniformLocation(vfPrograms,  "dSphere"); 
		gl.glProgramUniform1i(vfPrograms,  cLoc,  drawSphere);

	  }

		 void drawSolar(float E, float e, float M, float m) {
			 float tiltAngle = 45; 
			 float tmp[] = {0, 0, 0, 1}; 
			 
			 	gl.glDisable(GL_BLEND);
				gl.glActiveTexture(GL_TEXTURE2);
				if (cnt % 1000 < 500) 
					gl.glBindTexture(GL_TEXTURE_2D,	cubeTexture);
				else gl.glBindTexture(GL_TEXTURE_2D,	cube1Texture);

				// Global coordinates
			    gl.glLineWidth(3);
			    //coordOff = false; // cjx
			    drawSphere(); // for loading matrix purpose
			    drawColorCoord(WIDTH/5, WIDTH/5, WIDTH/5);
			    
			    myPushMatrix();
				    myRotatef(e, 0.0f, 1.0f, 0.0f);
				    // rotating around the "sun"; proceed angle
				    myRotatef(tiltAngle*dg, 0.0f, 0.0f, 1.0f); // tilt angle
				    myTranslatef(0.0f, E, 0.0f);
				    myPushMatrix();
				    	myTranslatef(0.0f, E*3, 0.0f);
					    myScalef(WIDTH/1.5f, WIDTH/1.5f, WIDTH/1.5f);
					    drawSphere();
					    gl.glLineWidth(2);
					    drawColorCoord(2, 2, 2);
					    // get the center of the earth
					    myTransHomoVertex(tmp, earthC);		
					myPopMatrix();
				    					
				    myPushMatrix();
					    myScalef(E*1.5f, E, E*1.5f);
					    myRotatef(90*dg, 1.0f, 0.0f, 0.0f); // orient the cone
					    drawCone();
				    myPopMatrix();
				    
			    	myTranslatef(0.0f, E, 0.0f);
				    myPushMatrix();
				      cylinderm = cylinderm+cylinderD;
				      myRotatef(cylinderm, 0.0f, 1.0f, 0.0f);
				      // rotating around the "earth"
				      myTranslatef(M*3f, 0.0f, 0.0f);
				      myScalef(E/6, E/6, E/6);
					  // light source position
				      
				      // send the material property to the vertex shader
					  M_emission[0] = 1; // Material property: emission 
				 	  FloatBuffer cBuf = Buffers.newDirectFloatBuffer(M_emission);
					  int colorLoc = gl.glGetUniformLocation(vfPrograms,  "Me"); 
					  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
					  M_emission[0] = 0.1f;

					  drawCylinder();
					  gl.glLineWidth(1);
				      drawColorCoord(2, 2, 2);
				      // retrieve the center of the cylinder
				      // the matrix is stored column major left to right
					  myTransHomoVertex(tmp, cylinderC);
				    myPopMatrix();
				    myPushMatrix();
				      spherem = spherem+sphereD;
				      myRotatef(spherem, 0.0f, 1.0f, 0.0f);
				      // rotating around the "earth"
				      myTranslatef(M*3f, 0.0f, 0.0f);
				      myScalef(E/6, E/6, E/6);
				      
				      // send the material property to the vertex shader
					  M_emission[1] = 1; // Material property: emission 
				 	  cBuf = Buffers.newDirectFloatBuffer(M_emission);
					  colorLoc = gl.glGetUniformLocation(vfPrograms,  "Me"); 
					  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
					  M_emission[1] = 0.1f;
				      drawSphere();
				      drawColorCoord(2, 2, 2);
				      // retrieve the center of the sphere
					  myTransHomoVertex(tmp, sphereC);
					  myTransHomoVertex(tmp, L2_position);
				    myPopMatrix();

				    myPushMatrix();
				      conem = conem+coneD;
				      myRotatef(conem, 0.0f, 1.0f, 0.0f);
				      // rotating around the "earth"
				      myTranslatef(M*3f, 0.0f, 0.0f);
				      myScalef(E/6, E/6, E/6);
				      // send the material property to the vertex shader
					  M_emission[2] = 1; // Material property: emission 
				 	  cBuf = Buffers.newDirectFloatBuffer(M_emission);
					  colorLoc = gl.glGetUniformLocation(vfPrograms,  "Me"); 
					  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
					  M_emission[2] = 0.1f;
				      drawCone();
				      drawColorCoord(2, 2, 2);
				      // retrieve the center of the cone
					  myTransHomoVertex(tmp, coneC);
					  
					  // restore emission 
				 	  cBuf = Buffers.newDirectFloatBuffer(M_emission);
					  colorLoc = gl.glGetUniformLocation(vfPrograms,  "Me"); 
					  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

					  myPopMatrix();
				myPopMatrix();
				
	
			    if (distance(coneC, sphereC)<E/5) {
			        // collision detected, swap the rotation directions
			        float tmpD = coneD;
			        coneD = sphereD;
			        sphereD = tmpD;
			      }
			      if (distance(coneC, cylinderC)<E/5) {
			        // collision detected, swap the rotation directions
			        float tmpD = coneD;
			        coneD = cylinderD;
			        cylinderD = tmpD;
			      }
			      if (distance(cylinderC, sphereC)<E/5) {
			        // collision detected, swap the rotation directions
			        float tmpD = cylinderD;
			        cylinderD = sphereD;
			        sphereD = tmpD;
			      } 
		    }	  
	  

	  
	  
	public void init(GLAutoDrawable drawable) {
		
		gl = (GL4) drawable.getGL();
		String vShaderSource[], fShaderSource[] ;
					
		vShaderSource = readShaderSource("src/JOGL4_7_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/JOGL4_7_F.shader"); // read fragment shader
		vfPrograms = initShaders(vShaderSource, fShaderSource);		
		
		// 1. generate vertex arrays indexed by vao
		gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
		gl.glBindVertexArray(vao[0]); // use handle 0
		
		// 2. generate vertex buffers indexed by vbo: here vertices and colors
		gl.glGenBuffers(vbo.length, vbo, 0);
		
		// 3. enable VAO with loaded VBO data
		gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
		
		// if you don't use it, you should not enable it
		gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: Normal
					
		// if you don't use it, you should not enable it
		gl.glEnableVertexAttribArray(2); // enable the 2th vertex attribute: TextureCoord
					
		//4. specify drawing into only the back_buffer
		gl.glDrawBuffer(GL_BACK); 
		
		// 5. Enable zbuffer and clear framebuffer and zbuffer
		gl.glEnable(GL_DEPTH_TEST); 

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// read an image as texture
		Texture	joglTexture	=	loadTexture("src/gmu.jpg");						
		gmuTexture	=	joglTexture.getTextureObject(); 
		Texture	joglTexture1	=	loadTexture("src/VSE.jpg");						
		vseTexture	=	joglTexture1.getTextureObject(); 
		Texture	joglTexture2	=	loadTexture("src/earthCube.jpg");						
		cubeTexture	=	joglTexture2.getTextureObject(); 
		Texture	joglTexture3	=	loadTexture("src/natureCube.jpg");						
		cube1Texture	=	joglTexture3.getTextureObject(); 

		// activate texture #0 and bind it to the texture object
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D,	gmuTexture);
		gl.glActiveTexture(GL_TEXTURE1);
		gl.glBindTexture(GL_TEXTURE_2D,	vseTexture);
		gl.glActiveTexture(GL_TEXTURE2);
		gl.glBindTexture(GL_TEXTURE_2D,	cubeTexture);
		gl.glBindTexture(GL_TEXTURE_2D,	cube1Texture);
		

		}


	  public static void main(String[] args) {
	    new JOGL4_7_TexObjects();
	  }

}
