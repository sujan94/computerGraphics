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



public class JOGL4_1_Blending extends JOGL3_11_Phong {
 
	  float M_emission[] = {0.1f, 0.1f, 0.1f, 0.1f}; // Material property: emission 
	  float L_ambient[] = {0.2f, 0.2f, 0.2f, 1}; // Light source property: ambient 
	  float M_ambient[] = {0.2f, 0.2f, 0.2f, 0.1f}; // Material property: ambient 
	  float L_diffuse[] = {1, 1, 1, 1}; // Light source property: diffuse 
	  float M_diffuse[] = {.5f, 0.5f, 0.5f, 0.4f}; // Material property: diffuse 
	  float L_position[] = {4*WIDTH, 4*WIDTH, 4*WIDTH, 1}; // Light source property: position

	  float L_specular[] = {1, 1, 1, 1}; // Light source property: specular 
	  float M_specular[] = {1f, 1f, 1f, 0.8f}; // Material property: specular 
	  float M_shininess = 50; // Material property: shininess 
	  float V_position[] = {0, 0, 2*WIDTH, 1}; // View position

	  float L1_diffuse[] = {1, 0.1f, 0.1f, 0.1f}; // Light source property: diffuse 
	  float L1_position[] = {4*WIDTH, 4*WIDTH, 4*WIDTH, 1}; // Light source property: position
	  float L2_diffuse[] = {0.1f, 1, 0.1f, 0.1f}; // Light source property: diffuse 
	  float L2_position[] = {4*WIDTH, 4*WIDTH, 4*WIDTH, 1}; // Light source property: position
	  float L3_diffuse[] = {0.1f, 0.1f, 1, 0.1f}; // Light source property: diffuse 
	  float L3_position[] = {4*WIDTH, 4*WIDTH, 4*WIDTH, 1}; // Light source property: position
	  
	  
		 void drawSolar(float E, float e, float M, float m) {

			 float tiltAngle = 45; 
			 float tmp[] = {0, 0, 0, 1}; 

			   // enable blending for solar system

			    gl.glEnable(GL_BLEND);
			    gl.glBlendFunc(GL_SRC_ALPHA,
			                   GL_ONE_MINUS_SRC_ALPHA);
			    
			   
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
				    myTranslatef(0.0f, 0.5f*E, 0.0f);
					    myScalef(E/4, 1.5f*E, E/4);
					    myRotatef(90*dg, 1.0f, 0.0f, 0.0f); // orient the cone
					    drawCone();
				    myPopMatrix();
				    
				   gl.glDepthMask(false);  // only the big cone and spheres are not blocking

				    myPushMatrix();
				    myScalef(1.5f*E, E, 1.5f*E);
				    myRotatef(90*dg, 1.0f, 0.0f, 0.0f); // orient the cone
				    drawCone();
				    myPopMatrix();
			    
				    myTranslatef(0.0f, 0.5f*E, 0.0f);

				    myPushMatrix();
				    	myTranslatef(0.0f, E, 0.0f);
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
				      myTranslatef(M*2.5f, 0.0f, 0.0f);
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
				      myTranslatef(M*2.5f, 0.0f, 0.0f);
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
				      myTranslatef(M*2.5f, 0.0f, 0.0f);
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

	  
	  
	
	  public void display(GLAutoDrawable glDrawable) {
		    cnt++;
			depth = (cnt/100)%7;

		    if (cnt%60==0) {
		      dalpha = -dalpha;
		      dbeta = -dbeta;
		      dgama = -dgama;
		    }
		    alpha += dalpha;
		    beta += dbeta;
		    gama += dgama;
		    
		    gl.glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);


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

			
	 		cBuf = Buffers.newDirectFloatBuffer(L_specular);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ls"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

	 		cBuf = Buffers.newDirectFloatBuffer(M_specular);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Ms"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
			
			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Msh"); 
			gl.glProgramUniform1f(vfPrograms,  colorLoc,  M_shininess);

			cBuf = Buffers.newDirectFloatBuffer(V_position);

			//Connect JOGL variable with shader variable by name
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "Vsp"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

			
	 		cBuf = Buffers.newDirectFloatBuffer(L1_diffuse);
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "L1d"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
			
	 		cBuf = Buffers.newDirectFloatBuffer(L2_diffuse);
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "L2d"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
			
	 		cBuf = Buffers.newDirectFloatBuffer(L3_diffuse);
			colorLoc = gl.glGetUniformLocation(vfPrograms,  "L3d"); 
			gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

			  // send the lightsource position to the vertex shader
			  L1_position[0] = cylinderC[0];
			  L1_position[1] = cylinderC[1];
			  L1_position[2] = cylinderC[2];
		 	  cBuf = Buffers.newDirectFloatBuffer(L1_position);
			  colorLoc = gl.glGetUniformLocation(vfPrograms,  "L1sp"); 
			  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

			  L2_position[0] = sphereC[0];
			  L2_position[1] = sphereC[1];
			  L2_position[2] = sphereC[2];
		 	  cBuf = Buffers.newDirectFloatBuffer(L2_position);
			  colorLoc = gl.glGetUniformLocation(vfPrograms,  "L2sp"); 
			  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);

			  L3_position[0] = coneC[0];
			  L3_position[1] = coneC[1];
			  L3_position[2] = coneC[2];
		 	  cBuf = Buffers.newDirectFloatBuffer(L3_position);
			  colorLoc = gl.glGetUniformLocation(vfPrograms,  "L3sp"); 
			  gl.glProgramUniform4fv(vfPrograms,  colorLoc, 1, cBuf);
			
			    myPushMatrix();
			    if (cnt%1500<750) 
				 myCamera(WIDTH/3, 2f*cnt, WIDTH/3, spherem); 			    
			    // drawSolar(WIDTH/4, 2*cnt*dg, WIDTH/6, cnt*dg);
			     drawRobot(O, A, B, C, alpha*dg, beta*dg, gama*dg);
			    myPopMatrix();
	  }

	  
	  
	  
	  public static void main(String[] args) {
	    new JOGL4_1_Blending();
	  }

}
