/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */


import com.jogamp.opengl.GLAutoDrawable;
import static com.jogamp.opengl.GL4.*; 


public class JOGL2_12_RobotSolar extends JOGL2_11_ConeSolar {
  
    // replace with a perspective projection
	public void reshape(
		      GLAutoDrawable glDrawable, int x, int y, int w, int h) {

		    WIDTH = w; HEIGHT = h;
		 
		    // enable zbuffer for hidden-surface removal
		    gl.glEnable(GL_DEPTH_TEST);

		    // specify the drawing area within the frame window
		    gl.glViewport(0, 0, w, h);

		    // projection is carried on the projection matrix
		    //gl.glMatrixMode(GL.GL_PROJECTION);
		    myLoadIdentity();
		    
		    myFrustum(-w/4,w/4,-h/4,h/4,w/2,4*w);
		    myTranslatef(0, 0, -2*w);
			get_Matrix(PROJ); 
			
			// connect the PROJECTION matrix to the vertex shader
			int projLoc = gl.glGetUniformLocation(vfPrograms,  "proj_matrix"); 
			gl.glProgramUniformMatrix4fv(vfPrograms, projLoc,  1,  false,  PROJ, 0);

		    // transformations are on the modelview matrix
		    //gl.glMatrixMode(GL_MODELVIEW);
		    myLoadIdentity();
			gl.setSwapInterval(1);

		  }
	
  	// multiply the current matrix with the orthographic projection matrix
    void myFrustum(float l, float r, float b, float t, float n, float f) {// look at z near and far
    	float frustumMatrix[][] = {
    			{2f*n/(r-l),  		0, 			(r+l)/(r-l), 		0}, 
    			{       0,   2f*n/(t-b), 	  	(t+b)/(t-b), 		0}, 
    			{       0,          0,  	   -(f+n)/(f-n),     -2*f*n/(f-n)}, 
    			{       0, 		    0, 		             -1, 		0}
    	}; 	    	
    	myMultMatrix(frustumMatrix); 
    } 



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

		    drawRobot(O, A, B, C, alpha, beta, gama);

		  }


		  void drawRobot (
		      float O,
		      float A,
		      float B,
		      float C,
		      float alpha,
		      float beta,
		      float gama) {

		    // Global coordinates
		    gl.glLineWidth(4);
		    drawSphere(); 
		    drawColorCoord(WIDTH/4, WIDTH/4, WIDTH/4);

		    myPushMatrix();

		    myRotatef(cnt*dg, 0, 1, 0);
		    myRotatef(alpha*dg, 0, 0, 1);
		    // R_z(alpha) is on top of the matrix stack
		    drawArm(O, A);

		    myTranslatef(A, 0, 0);
		    myRotatef(beta*dg, 0, 0, 1);
		    // R_z(alpha)T_x(A)R_z(beta) is on top of the stack
		    drawArm(A, B);
//		    drawSolar(WIDTH/4, 2.5f*cnt, WIDTH/6, 1.5f*cnt);

		    myTranslatef(B-A, 0, 0);
		    myRotatef(gama*dg, 0, 0, 1);
		    // R_z(alpha)T_x(A)R_z(beta)T_x(B)R_z(gama) is on top
		    drawArm(B, C);

		    // put the solar system at the end of the robot arm
		    myTranslatef(C-B, 0, 0);
		    drawSolar(WIDTH/4, 2.5f*cnt, WIDTH/6, 1.5f*cnt);

		    myPopMatrix();
		  }

	  
	  
	  public static void main(String[] args) {
	    new JOGL2_12_RobotSolar();
	  }

}
