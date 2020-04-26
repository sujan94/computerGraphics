import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Logger;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * The professor actually extended Triangle class
 *
 * Uses the following shader files
 * vShaderSource = readShaderSource("src/JOGL1_1_V.shader"); // read vertex shader
 * fShaderSource = readShaderSource("src/JOGL1_1_F.shader"); // read fragment shader
 */
public class Character extends JOGL1_0_Frame
{

	// vertex array object (handle), for sending to the vertex shader
	int[] vao = new int[1];

	// vertex buffers objects (handles) to stores position, color, normal, etc
	int[] vbo = new int[2];

	// for the animation
	FPSAnimator animator;

	// handle to shader programs
	int vfPrograms;

	double cx = WIDTH / 2, cy = HEIGHT / 2, // center of the circle
			r = WIDTH / 3, // radius of the circle
			theta, // current angle
			delta = 1 / r; // angle for one unit apart: 2PI/2PI*r

	private float[] vPoint;

	private float[] color;

	public Character()
	{
		animator = new FPSAnimator(canvas, 60);

		color = new float[] { 0.0f, 0.0f, 0.0f };

		vPoint = new float[] { 0.0f, 0.0f };
	}


	FPSAnimator animator()
	{
		return animator;
	}


	void start()
	{
		animator().start();
	}


	/**
	 * Copied from {@link JOGL1_3_VertexArray#init(GLAutoDrawable)}
	 */
	@Override
	public void init(GLAutoDrawable drawable)
	{
		// array of vertices and colors corresponding to the vertices: a triangle
		float[] vPoints = { -0.5f, 0.0f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f };
		float[] vColors = { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };

		System.out.println("\na) Init is called once: ");

		String[] vShaderSource;
		String[] fShaderSource;
		gl = (GL4) drawable.getGL();

		System.out.println("	load the shader programs; ");

		vShaderSource = readShaderSource("src/JOGL1_3_V.shader"); // read vertex shader
		fShaderSource = readShaderSource("src/JOGL1_3_F.shader"); // read fragment shader
		vfPrograms = initShaders(vShaderSource, fShaderSource);

		// 1. generate vertex arrays indexed by vao
		gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
		System.out.println("	Generate VAO: " + vao.length); // we only use one vao
		gl.glBindVertexArray(vao[0]); // use handle 0

		// 2. generate vertex buffers indexed by vbo: here to store vertices and colors
		gl.glGenBuffers(vbo.length, vbo, 0);
		System.out.println("	Generate VBO: " + vbo.length); // we use two: position and color

		// 3. load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
			vBuf, // the vertex positions
			GL_STATIC_DRAW); // the data is static
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer

