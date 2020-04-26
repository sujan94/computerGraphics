/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */

public class JOGL2_11_ConeSolar extends JOGL2_10_GenSolar
{

	void drawSolar(float E, float e, float M, float m)
	{

		// Global coordinates
		gl.glLineWidth(3);
		// coordOff = false; // cjx
		drawSphere(); // for loading matrix purpose
		drawColorCoord(WIDTH / 4, WIDTH / 4, WIDTH / 4);

		myPushMatrix();
		myRotatef(e * dg, 0.0f, 1.0f, 0.0f);
		// rotating around the "sun"; proceed angle
		myRotatef(tiltAngle * dg, 0.0f, 0.0f, 1.0f); // tilt angle
		myTranslatef(0.0f, E, 0.0f);
		myPushMatrix();
		myScalef(WIDTH / 20, WIDTH / 20, WIDTH / 20);
		drawSphere();
		myPopMatrix();
		myPushMatrix();
		myScalef(E / 8, E, E / 8);
		myRotatef(90 * dg, 1.0f, 0.0f, 0.0f); // orient the cone
		drawCone();
		myPopMatrix();

		myRotatef(m * dg, 0.0f, 1.0f, 0.0f);
		// rotating around the "earth"
		myTranslatef(M, 0.0f, 0.0f);
		gl.glLineWidth(2);
		myScalef(E / 8, E / 8, E / 8);
		myRotatef(cnt * dg, 0.0f, 1.0f, 0.0f); // self rotation
		drawSphere();
		drawColorCoord(2, 2, 2);
		myPopMatrix();
	}


	public static void main(String[] args)
	{
		new JOGL2_11_ConeSolar();
	}

}
