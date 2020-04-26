/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */


import com.jogamp.opengl.GLAutoDrawable;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL4.*; 


public class JOGL2_13_TravelSolar extends JOGL2_11_ConeSolar {
  
 		  public void display(GLAutoDrawable glDrawable) {
 			    cnt++;
 			    depth = (cnt/50)%7;

 			    gl.glClear(GL_COLOR_BUFFER_BIT|
 			               GL_DEPTH_BUFFER_BIT);

 			    if (cnt%60==0) {
 			      dalpha = -dalpha;
 			      dbeta = -dbeta;
 			      dgama = -dgama;
 			    }
 			    alpha += dalpha;
 			    beta += dbeta;
 			    gama += dgama;


 			    myPushMatrix();
 			      // moving the camera from the origin to the moon
 			      // the transformation is reasoned from top down
 			   if (cnt%500<250) 
 				   myCamera(WIDTH/4, 2.5f*cnt, WIDTH/6, 1.5f*cnt); 			    
 			     drawSolar(WIDTH/4, 2.5f*cnt, WIDTH/6, 1.5f*cnt);
 			     //drawRobot(O, A, B, C, alpha, beta, gama);
 			    myPopMatrix();
		  }

 		  void myCamera(
 			      float E,
 			      float e,
 			      float M,
 			      float m) {

			    //1. camera faces the negative x axis
 			    myRotatef(-90*dg, 0, 1, 0);

 			    //2. camera on positive x axis
 			    myTranslatef(-M, 0, 0);

 			    //3. camera rotates around y axis (with the moon) 
 			    myRotatef(-m*dg, 0, 1, 0);

 			    // and so on reversing the solar transformation
 			    myTranslatef(0, -E, 0);
 			    myRotatef(-tiltAngle*dg, 0, 0, 1); // tilt angle
 			    // rotating around the "sun"; proceed angle
 			    myRotatef(-e*dg, 0, 1, 0);
 			    
 			  }


	  
	  public static void main(String[] args) {
	    new JOGL2_13_TravelSolar();
	  }

}
