import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.FloatBuffer;

public class CircleA extends JOGL1_1_PointVFfiles {
	// Radius for the circle
	private static final float RADIUS = 0.6f;

	FPSAnimator animator; // for thread that calls display() repetitively
	int vfPrograms; // handle to shader programs
	float posX = RADIUS, posY = 0.0f, delta = 0.009f;
	boolean isClockWiseDirection = false;

	public CircleA() { // it calls supers constructor first

		// Frame per second animator
		animator = new FPSAnimator(canvas, 40); // 40 calls per second; frame rate
		animator.start();
		System.out.println("A thread that calls display() repeatitively.");
	}

	public void display(GLAutoDrawable drawable) {
		
 

		// clear the display every frame: another way to set the background color
		float bgColor[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
		gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

		gl.glDrawBuffer(GL.GL_FRONT_AND_BACK); //if you want a still image

		if (posX > -RADIUS && !isClockWiseDirection) {
			posX = posX - delta;
			posY = (float) Math.sqrt(RADIUS * RADIUS - (posX * posX));
		} else if (posX == RADIUS) {
			posY = 0.0f;
			isClockWiseDirection = false;
		} else {
			isClockWiseDirection = true;
			posX = posX + delta;
			posY = -1 * (float) Math.sqrt(RADIUS * RADIUS - (posX * posX));
		}

		// Connect JOGL variable with shader variable by name
		int posLocX = gl.glGetUniformLocation(vfPrograms, "xPos"); // returns the location of "sPos"
		gl.glProgramUniform1f(vfPrograms, posLocX, posX);

		// Connect JOGL variable with shader variable by name
		int posLocY = gl.glGetUniformLocation(vfPrograms, "yPos"); // returns the location of "sPos"
		gl.glProgramUniform1f(vfPrograms, posLocY, posY);

		gl.glPointSize(6.0f);

		// This is the upper-right quad
		gl.glViewport(400, 400, 800, 800);
		gl.glDrawArrays(GL_POINTS, 0, 1);
	}

	public void init(GLAutoDrawable drawable) { // reading new vertex & fragment shaders
		gl = (GL4) drawable.getGL();
		String vShaderSource[], fShaderSource[];

		vShaderSource = readShaderSource("src/H1_sprajap2_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/H1_sprajap2_F.shader"); // read fragment shader

		vfPrograms = initShaders(vShaderSource, fShaderSource);
		
		
	}

	public void dispose(GLAutoDrawable drawable) {
		animator.stop(); // stop the animator thread
		System.out.println("Animator thread stopped.");
		super.dispose(drawable);
	}

	public static void main(String[] args) {
		new CircleA();
	}
}
