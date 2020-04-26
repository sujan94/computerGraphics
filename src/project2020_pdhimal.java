import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.GL.GL_LINE_LOOP;
import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * <pre>
 * You should implement the following:
 *
 * 1. Draw a circle and multiple points bouncing in a circle
 *  ◦ Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
 *  ◦ draw points bouncing in the circle by sending them as an array in VBO; you should use random colors for the points;
 * 2. Draw a pentagon that rotates on the circle, with your initials moving with the vertices
 *  ◦ In addition to the circle and points bouncing, you draw a pentagon with colored vertices. 
 *    You draw a pentagon by send line vertices as an array in VBO to the vertex shader at the same time.
 *    Then you draw the vertices with a different color. The animation is achieved by rotation around the z axis.
 *  ◦ Scale and translate your initials to the vertices; you need to transform and display each character serially 
 *    because your function draws one character at a time;
 *       ▪ If the characters are rotated with the pentagon, they should stay vertical;
 * 3. Draw your initials around the center and rotate in the opposite direction as an animation
 *  ◦ Scale, translate, and rotate each of your initials
 * </pre>
 *
 * This class uses the following shader files:
 * - JOGL1_3_V.shader"; // read vertex shader
 * -JOGL1_3_F.shader"); // read fragment shader
 *
 * @author Prakash Dhimal
 *         Computer Graphics
 *         CS 551
 *         George Mason University
 *         Project 2020 Transformation
 */
class ProjectTransformation extends JOGL1_4_3_Line
{

	private final float[] circlePointsColor;

	private final float[] circlePoints;

	// radius of the circle
	double radius = 0.5;

	// number of points to make the circle
	int numberOfPoints = 720;

	// angle increment to draw the circle, rotate the pentagon, and the initials
	double angleIncrement = 2 * Math.PI / numberOfPoints;

	// current theta for the pentagon
	double theta = 0;

	// for the initials rotation the opposite direction
	double initialTheta = 0;

	// for the radius rotating the opposite direction
	double initial_radius = radius - 0.1;

	// radius for the bouncing points
	double current_radius = 0;

	double radius_Increment = 0.1;

	boolean increasing = true;

	List<Double> radialsToBounce = new ArrayList<>();

	public ProjectTransformation()
	{
		// all white circle
		circlePointsColor = new float[] { 0.0f, 0.0f, 0.0f };
		// cache the circle points, they will be the same
		circlePoints = getCirclePoints();

		// bounce the points in these radials
		for (double i = 0; i < 360; i = i + 10)
		{
			radialsToBounce.add(i);
		}
	}


	/**
	 * <pre>
	 * Puts the circle to the float[] array
	 * From the book
	 * As an example, a simple 2D circle equation with radius (r) and centered at (cx, cy)
	 * can be expressed in parametric function as:
	 * 	x = r * cos(theta) + cx
	 * 	y = r * sin(theta) + cy
	 * 	r is the radius of the circle
	 * 	cx - center x
	 * 	cy - center y
	 * if nothing is give, it is effectively 0,0
	 * Here, are are working with a co-ordinate system :
	 *    x: -1 to 1
	 *    y: -1 to 1
	 *    origin: 0,0
	 * Therefore, we do not need the cx,cy
	 * </pre>
	 */
	private float[] getCirclePoints()
	{
		double currentAngle;
		float[] circlePoints = new float[3 * numberOfPoints];
		for (int i = 0; i < numberOfPoints; i++)
		{
			currentAngle = i * angleIncrement;
			float x = (float) (radius * Math.cos(currentAngle));
			float y = (float) (radius * Math.sin(currentAngle));

			// write a pixel into the framebuffer, here we write into an array
			circlePoints[(i * 3)] = x; // this is from -1 to 1
			circlePoints[(i * 3) + 1] = y; // this is from -1 to 1
			circlePoints[(i * 3) + 2] = 0.0f;
		}
		return circlePoints;
	}


