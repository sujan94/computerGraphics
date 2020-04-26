import com.jogamp.opengl.GLAutoDrawable;

/**
 * Design a program that extends JOGL2_11, so that it has 3 moons (a cone, a cylinder, and a sphere) rotating around
 * the earth in the generalized solar system.
 *
 * The three moons will change their direction of rotation when they collide with one another.
 * 
 * Uses the following shader files:
 * - JOGL2_5_V.shader
 * - JOGL2_5_F.shader
 * See {@link JOGL2_7_Sphere#init(GLAutoDrawable)}
 *
 * @author Prakash Dhimal
 *         Computer Graphics
 *         CS 551
 *         George Mason University
 *         Homework 3 3D Collision, transformation, and viewing
 */
class ConeSolarCollision extends JOGL2_11_ConeSolar
{
	/**
	 * Copied from J2_11_ConeSolarCollision.java
	 */
	// direction and speed of rotation
	static float coneD = WIDTH / 110;

	static float sphereD = -WIDTH / 64;

	static float cylinderD = WIDTH / 300f;

	static float spherem = 120, cylinderm = 240;

	static float tmpD = 0, conem = 0; // centers of the objects

	static float[] coneC = new float[3];

	static float[] sphereC = new float[3];

	static float[] cylinderC = new float[3];

	// current matrix on the matrix stack
	static float[] currM = new float[16];

	ConeSolarCollision()
	{
		// control the speed
		coneD = coneD / 1000;
		sphereD = sphereD / 1000;
		cylinderD = cylinderD / 1000;
	}


	/**
	 * Inspired by code example from J2_11_ConeSolarCollision.java
	 * 
	 * @param E
	 * @param e
	 * @param M
	 * @param m
	 */
	@Override
	void drawSolar(
			float E,
			float e,
			float M,
			float m)
	{
		// Global coordinates
		gl.glLineWidth(3);
		drawSphere(); // for loading matrix purpose
		drawColorCoord(
			WIDTH / 4,
			WIDTH / 4,
			WIDTH / 4);

		/**
		 * Set up to rotate around the sun at an angle
		 */
		myPushMatrix();
		myRotatef(e * dg, 0.0f, 1.0f, 0.0f);
		// rotating around the "sun"; proceed angle
		myRotatef(tiltAngle * dg, 0.0f, 0.0f, 1.0f); // tilt angle
		myTranslatef(0.0f, E, 0.0f);
		myPushMatrix();
		myScalef(WIDTH / 20, WIDTH / 20, WIDTH / 20);

		/**
		 * Earth as a sphere
		 */
		drawSphere();
		myPopMatrix();
		myPushMatrix();
		myScalef(E / 8, E, E / 8);
		myRotatef(90 * dg, 1.0f, 0.0f, 0.0f); // orient the cone
		drawCone();
		myPopMatrix();

		/**
		 * Cone
		 * *******************************************************************
		 */
		myPushMatrix();
		conem = conem + coneD;
		myRotatef(
			conem,
			0.0f,
			1.0f,
			0.0f);
		// rotating around the "earth"
		myTranslatef(
			M,
			0.0f,
			0.0f);
		gl.glLineWidth(2);
		myScalef(
			E / 8,
			E / 8,
			E / 8);
		// self rotation
		myRotatef(
			(float) Math.toRadians(cnt),
			0.0f,
			1.0f,
			0.0f);
		drawCone();

		// retrieve the center of the cone
		get_Matrix(currM);
		coneC[0] = currM[12];
		coneC[1] = currM[13];
		coneC[2] = currM[14];
		myPopMatrix();
		/**
		 *
		 * Cone ends
		 * *******************************************************************
		 */

		/**
		 * Sphere
		 * *******************************************************************
		 */
		myPushMatrix();
		spherem = spherem + sphereD;
		myRotatef(
			spherem,
			0.0f,
			1.0f,
			0.0f);
		// rotating around the "earth"
		myTranslatef(
			M * 2,
			0.0f,
			0.0f);
		myScalef(
			E / 8,
			E / 8,
			E / 8);
		// self rotation
		myRotatef(
			(float) Math.toRadians(cnt * 5),
			0.0f,
			1.0f,
			0.0f);
		drawSphere();
		// retrieve the center of the sphere
		get_Matrix(currM);
		sphereC[0] = currM[12];
		sphereC[1] = currM[13];
		sphereC[2] = currM[14];
		myPopMatrix();

		/**
		 * Sphere ends
		 * *******************************************************************
		 */

		/**
		 * Cylinder
		 * *******************************************************************
		 */
		myPushMatrix();
		cylinderm = cylinderm + cylinderD;
		myRotatef(
			cylinderm,
			0.0f,
			1.0f,
			0.0f);
		// rotating around the "earth"
		myTranslatef(
			M,
			0.0f,
			M * 1.5f);
		gl.glLineWidth(2);
		myScalef(E / 8, E / 8, E / 8);
		// self rotation
		myRotatef(
			(float) Math.toRadians(cnt * 2),
			0.0f,
			1.0f,
			0.0f);
		drawCylinder();
		// retrieve the center of the cylinder
		// the matrix is stored column major left to right
		get_Matrix(currM);
		cylinderC[0] = currM[12];
		cylinderC[1] = currM[13];
		cylinderC[2] = currM[14];
		myPopMatrix();

		/**
		 * Cylinder ends
		 * *******************************************************************
		 */
		myPopMatrix();

		/**
		 * This is the sun
		 */
		myPushMatrix();
		// rotating around the "earth"
		myTranslatef(
			0,
			E,
			0.0f);
		gl.glLineWidth(2);
		myScalef(E / 8, E / 8, E / 8);
		// no need to self-rotate the sun
		drawSphere();
		myPopMatrix();

		if (distance(coneC, sphereC) < E / 5)
		{
			// collision detected, swap the rotation directions
			tmpD = coneD;
			coneD = sphereD;
			sphereD = tmpD;
		}
		if (distance(coneC, cylinderC) < E / 5)
		{
			// collision detected, swap the rotation directions
			tmpD = coneD;
			coneD = cylinderD;
			cylinderD = tmpD;
		}
		if (distance(cylinderC, sphereC) < E / 5)
		{
			// collision detected, swap the rotation directions
			tmpD = cylinderD;
			cylinderD = sphereD;
			sphereD = tmpD;
		}
	}


	/**
	 * distance between two points
	 * Copied from J2_11_ConeSolarCollision#distance()
	 * @param cl
	 * @param c2
	 * @return
	 */
	float distance(float[] cl, float[] c2)
	{
		float tmp = (c2[0] - cl[0]) * (c2[0] - cl[0]) + (c2[1] - cl[1]) * (c2[1] - cl[1]) + (c2[2] - cl[2]) * (c2[2] - cl[2]);
		return ((float) Math.sqrt(tmp));
	}


	public static void main(String[] args)
	{
		ConeSolarCollision f = new ConeSolarCollision();
		f.setTitle("Solar System Collision using JOGL");
		f.setSize(WIDTH, HEIGHT);
		f.setVisible(true);
	}
}