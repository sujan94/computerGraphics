
/*************************************************
 * Created on August 1, 2018, @author: Jim X. Chen
 *
 * Draw randomly generated points into both buffers one at a time
 * Since each point is sent to the vertex shader serially, there is no parallel rendering (efficiency)
 * 
 * This is to implement the text book's example: J_1_1_Point
 */

import static com.jogamp.opengl.GL4.*;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLAutoDrawable;

public class JOGL1_4_1_Point extends JOGL1_3_VertexArray
{

	public void display(GLAutoDrawable drawable)
	{
		float vPoint[] = { 0.0f, 0.0f, 0.0f };
		float vColor[] = { 1.0f, 0.0f, 0.0f };

		// 1. draw into both buffers
		gl.glDrawBuffer(GL_FRONT_AND_BACK);
		// try { Thread.sleep(500); } catch (Exception ignore) {}

		// 2. generate a random point
		vPoint[0] = (float) (2 * Math.random() - 1);
		vPoint[1] = (float) (2 * Math.random() - 1);

		// 3. generate a random color
		vColor[0] = (float) Math.random();
		vColor[1] = (float) Math.random();
		vColor[2] = (float) Math.random();

		gl.glPointSize(4.0f);
		// specify to draw the point with the color
		drawPoint(vPoint, vColor);
	}


	// specify to draw a point
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


	public static void main(String[] args)
	{
		new JOGL1_4_1_Point();

	}
}
