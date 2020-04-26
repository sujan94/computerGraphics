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


public class JOGL4_8_Mipmap extends JOGL4_7_TexObjects {
	
	  public void drawSphere() {
		  myPushMatrix();
		  	myRotatef(-45*dg, 0, 1, 0); 
		  	myRotatef(-135*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(-45*dg, 0, 1, 0); 
		  	myRotatef(135*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(45*dg, 0, 1, 0); 
		  	myRotatef(-135*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(45*dg, 0, 1, 0); 
		  	myRotatef(135*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(-45*dg, 0, 1, 0); 
		  	myRotatef(-45*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(-45*dg, 0, 1, 0); 
		  	myRotatef(45*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(45*dg, 0, 1, 0); 
		  	myRotatef(-45*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(45*dg, 0, 1, 0); 
		  	myRotatef(45*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.08f, 0.08f, 1f); 
		  	drawCone(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(270*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.05f, 0.05f, 0.8f); 
		  	drawCylinder(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(90*dg, 1, 0, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.05f, 0.05f, 0.8f); 
		  	drawCylinder(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(270*dg, 0, 1, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.05f, 0.05f, 0.8f); 
		  	drawCylinder(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(180*dg, 0, 1, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.05f, 0.05f, 0.8f); 
		  	drawCylinder(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		  	myRotatef(90*dg, 0, 1, 0); 
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.05f, 0.05f, 0.8f); 
		  	drawCylinder(); 		  	
		  myPopMatrix(); 
		  myPushMatrix();
		    myTranslatef(0, 0, 0.8f); 
		    myScalef(0.05f, 0.05f, 0.8f); 
		  	drawCylinder(); 		  	
		  myPopMatrix(); 
		  super.drawSphere();
	  }
  
	 void drawSolar(float E, float e, float M, float m) {
		 float tmp[] = {0, 0, 0, 1}; 
		 
		    gl.glDisable(GL_BLEND);
		   	gl.glDepthMask(true);  
		   	
		    // Global coordinates
		    gl.glLineWidth(3);
		    //coordOff = false; // cjx
		    drawSphere(); // for loading matrix purpose
		    drawColorCoord(WIDTH/5, WIDTH/5, WIDTH/5);
		    
		    myPushMatrix();
			    myRotatef(e, 0.0f, 1.0f, 0.0f);
			    // rotating around the "sun"; proceed angle
			    float tiltAngle = 45; 
			    myRotatef(tiltAngle*dg, 0.0f, 0.0f, 1.0f); // tilt angle
			    myTranslatef(0.0f, 1.2f*E, 0.0f);
			    					
			    myPushMatrix();
			    myTranslatef(0.0f, 0.5f*E, 0.0f);
				    myScalef(E/8, E, E/8);
				    myRotatef(90*dg, 1.0f, 0.0f, 0.0f); // orient the cone
				    drawCone();
			    myPopMatrix();
			    
			    myPushMatrix();
			    myTranslatef(0.0f, -0.7f*E, 0.0f);
			    myScalef(2f*E, 0.5f*E, 2f*E);
			    myRotatef(90*dg, 1.0f, 0.0f, 0.0f); // orient the cone
			    drawCone();
			    myPopMatrix();
		    
			    myTranslatef(0.0f, 0.5f*E, 0.0f);

			    myPushMatrix();
			    	myTranslatef(0.0f, 1.5f*E, 0.0f);
				    myScalef(WIDTH/3, WIDTH/3, WIDTH/3);
				    drawSphere();
				    gl.glLineWidth(2);
				    drawColorCoord(2, 2, 2);
				    // get the center of the earth
				    myTransHomoVertex(tmp, earthC);		
			    myPopMatrix();

		  
			    myPushMatrix();
			      cylinderm = cylinderm+cylinderD;
			      myRotatef(cylinderm, 0.0f, 1.0f, 0.0f);
			      // rotating around the "earth"
			      myTranslatef(M*1.5f, 0.0f, 0.0f);
			      myScalef(E/12, E/12, E/12);
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
			      myTranslatef(M*1.5f, 0.0f, 0.0f);
			      myScalef(E/12, E/12, E/12);
			      
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
			      myTranslatef(M*1.5f, 0.0f, 0.0f);
			      myScalef(E/12, E/12, E/12);
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
		    gl.glDepthMask(true);  
		    gl.glDisable(GL_BLEND);


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
		joglTexture	=	loadTexture("src/VSE.jpg");						
		vseTexture	=	joglTexture.getTextureObject(); 
		joglTexture	=	loadTexture("src/earthCube.jpg");						
		cubeTexture	=	joglTexture.getTextureObject(); 
		joglTexture	=	loadTexture("src/natureCube.jpg");						
		cube1Texture	=	joglTexture.getTextureObject(); 

		// activate texture #0 and bind it to the texture object
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D,	gmuTexture);
		
		gl.glActiveTexture(GL_TEXTURE1);
		gl.glBindTexture(GL_TEXTURE_2D,	vseTexture);

		/*
        // This portion is about filters, and anisotropy
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		gl.glHint(GL_GENERATE_MIPMAP_HINT, GL_NICEST); 
		gl.glGenerateMipmap(GL_TEXTURE_2D);			
		
		if (gl.isExtensionAvailable("GL_EXT_texture_filter_anisotropic"))
		{ float max[ ] = new float[1];
			gl.glGetFloatv(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max, 0); // maximum number of anisotropic sizes/images
			gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, max[0]);

		}
		
		*/
		
		gl.glActiveTexture(GL_TEXTURE2);
		gl.glBindTexture(GL_TEXTURE_2D,	cubeTexture);
		//gl.glBindTexture(GL_TEXTURE_2D,	cube1Texture);
		

		}


	  public static void main(String[] args) {
	    new JOGL4_8_Mipmap();
	  }

}