	@Override
	public void display(GLAutoDrawable drawable)
	{
		// Clear the frame, so that we can see the color changing (Animation)
		float[] bgColor = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
		gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

		/**
		 * 1. Draw a circle and multiple points bouncing in a circle
		 * ◦ Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
		 * ◦ draw points bouncing in the circle by sending them as an array in VBO; you should use random colors for the points;
		 */
		circleOfPoints();

		/**
		 * 2. Draw a pentagon that rotates on the circle, with your initials moving with the vertices
		 * ◦ In addition to the circle and points bouncing, you draw a pentagon with colored vertices.
		 * You draw a pentagon by send line vertices as an array in VBO to the vertex shader at the same time.
		 * Then you draw the vertices with a different color. The animation is achieved by rotation around the z axis.
		 * ◦ Scale and translate your initials to the vertices; you need to transform and display each character serially
		 * because your function draws one character at a time;
		 * ▪ If the characters are rotated with the pentagon, they should stay vertical;
		 */
		float[] pentagon_points = pentagon();
		float x = pentagon_points[0];
		if (x < 0)
			x = x - 0.1f;
		else
			x = x + 0.1f;
		float y = pentagon_points[1];
		if (y < 0)
			y = y - 0.1f;
		else
			y = y + 0.1f;
		drawInitial(
			100f,
			100f,
			x,
			y);

		/**
		 * 3. Draw your initials around the center and rotate in the opposite direction as an animation
		 * ◦ Scale, translate, and rotate each of your initials
		 */

		initialTheta = initialTheta + angleIncrement;
		x = (float) (initial_radius * Math.cos(initialTheta));
		y = (float) (initial_radius * Math.sin(initialTheta));
		drawInitial(
			100f,
			100f,
			x,
			y);

		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}


	/**
	 * 1. Draw a circle and multiple points bouncing in a circle
	 * ◦ Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
	 * ◦ draw points bouncing in the circle by sending them as an array in VBO; you should use random colors for the points;
	 */
	private void circleOfPoints()
	{
		// drawing the circle by passing the array to glDrawArray
		drawPointArray(circlePoints, circlePointsColor, GL_LINE_LOOP, numberOfPoints);
		gl.glPointSize(2.0f);
		// draw the bouncing points inside the circle
		drawPointsInCircle();
		drawBouncingPoint();
	}


	/**
	 * 1. Draw a circle and multiple points bouncing in a circle
	 * ◦ Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
	 * ◦ draw points bouncing in the circle by sending them as an array in VBO; you should use random colors for the points;
	 */
	private void drawPointsInCircle()
	{
		int bouncingPoints = 20;
		float[] pointsInCircle = new float[3 * bouncingPoints];
		float[] color = new float[3];
		color[0] = (float) (2 * Math.random() - 1);
		color[1] = (float) (2 * Math.random() - 1);
		color[2] = (float) (2 * Math.random() - 1);

		int i = 0;
		while (i < bouncingPoints)
		{
			// randomly pick a theta, in radians
			double angle = (2 * Math.random() - 1) * (2 * Math.PI);
			double random_radius = ((2 * Math.random() - 1) * radius);

			float x = (float) (random_radius * Math.cos(angle));
			float y = (float) (random_radius * Math.sin(angle));

			pointsInCircle[(i * 3)] = x; // normalize -1 to 1
			pointsInCircle[(i * 3) + 1] = y; // normalize -1 to 1
			pointsInCircle[(i * 3) + 2] = 0.0f;
			i++;
		}
		gl.glPointSize(6.0f);
		drawPointArray(pointsInCircle, color, GL_POINTS, bouncingPoints);
		gl.glPointSize(1.0f);
	}


