/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */


import com.jogamp.opengl.*;
import com.jogamp.opengl.util.gl2.GLUT;


public class JOGL2_10_GenSolar extends JOGL2_9_Solar {
  
	  static float tiltAngle = 45;
	  static float dg = (float) Math.PI/180f; // degree to gradian
	  
	  void drawSolar(float earthDistance,
	                 float earthAngle,
	                 float moonDistance,
	                 float moonAngle) {


	    // Global coordinates
		  drawSphere(); // a tiny sphere, for loading the matrix for drawing the coordinates
		  gl.glLineWidth(3);
	      drawColorCoord(WIDTH/4, WIDTH/4, WIDTH/4);

	    myPushMatrix();
	    myRotatef(earthAngle*dg, 0.0f, 1.0f, 0.0f);
	    // rotating around the "sun"; proceed angle
	    myRotatef(tiltAngle*dg, 0.0f, 0.0f, 1.0f);
	    // tilt angle, angle between the center line and y axis
	    //gl.glBegin(GL.GL_LINES);
	    //gl.glVertex3f(0.0f, 0.0f, 0.0f);
	    //gl.glVertex3f(0.0f, earthDistance, 0.0f);
	    //gl.glEnd();
	    myTranslatef(0.0f, earthDistance, 0.0f);
	    // cjx gl.glLineWidth(3);
	    myPushMatrix();
	    myScalef(WIDTH/20, WIDTH/20, WIDTH/20);
	    drawSphere();
	    drawColorCoord(2, 5, 2);
	    myPopMatrix();

	    myRotatef(moonAngle*dg, 0.0f, 1.0f, 0.0f);
	    // rotating around the "earth"
	    myTranslatef(moonDistance, 0.0f, 0.0f);
	    gl.glLineWidth(3);
	    myScalef(WIDTH/40, WIDTH/40, WIDTH/40);
	    drawSphere();
	    drawColorCoord(2, 2, 2);
	    myPopMatrix();
	  }
	  
	  
	  public static void main(String[] args) {
	    new JOGL2_10_GenSolar();
	  }

}
