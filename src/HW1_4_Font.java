import com.jogamp.opengl.GLAutoDrawable;

/**
 * 
 * 
 * <p>
 * Title: Foundations of 3D Graphics Programming : Using JOGL and Java3D
 * </p>
 *
 * <p>
 * Description: using glut fonts ...
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 *
 * <p>
 * Company: George Mason University
 * </p>
 *
 * @author Dr. Jim X. Chen
 * @version 1.0
 */
public class HW1_4_Font extends HW1_3_Pentagon
{

	public void display(GLAutoDrawable drawable)
	{

		super.display(drawable);

		// draw a stroke string in the center (no clipping)
		// gl.glPushMatrix();
		// gl.glTranslated(cx, cy, 0); // put it at the center of circle
		// gl.glScalef(0.2f, 0.2f, 0.2f); // scale the sentence
		// gl.glRotated(-180 * theta / Math.PI, 0, 0, 1); // rotate it according to degrees
		// gl.glTranslated(-JOGL1_0_Frame.WIDTH / 2, -5, 0); // put the world in the center of coord
		// glut.glutStrokeString(GLUT.STROKE_MONO_ROMAN, "HELLO WORLD!");
		// gl.glPopMatrix();

		// delta is about a pixel width in logical coord.
		theta = theta - delta;

		for (int i = 1; i <= 5; i++)
		{
			int x = (int) (r * Math.cos(theta + 2 * i * Math.PI / 5) + cx);
			int y = (int) (r * Math.sin(theta + 2 * i * Math.PI / 5) + cy);

			// draw a red point on Circle
			gl.glPointSize(8);
			// todo - gl.glColor3f(1.0f, 0.0f, 0.0f);

			// this one was updated
			drawPoint(new float[] { (float) x, (float) y }, vColor);
			gl.glPointSize(1);

			// label the current vertex
			// todo - gl.glColor3f(1f, 1f, 0f);
			labelVertex(x, y, i);
		}
	}


	public void labelVertex(int x, int y, int i)
	{
		/**
		 * todo
		 * <pre>
		// label the vertex
		gl.glRasterPos3f(x, y, 0); // start poistion
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Vertex[");
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, Integer.toString(i));
		glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, ']');
		 </pre>
		 */
	}


	public static void main(String[] args)
	{
		HW1_4_Font f = new HW1_4_Font();

		f.setTitle("HW1_4 - using font");
		f.setSize(WIDTH, HEIGHT);
		f.setVisible(true);

	}
}
