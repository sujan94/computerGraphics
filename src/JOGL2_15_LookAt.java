/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 * Demonstrate myPerspective
 * 
 */


import com.jogamp.opengl.GLAutoDrawable;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL4.*;


public class JOGL2_15_LookAt extends JOGL2_14_Perspective {
    int w = WIDTH, h = HEIGHT;

	public void display(GLAutoDrawable glDrawable) {

		    cnt++;
		    depth = (cnt/150)%6;
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

		    viewPort1();
		    viewPort2();
		    viewPort3();
		    viewPort4();
		  }


		  public void viewPort1() {

		    gl.glViewport(0, 0, w/2, h/2);

		    drawRobot(O, A, B, C, alpha*dg, beta*dg, gama*dg);
		    myPushMatrix(); 
		    // the objects' centers are retrieved here
		    myLoadIdentity(); 
		    drawRobot(O, A, B, C, alpha*dg, beta*dg, gama*dg);
			myPopMatrix(); 
		  }


		  public void viewPort2() {
		    int w = WIDTH, h = HEIGHT;

		    gl.glViewport(w/2, 0, w/2, h/2);
		    myPushMatrix(); 
	    		myTranslatef(0, 0, -w); 
				myLookAt(coneC[0], coneC[1], coneC[2], earthC[0], earthC[1], earthC[2], earthC[0], earthC[1], earthC[2]);		    
			    drawRobot(O, A, B, C, alpha*dg, beta*dg, gama*dg);
			    myPushMatrix(); 
				    myLoadIdentity(); 
				    // the objects' centers are retrieved here
					drawSolar(WIDTH/4, 2.5f*cnt*dg, WIDTH/6, cnt*dg);
				myPopMatrix(); 
			myPopMatrix(); 

		    
		  }


		  public void viewPort3() {

			    gl.glViewport(w/2, h/2, w/2, h/2);
			    myPushMatrix(); 
			    // earthC retrieved in drawSolar() before viewPort2
				myLookAt(earthC[0], earthC[1], earthC[2], 0, 0, 0, 0, 1, 0);
				drawSolar(WIDTH/4, 2.5f*cnt*dg, WIDTH/6, cnt*dg);
				myPopMatrix(); 
		 }

		  
		  public void viewPort4() {
		    int w = WIDTH, h = HEIGHT;
		    
		    myPushMatrix(); 
		    // earthC retrieved in drawSolar() before viewPort2
		    myLoadIdentity(); 
		    drawSolar(WIDTH/4, 2.5f*cnt*dg, WIDTH/6, cnt*dg);
			myPopMatrix(); 

		    gl.glViewport(0, h/2, w/2, h/2);
		    myPushMatrix(); 
			    // earthC retrieved in drawSolar() before viewPort3
			    mygluLookAt(sphereC[0], sphereC[1], sphereC[2], earthC[0], earthC[1], earthC[2], earthC[0], earthC[1], earthC[2]);
				drawSolar(WIDTH/4, 2.5f*cnt*dg, WIDTH/6, cnt*dg);
		    myPopMatrix(); 
		}


		  public void myLookAt(
		      double eX, double eY, double eZ,
		      double cX, double cY, double cZ,
		      double upX, double upY, double upZ) {
		    //eye and center are points, but up is a vector

		    //1. change center into a vector:
		    // glTranslated(-eX, -eY, -eZ);
		    cX = cX-eX;
		    cY = cY-eY;
		    cZ = cZ-eZ;

		    //2. The angle of center on xz plane and x axis
		    // i.e. angle to rot so center in the neg. yz plane
		    double a = Math.atan(cZ/cX);
		    if (cX==0) a = 0; 
		    else 
		    if (cX>=0) {
		      a = a+Math.PI/2;
		    } else {
		      a = a-Math.PI/2;
		    }

		    //3. The angle between the center and y axis
		    // i.e. angle to rot so center in the negative z axis
		    double b = Math.acos(cY/Math.sqrt(cX*cX+cY*cY+cZ*cZ));
		    b = b-Math.PI/2;

		    //4. up rotate around y axis (a) radians
		    double upx = upX*Math.cos(a)+upZ*Math.sin(a);
		    double upz = -upX*Math.sin(a)+upZ*Math.cos(a);
		    upX = upx;
		    upZ = upz;

		    //5. up rotate around x axis (b) radians
		    double upy = upY*Math.cos(b)-upZ*Math.sin(b);
		    upz = upY*Math.sin(b)+upZ*Math.cos(b);
		    upY = upy;
		    upZ = upz;

		    double c = Math.atan(upX/upY);
		    if (upY<0) {
		      //6. the angle between up on xy plane and y axis
		      c = c+Math.PI;
		    }
		    myRotatef((float) c, 0, 0, 1);
		    // up in yz plane
		    myRotatef((float) b, 1, 0, 0);
		    // center in negative z axis
		    myRotatef((float) a, 0, 1, 0);
		    //center in yz plane
		    myTranslatef((float)-eX, (float)-eY, (float)-eZ);
		    //eye at the origin
		  }


		  public void mygluLookAt(
		      double eX, double eY, double eZ,
		      double cX, double cY, double cZ,
		      double upX, double upY, double upZ) {
		    //eye and center are points, but up is a vector

		    double[] F = new double[3];
		    double[] UP = new double[3];
		    double[] s = new double[3];
		    double[] u = new double[3];

		    F[0] = cX-eX;
		    F[1] = cY-eY;
		    F[2] = cZ-eZ;
		    UP[0] = upX;
		    UP[1] = upY;
		    UP[2] = upZ;
		    normalize(F);
		    normalize(UP);
		    crossProd(F, UP, s);
		    crossProd(s, F, u);

		    float[][] M = new float[4][4];

		    M[0][0] = (float) s[0];
		    M[1][0] = (float)u[0];
		    M[2][0] = (float)-F[0];
		    M[3][0] = (float)0;
		    M[0][1] = (float)s[1];
		    M[1][1] = (float)u[1];
		    M[2][1] = (float)-F[1];
		    M[3][1] = (float)0;
		    M[0][2] = (float)s[2];
		    M[1][2] = (float)u[2];
		    M[2][2] = (float)-F[2];
		    M[3][2] = 0;
		    M[0][3] = 0;
		    M[1][3] = 0;
		    M[2][3] = 0;
		    M[3][3] = 1;

		    myMultMatrix(M);
		    myTranslatef((float)-eX, (float)-eY, (float)-eZ);
		  }


		  public void normalize(double v[]) {

		    double d = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);

		    if (d==0) {
		      System.out.println("0 length vector: normalize().");
		      return;
		    }
		    v[0] /= d;
		    v[1] /= d;
		    v[2] /= d;
		  }


		  public void crossProd(double U[],
		                        double V[], double W[]) {
		    // W = U X V
		    W[0] = U[1]*V[2]-U[2]*V[1];
		    W[1] = U[2]*V[0]-U[0]*V[2];
		    W[2] = U[0]*V[1]-U[1]*V[0];
		  }


	  
	  public static void main(String[] args) {
	    new JOGL2_15_LookAt();
	  }

}
