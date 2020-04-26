/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_LINES;

import java.awt.event.*;
//import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;


public class JOGL2_2_ReshapePushPop extends JOGL2_2_Reshape  {
	
  // the point to be dragged as the lowerleft corner
  private static float P1[] = {-WIDTH/4, -HEIGHT/4, 0f};

  // reshape scale value
  private float sx = 1, sy = 1;

  // when mouse is dragged, a new lowerleft point
  // and scale value for the rectangular area
  public void mouseDragged(MouseEvent e) {
	   float wd1 = WIDTH/2;
	    float ht1 = HEIGHT/2;

	    // The mouse location, new lowerleft corner
	    P1[0] = e.getX()-WIDTH/2;
	    P1[1] = HEIGHT/2-e.getY();
	    float wd2 = WIDTH/4-P1[0];
	    float ht2 = HEIGHT/4-P1[1];
	    
	   // System.out.println(P1[0] + ", " + P1[1]);

	    // scale value of the current rectangular area
	    sx = wd2/wd1;
	    sy = ht2/ht1;
	    //System.out.println(sx + ", " + sy);
  }


  public void mouseMoved(MouseEvent e) {

 }


  public void init(GLAutoDrawable drawable) {

    super.init(drawable);
    // listen to mouse motioin
    canvas.addMouseMotionListener(this);
    //drawable.addMouseMotionListener(this);
  }


  public void display(GLAutoDrawable glDrawable) {
	   float c[] = {0.0f, 0.0f, 0.0f, 1f};
	   float h[] = {0.0f, 0.8f, 0.0f, 1f};

	   long curTime;
	   float hAngle, hsecond, hminute, hhour;

	    // the rectangle lowerleft and upperright corners
	    float v0[] = {-1f/4f, -1f/4f, 0f};
	    float v1[] = {1f/4f, 1f/4f, 0f};
	
	 
	    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
	  
	    
	 
	    // reshape according to the current scale
	    myLoadIdentity();
	    
	    myTranslatef(P1[0]/WIDTH, P1[1]/HEIGHT, 0f);
	    myScalef(sx, sy, 1f);
	    myTranslatef(-v0[0], -v0[1], 0f);
	 
	    float v00[] = {0, 0, 0};
	    float v01[] = {0, 0, 0};
	    float v11[] = {0, 0, 0};
	    float v10[] = {0, 0, 0};
	    
	    v00[0] = v0[0];  v00[1] = v0[1]; 
	    v01[0] = v1[0];  v01[1] = v0[0]; 
	    v11[0] = v1[0];  v11[1] = v1[1]; 
	    v10[0] = v0[0];  v10[1] = v1[1]; 
	    
	    //gl.glColor3f(1, 1, 1); // the rectangle is white
	    color[0] = 1f;  color[1] = 1f;   color[2] = 1f; 
	    uploadColor(color); // unform iColor; 
	    transDrawClock(v00, v01);
	    transDrawClock(v01, v11);
	    transDrawClock(v11, v10);
	    transDrawClock(v10, v00);
		    
	    curTime = System.currentTimeMillis()/1000;
	    // returns the current time in milliseconds
	
	    hsecond = curTime%60;
	    curTime = curTime/60;
	    hminute = curTime%60 + hsecond/60f;
	    curTime = curTime/60;
	    hhour = (curTime%12)+7+hminute/60f; // winter + 7; summer + 8
	    // Eastern Standard Time (daylight saving) 
	
	    hAngle = (float) Math.PI*hsecond/30f; // arc angle
	
	    //gl.glColor3f(1, 0, 0); // second hand in blue
	    color[0] = 0;  color[1] = 0;   color[2] = 1; 
	    uploadColor(color); // unform iColor; 
	    
	    myPushMatrix(); 
		    myTranslatef(c[0], c[1], 0f);
		    myRotatef(-hAngle, 0f, 0f, 1f);
		    myTranslatef(-c[0], -c[1], 0f);
		    gl.glLineWidth(1);
		    transDrawClock(c, h);
	    myPopMatrix(); 
	
	    // gl.glColor3f(0, 1, 0); // minute hand in green
	    color[0] = 0;  color[1] = 1;   color[2] = 0; 
	    uploadColor(color); // unform iColor; 
	    myPushMatrix(); 
		    hAngle = (float) Math.PI*hminute/30; // arc angle
		    myTranslatef(c[0], c[1], 0f);
		    myScalef(0.8f, 0.8f, 1f); // minute hand shorter
		    myRotatef(-hAngle, 0f, 0f, 1f);
		    myTranslatef(-c[0], -c[1], 0f);
		    gl.glLineWidth(2);
		    transDrawClock(c, h);
	    myPopMatrix(); 
	
	    //gl.glColor3f(0, 0, 1); // hour hand in red
	    color[0] = 1;  color[1] = 0;   color[2] = 0; 
	    uploadColor(color); // unform iColor; 
	    hAngle = (float) Math.PI*hhour/6; // arc angle
	    myTranslatef(c[0], c[1], 0f);
	    myScalef(0.5f, 0.5f, 1f); // hour hand shortest
	    myRotatef(-hAngle, 0f, 0f, 1f);
	    myTranslatef(-c[0], -c[1], 0f);
	    gl.glLineWidth(4);
	    transDrawClock(c, h);
		    
  }


  public void transDrawClock(float C[], float H[]) {	  
	 
	// prepare Modelview matrix to be sent to the vertex shader as uniform
	uploadMV(); 
	
	// stores the two vertices to be sent to the vertex shader 
	float v[] = new float[6]; 
    for (int i=0; i<3; i++) v[i] = C[i]; 
    for (int i=0; i<3; i++) v[i+3] = H[i]; 

	// load vbo[0] with vertex data
	gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0 		
	FloatBuffer vBuf = Buffers.newDirectFloatBuffer(v);
	gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
			vBuf, // the vertex positions
			GL_STATIC_DRAW); // the data is static
	gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer
	
		
	// draw a clock hand 
    gl.glDrawArrays(GL_LINES, 0, 2); 
  }




  public static void main(String[] args) {
    new JOGL2_2_ReshapePushPop();
  }

}