	private void drawBouncingPoint()
	{
		float[] pointsInCircle = new float[3 * radialsToBounce.size()];
		float[] color = new float[3];
		color[0] = (float) (2 * Math.random() - 1);
		color[1] = (float) (2 * Math.random() - 1);
		color[2] = (float) (2 * Math.random() - 1);

		if (increasing)
			current_radius = current_radius + radius_Increment;
		else
			current_radius = current_radius - radius_Increment;

		if (current_radius >= radius)
		{
			increasing = false;
			if (current_radius > radius)
				current_radius = current_radius - radius_Increment;
		}
		else if (current_radius <= 0)
		{
			increasing = true;
			if (current_radius < radius)
				current_radius = current_radius + radius_Increment;
		}

		AtomicInteger i = new AtomicInteger();
		radialsToBounce.forEach(radial -> {
			// randomly pick a theta, in radians
			double angle = (2 * Math.random() - 1) * (2 * Math.PI);

			float x = (float) (current_radius * Math.cos(angle));
			float y = (float) (current_radius * Math.sin(angle));

			pointsInCircle[(i.get() * 3)] = x; // normalize -1 to 1
			pointsInCircle[(i.get() * 3) + 1] = y; // normalize -1 to 1
			pointsInCircle[(i.get() * 3) + 2] = 0.0f;
			i.getAndIncrement();
		});
		if (current_radius >= radius)
			gl.glPointSize(4.0f);
		else
			gl.glPointSize(6.0f);
		drawPointArray(pointsInCircle, color, GL_POINTS, radialsToBounce.size());
		gl.glPointSize(1.0f);
	}


	/**
	 * <pre>
	 * Draw a pentagon based on
	 *  - theta
	 *  - angleIncrement
	 *     
	 * </pre>
	 * 
	 * @return last two vertices of the pentagon, the last vertex will be used as the origin to draw the initials
	 */
	private float[] pentagon()
	{
		// delta generates about a unit length in logical coord.
		theta = theta - angleIncrement;
		float[] pentagonPoints = new float[3 * 2];
		float[] color = new float[3];
		color[0] = (float) (2 * Math.random() - 1);
		color[1] = (float) (2 * Math.random() - 1);
		color[2] = (float) (2 * Math.random() - 1);

		double pentagon_radius = radius + 0.2;

		// starting point
		float x0 = (float) (pentagon_radius * Math.cos(theta));
		float y0 = (float) (pentagon_radius * Math.sin(theta));

		for (int i = 1; i <= 5; i++)
		{
			float x = (float) (pentagon_radius * Math.cos(theta + 2 * i * Math.PI / 5));
			float y = (float) (pentagon_radius * Math.sin(theta + 2 * i * Math.PI / 5));

			pentagonPoints[0] = x0;
			pentagonPoints[1] = y0;
			pentagonPoints[2] = 0;

			pentagonPoints[3] = x;
			pentagonPoints[4] = y;
			pentagonPoints[5] = 0;
			// draw these points
			drawPointArray(pentagonPoints, color, GL_LINES, 2);

			vPoint[0] = x;
			vPoint[1] = y;
			gl.glPointSize(6.0f);
			drawPointArray(vPoint, color, GL_POINTS, 1);
			gl.glPointSize(1.0f);

			x0 = x;
			y0 = y;
		}

		return pentagonPoints;
	}


