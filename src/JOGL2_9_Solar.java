/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */




import com.jogamp.opengl.*;
import com.jogamp.opengl.util.gl2.GLUT;


public class JOGL2_9_Solar extends JOGL2_8_Robot3d {
  
	  static boolean coordOff=false; // cjx for images

	  public void display(GLAutoDrawable glDrawable) {

	    depth = (cnt/100)%7;
	    cnt++;

	    gl.glClear(GL.GL_COLOR_BUFFER_BIT|
	               GL.GL_DEPTH_BUFFER_BIT);

//	    myRotatef(0.01f, 0.5f, 0f, 0.5f); 
	    drawSolar(WIDTH/4, 2*cnt, WIDTH/12, cnt);
	  }


	  // notice that draw drawColorCoord didn't load the MV matrix, so it has to be following drawSphere(), which loads MV matrix
	  public void drawColorCoord(float xlen, float ylen,
	                             float zlen) {
	    GLUT glut = new GLUT();

	   // if (coordOff) return; // cjx for images

	    float vColor[] = {1, 0, 0}; 
	    float vPoint[] = {0, 0, 0, 0, 0, 0}; 
	    
	    vPoint[3] = xlen; 
	    drawLineJOGL(vPoint, vColor); 
	    vColor[0] = 0; vColor[1] = 1; 
	    vPoint[3] = 0; 
	    vPoint[4] = ylen; 
	    drawLineJOGL(vPoint, vColor); 
	    vColor[1] = 0; vColor[2] = 1; 
	    vPoint[4] = 0; 
	    vPoint[5] = zlen; 
	    drawLineJOGL(vPoint, vColor); 
	    
/*
	    gl.glBegin(GL.GL_LINES);
	    gl.glColor3f(1, 0, 0);
	    gl.glVertex3f(0, 0, 0);
	    gl.glVertex3f(0, 0, zlen);
	    gl.glColor3f(0, 1, 0);
	    gl.glVertex3f(0, 0, 0);
	    gl.glVertex3f(0, ylen, 0);
	    gl.glColor3f(0, 0, 1);
	    gl.glVertex3f(0, 0, 0);
	    gl.glVertex3f(xlen, 0, 0);
	    gl.glEnd();

	    
	    // coordinate labels: X, Y, Z
	    myPushMatrix();
	    myTranslatef(xlen, 0, 0);
	    myScalef(xlen/WIDTH, xlen/WIDTH, 1);
	    glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, 'X');
	    myPopMatrix();

	    myPushMatrix();
	   // myColor3f(0, 1, 0);
	    myTranslatef(0, ylen, 0);
	    myScalef(ylen/WIDTH, ylen/WIDTH, 1);
	    glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, 'Y');
	    myPopMatrix();

	    myPushMatrix();
	    //myColor3f(1, 0, 0);
	    myTranslatef(0, 0, zlen);
	    myScalef(zlen/WIDTH, zlen/WIDTH, 1);
	    glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, 'Z');
	    myPopMatrix();
	    
	    
	    */
	  }

	  
	  void drawSolar(float E, float e, float M, float m) {

		 // drawColorCoord(WIDTH/8, WIDTH/8, WIDTH/8);

		    myPushMatrix();
		    
			    myPushMatrix();
				    myScalef(WIDTH/15f, WIDTH/15f, WIDTH/15f);
				    drawSphere();
					drawColorCoord(4f, 4f, 4f);
			    myPopMatrix();
	
	
			    myRotatef(e/60f, 0.0f, 1.0f, 0.0f);
			    // rotating around the "sun"; proceed angle
			    myTranslatef(E, 0.0f, 0.0f);
	
	//		    drawColorCoord(WIDTH/6, WIDTH/6, WIDTH/6);
			    myPushMatrix();
				    myScalef(WIDTH/20f, WIDTH/20f, WIDTH/20f);
				    myRotatef((e+m)/60f, 0.0f, 1.0f, 0.0f); //earth's self rotation
				    drawSphere();
					drawColorCoord(3f, 3f, 3f);
			    myPopMatrix();
	
			    myRotatef(m/60f, 0.0f, 1.0f, 0.0f);
			    // rotating around the "earth"
			    myTranslatef(M, 0.0f, 0.0f);
			    myScalef(WIDTH/40f, WIDTH/40f, WIDTH/40f);
			    drawSphere();
			    drawColorCoord(3f, 3f, 3f);

		    myPopMatrix();
		  }

	  
	  
  public static void main(String[] args) {
    new JOGL2_9_Solar();
  }

}
