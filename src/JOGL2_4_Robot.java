/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */


import com.jogamp.opengl.*;


public class JOGL2_4_Robot extends JOGL2_3_Robot2d {
	
	  public void display(GLAutoDrawable glDrawable) {

		    gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		    alpha += dalpha/50f;
		    beta += dbeta/50f;
		    gama += dgama/50f;

		    gl.glLineWidth(7f); // draw a wide line for arm
		   drawRobot(A, B, C, alpha, beta, gama);
		  }


		  void drawRobot(
		      float A[],
		      float B[],
		      float C[],
		      float alpha,
		      float beta,
		      float gama) {

		    //myRotatef(0.1f, 0.0f, 1.0f, 0.0f); // this will accumulate on the current matrix
		    myPushMatrix();
		    	myRotatef(0.1f, 0.0f, 1.0f, 0.0f); // this is a fixed rotation around y

		         // gl.glColor3f(1, 1, 0); 
			    color[0] = 1;  color[1] = 1;   color[2] = 0; 
			    myRotatef(alpha, 0.0f, 0.0f, 1.0f);
			    // R_z(alpha) is on top of the matrix stack
			    transDrawArm(O, A);
	
			    //gl.glColor3f(0, 1, 1);
			    color[0] = 0;  color[1] = 1;   color[2] = 1; 
			    myTranslatef(A[0], A[1], 0.0f);
			    myRotatef(beta, 0.0f, 0.0f, 1.0f);
			    myTranslatef(-A[0], -A[1], 0.0f);
			    // R_z(alpha)T(A)R_z(beta)T(-A) is on top
			    transDrawArm(A, B);
	
			    //gl.glColor3f(1, 0, 1);
			    color[0] = 1;  color[1] = 0;   color[2] = 1; 
			    myTranslatef(B[0], B[1], 0.0f);
			    myRotatef(gama, 0.0f, 0.0f, 1.0f);
			    myTranslatef(-B[0], -B[1], 0.0f);
			    // R_z(alpha)T(A)R_z(beta)T(-A) is on top
			    transDrawArm(B, C);

		    myPopMatrix();
		  }

		  
		  public void init(GLAutoDrawable drawable) {

			    super.init(drawable);
			    myLoadIdentity();
			  }


  public static void main(String[] args) {
    new JOGL2_4_Robot();
  }

}
