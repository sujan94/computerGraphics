/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_LINES;

import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;

public class JOGL2_1_Clock2d extends JOGL2_0_2DTransform {
	  static final float PI = 3.1415926f;
	  // homogeneous coordinates
	  static float c[] = {0.0f, 0.0f, 0.0f, 1f};
	  static float h[] = {0.0f, 0.8f, 0.0f, 1f};

	  static long curTime;
	  static float hAngle, hsecond, hminute, hhour;

	  float color[] = {1, 1, 1}; 

	  public void display(GLAutoDrawable glDrawable) {

	    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
	    
	    curTime = System.currentTimeMillis()/1000;
	    // returns the current time in milliseconds

	    hsecond = curTime%60;
	    curTime = curTime/60;
	    hminute = curTime%60 + hsecond/60f;
	    curTime = curTime/60;
	    hhour = (curTime%12)+7+hminute/60f; // winter + 7; summer + 8
	    // Eastern Standard Time (daylight saving) 

	    hAngle = PI*hsecond/30f; // arc angle

	    //gl.glColor3f(1, 0, 0); // second hand in blue
	    color[0] = 0;  color[1] = 0;   color[2] = 1; 
	    
	    myLoadIdentity();
	    myTranslatef(c[0], c[1], 0f);
	    myRotatef(-hAngle, 0f, 0f, 1f);
	    myTranslatef(-c[0], -c[1], 0f);
	    gl.glLineWidth(1);
	    transDrawClock(c, h);

	    drawCircle(WIDTH/5,  4); 

	    // gl.glColor3f(0, 1, 0); // minute hand in green
	    color[0] = 0;  color[1] = 1;   color[2] = 0; 
	    myLoadIdentity();
	    hAngle = PI*hminute/30; // arc angle
	    myTranslatef(c[0], c[1], 0f);
	    myScalef(0.8f, 0.8f, 1f); // minute hand shorter
	    myRotatef(-hAngle, 0f, 0f, 1f);
	    myTranslatef(-c[0], -c[1], 0f);
	    gl.glLineWidth(2);
	    transDrawClock(c, h);

	    //gl.glColor3f(0, 0, 1); // hour hand in red
	    color[0] = 1;  color[1] = 0;   color[2] = 0; 
	    myLoadIdentity();
	    hAngle = PI*hhour/6; // arc angle
	    myTranslatef(c[0], c[1], 0f);
	    myScalef(0.5f, 0.5f, 1f); // hour hand shortest
	    myRotatef(-hAngle, 0f, 0f, 1f);
	    myTranslatef(-c[0], -c[1], 0f);
	    gl.glLineWidth(3);
	    transDrawClock(c, h);
	    
	  }


	  public void transDrawClock(float C[], float H[]) {

	// send color data to vertex shader through uniform (array): color here is not per-vertex
	FloatBuffer cBuf = Buffers.newDirectFloatBuffer(color);

	//Connect JOGL variable with shader variable by name
	int colorLoc = gl.glGetUniformLocation(vfPrograms,  "iColor"); 
	gl.glProgramUniform3fv(vfPrograms,  colorLoc, 1, cBuf);
	  
	 
	// prepare Modelview matrix to be sent to the vertex shader as uniform
	float MV[] = new float [16];
	get_Matrix(MV); 
	
	
	// connect the modelview matrix
	int mvLoc = gl.glGetUniformLocation(vfPrograms,  "mv_matrix"); 
	gl.glProgramUniformMatrix4fv(vfPrograms, mvLoc,  1,  false,  MV, 0);

	
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
    new JOGL2_1_Clock2d();
  }

}
