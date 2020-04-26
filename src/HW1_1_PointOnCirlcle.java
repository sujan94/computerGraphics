
/**
 * <p>Title: Foundations of 3D Graphics Programming : Using JOGL and Java3D </p>
 *
 * <p>Description: A point moves in a cirlce ...</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: George Mason University</p>
 *
 * @author Dr. Jim X. Chen
 * @version 1.0
 */

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;

public class HW1_1_PointOnCirlcle extends JOGL1_4_3_Line
{

	double cx = WIDTH / 2, cy = HEIGHT / 2, // center of the circle
			r = WIDTH / 3, // radius of the circle
			theta, // current angle
			delta = 1 / r; // angle for one unit apart: 2PI/2PI*r

	// Called for OpenGL rendering every reshape
	public void display(GLAutoDrawable drawable)
	{

		// 1. clear the display
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// 2. draw a white circle
		// gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glPointSize(1);
		drawCircle(cx, cy, r);

		// delta is about one unit length width in logical coord.
		theta = theta + delta;
		double x = r * Math.cos(theta) + cx;
		double y = r * Math.sin(theta) + cy;

		// 3. draw a red point on Circle
		gl.glPointSize(8);
		// gl.glColor3f(1.0f, 0.0f, 0.0f);
		drawPoint(new float[] { (float) x, (float) y }, vColors);
		// sleep to slow down the rendering
		try
		{
			Thread.sleep(10);
		}
		catch (Exception ignore)
		{
		}
	}


	public void drawCircle(double x0, double y0, double r)
	{

		double th = 0;

		while (th <= Math.PI / 4)
		{
			th = th + delta;
			double x = r * Math.cos(th);
			double y = r * Math.sin(th);
			drawPoint(new float[] { (float) (x + x0), (float) (y + y0) }, vColors);
			drawPoint(new float[] { (float) (x + x0), (float) (-y + y0) }, vColors);
			drawPoint(new float[] { (float) (-x + x0), (float) (y + y0) }, vColors);
			drawPoint(new float[] { (float) (-x + x0), (float) (-y + y0) }, vColors);
			drawPoint(new float[] { (float) (y + x0), (float) (x + y0) }, vColors);
			drawPoint(new float[] { (float) (y + x0), (float) (-x + y0) }, vColors);
			drawPoint(new float[] { (float) (-y + x0), (float) (x + y0) }, vColors);
			drawPoint(new float[] { (float) (-y + x0), (float) (-x + y0) }, vColors);

		}
	}


	public static void main(String[] args)
	{
		HW1_1_PointOnCirlcle f = new HW1_1_PointOnCirlcle();

		// modified by ddu to show this example title
		f.setTitle("HW1 - a point moves in a circle");
		f.setSize(WIDTH, HEIGHT);
		f.setVisible(true);

	}

}
