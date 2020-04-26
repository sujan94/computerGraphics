/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */


import com.jogamp.opengl.GLAutoDrawable;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL4.*; 


public class JOGL2_13_TravelSolar extends JOGL2_12_RobotSolar {
  
 		  public void display(GLAutoDrawable glDrawable) {
 			    cnt++;
 			    depth = (cnt/100)%7;

  			    if (cnt%150==0) {
 			      dalpha = -dalpha;
 			      dbeta = -dbeta;
 			      dgama = -dgama;
 			    }
 			    alpha += dalpha;
 			    beta += dbeta;
 			    gama += dgama;

			    gl.glClear(GL_COLOR_BUFFER_BIT|
			               GL_DEPTH_BUFFER_BIT);


 			    myPushMatrix();
 			      // moving the camera from the origin to the moon
 			      // the transformation is reasoned from top down
 			    if (cnt%2000<1111) 
 				 myCamera(WIDTH/3, 2f*cnt, WIDTH/3, spherem); 			    
 			    // drawSolar(WIDTH/4, 2*cnt*dg, WIDTH/6, cnt*dg);
 			     drawRobot(O, A, B, C, alpha*dg, beta*dg, gama*dg);
 			    myPopMatrix();
		  }

 		  void myCamera(
 			      float E,
 			      float e,
 			      float M,
 			      float m) {
 			  float tiltAngle = 45; 

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
 			    
 			    // and reversing the robot transformation
 			    myTranslatef(-C+B, 0, 0);
 			    myRotatef(-gama*dg, 0, 0, 1);
 			    myTranslatef(-B+A, 0, 0);
 			    myRotatef(-beta*dg, 0, 0, 1);
 			    myTranslatef(-A, 0, 0);
 			    myRotatef(-alpha*dg, 0, 0, 1);
 			    myRotatef(-cnt*dg, 0, 1, 0);			  }


	  
	  public static void main(String[] args) {
	    new JOGL2_13_TravelSolar();
	  }

}
