package project2;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

public class Circle extends JOGL1_1_PointVFfiles {

	FPSAnimator animator; // for thread that calls display() repetitively
	int vfPrograms; // handle to shader programs
	float  pos=0.0f;


	double cx = 0.0, cy = 0.0,//center of the circle
			r = 1, // radius of the circle
			theta, // current angle
			delta = 1/r; // angle for one unit apart: 2PI/2PI*r

	int vao[ ] = new int[1]; // vertex array object (handle), for sending to the vertex shader
	int vbo[ ] = new int[2]; // vertex buffers objects (handles) to stores position, color, normal, etc


	float vColors[] = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};

	public Circle() { // it calls supers constructor first

		// Frame per second animator
		animator = new FPSAnimator(canvas, 40); // 40 calls per second; frame rate
		animator.start();
		System.out.println("A thread that calls display() repeatitively.");
	}

	public void display(GLAutoDrawable drawable) {


// clear the display every frame
		float bgColor[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
		gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

		//Connect JOGL variable with shader variable by name
		int posLoc = gl.glGetUniformLocation(vfPrograms,  "sPos");
		gl.glProgramUniform1f(vfPrograms,  posLoc,  pos);

		float[] list = generateCirclePoints();
		// 3. load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(list);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex positions
				GL_STATIC_DRAW); // the data is static
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer

		float[] list2 = generateRandomPoints();
		FloatBuffer vBuf2 = Buffers.newDirectFloatBuffer(list);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				vBuf, // the vertex positions
				GL_STATIC_DRAW); // the data is static
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer

		// 4. load vbo[1] with color data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1
		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(vColors);
		gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit()*Float.BYTES,  //# of float * size of floats in bytes
				cBuf, //the vertex colors
				GL_STATIC_DRAW);
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer

		gl.glPointSize(6.0f);

		// 6. draw 3 points: VAO has two arrays of corresponding vertices and colors
		gl.glDrawArrays(GL_LINE_LOOP, 0, vBuf.limit()/3);
//		if (pos>0.1f) gl.glDrawArrays(GL_LINE_LOOP, 0, 3);
//		else
//			if (pos<-0.1f) gl.glDrawArrays(GL_TRIANGLES, 0, 3);
	}

	private float[] generateRandomPoints() {
		return new float[0];
	}

	private float[] generateCirclePoints()
	{
		int numberOfPoints = 720;
		double currentAngle;
		double angleIncrement = 2 * Math.PI / numberOfPoints;
		// I could do theta = theta + 1/r;
		float[] circlePoints = new float[3 * numberOfPoints]; // predefined number of pixels on the line

		for (int i = 0; i < numberOfPoints; i++)
		{
			currentAngle = i * angleIncrement;
			float x = (float) (r * Math.cos(currentAngle));
			float y = (float) (r * Math.sin(currentAngle));

			// write a pixel into the framebuffer, here we write into an array
			circlePoints[(i * 3)] = x; // normalize -1 to 1
			circlePoints[(i * 3) + 1] = y; // normalize -1 to 1
			circlePoints[(i * 3) + 2] = 0.0f;
		}
		return circlePoints;
	}

	void drawBouncingPoint()
	{
		int numberOfPoints = 5;
		float[] pointsInCircle = new float[3 * numberOfPoints];
		float[] color = new float[3];
		color[0] = (float) Math.random();
		color[1] = (float) Math.random();
		color[2] = (float) Math.random();

		int i = 0;
		while (i < 5)
		{
			// randomly pick a theta, in radians
			double angle = (2 * Math.random() - 1) * (2 * Math.PI);

			// we know that the circle's origin is 400,400
			// shoot a point from that to the boundary of a circle
			// generate a random radial, from 0 to 100
			double random_radius = ((2 * Math.random() - 1) * r);
			float x = (float) (random_radius * Math.cos(angle));
			float y = (float) (random_radius * Math.sin(angle));
//			vPoint[0] = x;
//			vPoint[1] = y;
//			drawPoint(vPoint, allPointsColor);
//
//			// write a pixel into the framebuffer, here we write into an array
//			pointsInCircle[(i * 3)] = x; // normalize -1 to 1
//			pointsInCircle[(i * 3) + 1] = y; // normalize -1 to 1
//			pointsInCircle[(i * 3) + 2] = 0.0f;
//			i++;
		}
//		drawPointArray(circlePoints, color);
	}

	public void init(GLAutoDrawable drawable) { // reading new vertex & fragment shaders
		gl = (GL4) drawable.getGL();
		String vShaderSource[], fShaderSource[];

		vShaderSource = readShaderSource("srcp/project2/circle_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("srcp/project2/circle_F.shader"); // read fragment shader
		vfPrograms = initShaders(vShaderSource, fShaderSource);

		// 1. generate vertex arrays indexed by vao
		gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
		System.out.println("	Generate VAO: " + vao.length); // we only use one vao
		gl.glBindVertexArray(vao[0]); // use handle 0

		// 2. generate vertex buffers indexed by vbo: here to store vertices and colors
		gl.glGenBuffers(vbo.length, vbo, 0);
		System.out.println("	Generate VBO: " + vbo.length); // we use two: position and color

		gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
		gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color


	}

	public void dispose(GLAutoDrawable drawable) {
		animator.stop(); // stop the animator thread
		System.out.println("Animator thread stopped.");
		super.dispose(drawable);
	}

	public static void main(String[] args) {
		new Circle();
	}
}