		// 4. load vbo[1] with color data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1
		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(vColors);
		gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
			cBuf, // the vertex colors
			GL_STATIC_DRAW);
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer

		// 5. enable VAO with loaded VBO data
		gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
		gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
		System.out.println("	Enable corresponding vertex attributes.\n"); // we use two: position and color
	}


	/**
	 * Copied from {@link JOGL1_0_Point#initShaders(String[], String[])}
	 */
	public int initShaders(String[] vShaderSource, String[] fShaderSource)
	{

		// 1. create, load, and compile vertex shader
		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		gl.glShaderSource(vShader, vShaderSource.length, vShaderSource, null, 0);
		gl.glCompileShader(vShader);

		// 2. create, load, and compile fragment shader
		int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);
		gl.glShaderSource(fShader, fShaderSource.length, fShaderSource, null, 0);
		gl.glCompileShader(fShader);

		// 3. attach the shader programs
		int vfProgram = gl.glCreateProgram(); // for attaching v & f shaders
		gl.glAttachShader(vfProgram, vShader);
		gl.glAttachShader(vfProgram, fShader);

		// 4. link the program
		gl.glLinkProgram(vfProgram); // successful linking --ready for using

		gl.glDeleteShader(vShader); // attached shader object will be flagged for deletion until
		// it is no longer attached
		gl.glDeleteShader(fShader);

		// 5. Use the program
		gl.glUseProgram(vfProgram); // loads them onto the GPU hardware
		gl.glDeleteProgram(vfProgram); // in-use program object will be flagged for deletion until
		// it is no longer in-use

		return vfProgram;
	}


	/**
	 * Copied from {@link JOGL1_3_VertexArray#readShaderSource(String)}
	 */
	public String[] readShaderSource(String filename)
	{ // read a shader file into an array
		Vector<String> lines = new Vector<String>(); // Vector object for storing shader program
		Scanner sc;

		try
		{
			sc = new Scanner(new File(filename)); // Scanner object for reading a shader program
		}
		catch (IOException e)
		{
			System.err.println("IOException reading file: " + e);
			return null;
		}
		while (sc.hasNext())
		{
			lines.addElement(sc.nextLine());
		}
		String[] shaderProgram = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++)
		{
			shaderProgram[i] = (String) lines.elementAt(i) + "\n";
		}
		sc.close();
		return shaderProgram; // a string of shader programs
	}


	/**
	 * See {@link HW1_4_Font#display(GLAutoDrawable)}
	 * 
	 * So I need a theta here
	 */
	public void display(GLAutoDrawable drawable)
	{
		super.display(drawable);

		GL2 gl2 = drawable.getGL().getGL2();

		// draw a stroke string in the center (no clipping)
		gl2.glPushMatrix();
		gl2.glTranslated(cx, cy, 0); // put it at the center of circle
		gl2.glScalef(0.2f, 0.2f, 0.2f); // scale the sentence
		gl2.glRotated(-180 * theta / Math.PI, 0, 0, 1); // rotate it according to degrees
		gl2.glTranslated(-WIDTH / 2, -5, 0); // put the world in the center of coord
		// glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, "HELLO WORLD!");
		gl2.glPopMatrix();

		// delta is about a pixel width in logical coord.
		theta = theta - delta;

		for (int i = 1; i <= 5; i++)
		{
			int x = (int) (r * Math.cos(theta + 2 * i * Math.PI / 5) + cx);
			int y = (int) (r * Math.sin(theta + 2 * i * Math.PI / 5) + cy);

			// draw a red point on Circle
			gl.glPointSize(8);
			// todo - gl.glColor3f(1.0f, 0.0f, 0.0f);

			vPoint[0] = x;
			vPoint[1] = y;

			// this one was updated
			drawPoint(vPoint, color);
			gl.glPointSize(1);

			// label the current vertex
			// todo - gl.glColor3f(1f, 1f, 0f);
			labelVertex(gl2, x, y, i);
		}
	}


	public void labelVertex(GL2 gl2, int x, int y, int i)
	{

		gl2.glRasterPos3f(x, y, 0); // start poistion
		GLUT glut = new GLUT();
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Vertex[");
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, Integer.toString(i));
		glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, ']');

		//

		/**
		 * todo
		 * <pre>
		 // label the vertex
		 gl.glRasterPos3f(x, y, 0); // start poistion
		
		 </pre>
		 */
	}


	/**
	 * Copied from {@link JOGL1_4_1_Point#drawPoint(float[], float[])}
	 */
	public void drawPoint(float[] vPoint, float[] vColor)
	{
		// 1. load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoint);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
			vBuf, // the vertex array
			GL_STATIC_DRAW);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

		// 2. load vbo[1] with color data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1
		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(vColor);
		gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
			cBuf, // the color array
			GL_STATIC_DRAW);
		gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer

		// 3. draw a point: VAO has two arrays of corresponding vertices and colors
		gl.glDrawArrays(GL_POINTS, 0, 1);
	}


	@Override
	public void dispose(GLAutoDrawable drawable)
	{
		animator().stop(); // stop the animator thread
		Logger.getGlobal().fine("Animator thread stopped.");
		super.dispose(drawable);
	}


	public static void main(String[] args)
	{
		// some constructor starts the process
		Character character = new Character();
		character.start();
	}

}
