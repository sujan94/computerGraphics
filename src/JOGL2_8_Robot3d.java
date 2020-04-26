/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */


import com.jogamp.opengl.*;

public class JOGL2_8_Robot3d extends JOGL2_7_Sphere {
  
	  float O = 0;
	  float A = (float)0.3*WIDTH;
	  float B = (float)0.55*WIDTH;
	  float C = (float)0.7*WIDTH;


	  public void display(GLAutoDrawable glDrawable) {

	    depth = (cnt/100)%7;
	    cnt++;
	    alpha += dalpha/40f;
	    beta += dbeta/40f;
	    gama += dgama/20f;

	    gl.glClear(GL.GL_COLOR_BUFFER_BIT|
	               GL.GL_DEPTH_BUFFER_BIT);

	    // the robot arm is rotating around y axis
	    myRotatef(0.02f, 0.0f, 1.0f, 0.0f);
	    drawRobot(O, A, B, C, alpha, beta, gama);
	  }


	  void drawArm(float End1, float End2) {

	    float scale = End2-End1;

	    myPushMatrix();
	    // the cylinder lies in the z axis;
	    // rotate it to lie in the x axis
	    myRotatef((float) Math.PI/2f, 0.0f, 1.0f, 0.0f); // using gradian instead of degree in our own implementation
	    myScalef(scale/5.0f, scale/5.0f, scale);
	    if (cnt%1500<500) {
	      drawCylinder();
	    } else if (cnt%1500<1000) {
	      drawCone();
	    } else {
	      myScalef(0.5f, 0.5f, 0.5f);
	      myTranslatef(0, 0, 1);
	      drawSphere();
	    }
	    myPopMatrix();
	  }


	  void drawRobot(float O, float A, float B, float C,
	                 float alpha, float beta, float gama) {
	    myPushMatrix();

	    myRotatef(alpha, 0.0f, 0.0f, 1.0f);
	    // R_z(alpha) is on top of the matrix stack
	    drawArm(O, A);

	    myTranslatef(A, 0.0f, 0.0f);
	    myRotatef(beta, 0.0f, 0.0f, 1.0f);
	    // R_z(alpha)T_x(A)R_z(beta) is on top of the stack
	    drawArm(A, B);

	    myTranslatef(B-A, 0.0f, 0.0f);
	    myRotatef(gama, 0.0f, 0.0f, 1.0f);
	    // R_z(alpha)T_x(A)R_z(beta)T_x(B)R_z(gama) is on top
	    drawArm(B, C);

	    myPopMatrix();
	  }

	  
	  
  public static void main(String[] args) {
    new JOGL2_8_Robot3d();
  }

}