	/**
	 * <pre>
	 * Draws initials (PD) in its own co-ordinate system based on the 
	 *   - height,
	 *   - width
	 *   - cx
	 *   - cy 
	 * </pre>
	 * 
	 * @param height height of the co-ordinate system for the initials (0 - HEIGHT)
	 * @param width width of the co-ordinate system for the initials (0 - WIDTH)
	 * @param cx origin(x) of the co-ordinate system for the initials (-1 to 1)
	 * @param cy origin(y) of the co-ordinate system for the initials (-1 to 1)
	 */
	private void drawInitial(
			float height,
			float width,
			float cx, // from -1 to 1
			float cy) // from -1 to 1
	{
		/**
		 * Make the color random
		 */
		vColor[0] = (float) (2 * Math.random() - 1);
		vColor[1] = (float) (2 * Math.random() - 1);
		vColor[2] = (float) (2 * Math.random() - 1);

		// Hard-code the lines to show my initials
		/**
		 * For all of the lines
		 */
		float distance_X = width / 5;
		float north_Y = height / 5;
		float south_Y = -width / 5;

		/**
		 * For P
		 */
		float east_P_X = -width / 3;
		float west_P_X = east_P_X + distance_X;
		// not doing zero to avoid NaN
		float mid_P_Y = -height / 15;

		float curve_P_X = width / 15;
		float far_west_P_X = west_P_X + curve_P_X;
		float curve_P_Y = height / 15;
		float north_curve_P_Y = north_Y - curve_P_Y;
		float mid_curve_P_Y = mid_P_Y + curve_P_Y;

		// | in P + offset
		vPoint[0] = (east_P_X / width) / (WIDTH / 2 / width) + cx;

		vPoint[0] = (east_P_X / width) / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = (east_P_X / width) / ((WIDTH / 2) / width) + cx;
		vPoint[4] = south_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// other end of P
		vPoint[0] = far_west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_curve_P_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = far_west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = mid_curve_P_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// top line of P
		vPoint[0] = (east_P_X / width) / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// bottom line of P
		vPoint[0] = (east_P_X / width) / ((WIDTH / 2) / width) + cx;
		vPoint[1] = mid_P_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = mid_P_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// top curve of P
		vPoint[0] = west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = far_west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = north_curve_P_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// bottom curve
		vPoint[0] = west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = mid_P_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = far_west_P_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = mid_curve_P_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		/**
		 * For D
		 *
		 * Made using six lines
		 */
		float east_D_X = west_P_X + distance_X;
		float west_D_X = east_D_X + distance_X;
		float curve_D_X = (float) (width / 9.2);
		float far_west_D_X = west_D_X + curve_D_X;
		float curve_D_Y = width / 8;
		float north_curve_D_Y = north_Y - curve_D_Y;
		float south_curve_D_Y = south_Y + curve_D_Y;

		// | in D
		vPoint[0] = east_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = east_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = south_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// west in D
		vPoint[0] = far_west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_curve_D_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = far_west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = south_curve_D_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// top line in D
		vPoint[0] = east_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// bottom line in D
		vPoint[0] = east_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = south_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = south_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// top curve
		vPoint[0] = west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = north_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = far_west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = north_curve_D_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);

		// bottom curve
		vPoint[0] = west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[1] = south_Y / height / ((HEIGHT / 2) / height) + cy;
		vPoint[3] = far_west_D_X / width / ((WIDTH / 2) / width) + cx;
		vPoint[4] = south_curve_D_Y / height / ((HEIGHT / 2) / height) + cy;
		drawLineJOGL(vPoint, vColor);
	}


	/**
	 *
	 * Send points as an array in VBO to the vertex shader at the same time,
	 * and call glDrawArray;
	 */
	private void drawPointArray(float[] vPoint, float[] colorArray, int mode, int size)
	{
		// 1. load vbo[0] with vertex data
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoint);
		gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
			vBuf, // the vertex array
			GL_STATIC_DRAW);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

		// 2. send color data to vertex shader through uniform (array): color here is not per-vertex
		FloatBuffer cBuf = Buffers.newDirectFloatBuffer(colorArray);

		// Connect JOGL variable with shader variable by name
		int colorLoc = gl.glGetUniformLocation(vfPrograms, "vColor");
		gl.glProgramUniform3fv(vfPrograms, colorLoc, 1, cBuf);

		// 3. draw a line: VAO has one array of corresponding two vertices
		gl.glDrawArrays(mode, 0, size);
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


	public static void main(String[] args)
	{
		ProjectTransformation projectTransformation = new ProjectTransformation();
		projectTransformation.setTitle("Project Transformation - Computer Graphics");
		projectTransformation.setSize(WIDTH, HEIGHT);
		projectTransformation.setVisible(true);
	}
}
