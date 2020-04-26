/*************************************************
 * Created on August 1, 2017, @author: Jim X. Chen
 *
 * Animate a point 
 * Animator and Uniform: 
 * 		Animator starts a thread that calls display() repetitively
 * 		Uniform sends a variable value from JOGL program to the shader programs (uniform value for all shader programs)
 *
 */


import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;

import java.nio.FloatBuffer;
import com.jogamp.common.nio.Buffers;

import com.jogamp.opengl.util.FPSAnimator;


public class JOGL1_2_Animate extends JOGL1_1_PointVFfiles {
	static FPSAnimator animator; // for thread that calls display() repetitively
	static int vfPrograms; // handle to shader programs
	
	static float pos=0.0f, delta=0.1f; // modify between display() calls

	public JOGL1_2_Animate() { // it calls supers constructor first
		
		// Frame per second animator 
		animator = new FPSAnimator(canvas, 40); // 40 calls per second; frame rate
		animator.start();
		System.out.println("\nConstructor: Animator starts a thread that calls display() repeatitively.");
	}

	public void display(GLAutoDrawable drawable) {
		
		// clear the display every frame: another way to set the background color
		float bgColor[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
		gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

		//gl.glDrawBuffer(GL.GL_FRONT_AND_BACK); //if you want a still image

		// pos goes from -1 to 1 back and forth
		pos += delta; 
		if (pos<=-1.0f) delta = 0.01f; 
		else if (pos>=1.0f) delta = -0.01f; 
		
		//Connect JOGL variable with shader variable by name
		int posLoc = gl.glGetUniformLocation(vfPrograms,  "sPos"); 
		gl.glProgramUniform1f(vfPrograms,  posLoc,  pos);
		
		gl.glPointSize(4.0f); 
		
		// This is the lower left quad
		gl.glViewport(0, 0, 400, 400); // physical coordinates: number in pixels		
		gl.glDrawArrays(GL_POINTS, 0, 1);
		
		// This is the upper-right quad
		gl.glViewport(400, 400, 400, 400);
		gl.glDrawArrays(GL_POINTS, 0, 1);
	}
	
	
	public void init(GLAutoDrawable drawable) { // reading new vertex & fragment shaders
		gl = (GL4) drawable.getGL();
		String vShaderSource[], fShaderSource[] ;
		
		vShaderSource = readShaderSource("src/JOGL1_2_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/JOGL1_2_F.shader"); // read fragment shader
		
		vfPrograms = initShaders(vShaderSource, fShaderSource);		
	}

	
	public void dispose(GLAutoDrawable drawable) {
		animator.stop(); // stop the animator thread
		System.out.println("Animator thread stopped.");
		super.dispose(drawable);
	}
	
	public static void main(String[] args) {
		new JOGL1_2_Animate();
		

	}
}
