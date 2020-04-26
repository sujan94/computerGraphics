/*************************************************
 * Created on August 10, 2019, @author: Jim X. Chen
 *
 * Achieving clipping in the fragment shader
 * 
 * This is to implement the text book's example: J_1_3_windowClipping
 */

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import static com.jogamp.opengl.GL4.*;


public class JOGL1_4_3_windowClipping extends JOGL1_4_3_Triangle {
	
	public void display(GLAutoDrawable drawable) {		
		
		int v[][] = new int[3][3]; // each vertex is v[i]
		// This is the viewport physical coordinates
		gl.glViewport(0, 0, WIDTH, HEIGHT);

		super.display(drawable); 

	    // Draw clipping boundaries
		v[0][0] = (int) WIDTH/4;
		v[0][1] = (int) HEIGHT/4;
		v[1][0] = (int) 3*WIDTH/4;
		v[1][1] = (int) HEIGHT/4;
		bresenhamLine(v[0][0], v[0][1], v[1][0], v[1][1]); 
	    // Draw clipping boundaries
		v[0][0] = (int) 3*WIDTH/4;
		v[0][1] = (int) HEIGHT/4;
		v[1][0] = (int) 3*WIDTH/4;
		v[1][1] = (int) 3*HEIGHT/4;
		bresenhamLine(v[0][0], v[0][1], v[1][0], v[1][1]); 
	    // Draw clipping boundaries
		v[0][0] = (int) WIDTH/4;
		v[0][1] = (int) 3*HEIGHT/4;
		v[1][0] = (int) 3*WIDTH/4;
		v[1][1] = (int) 3*HEIGHT/4;
		bresenhamLine(v[0][0], v[0][1], v[1][0], v[1][1]); 
	    // Draw clipping boundaries
		v[0][0] = (int) WIDTH/4;
		v[0][1] = (int) 3*HEIGHT/4;
		v[1][0] = (int) WIDTH/4;
		v[1][1] = (int) HEIGHT/4;
		bresenhamLine(v[0][0], v[0][1], v[1][0], v[1][1]); 
	}

	
	
	public void init(GLAutoDrawable drawable) {
			gl = (GL4) drawable.getGL();
			String vShaderSource[], fShaderSource[] ;
						
			vShaderSource = readShaderSource("src/JOGL1_4_3_V.shader"); // read vertex shader
			fShaderSource = readShaderSource("src/JOGL1_4_3_windowClipping_F.shader"); // read fragment shader
			vfPrograms = initShaders(vShaderSource, fShaderSource);		
			
			// 1. generate vertex arrays indexed by vao
			gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
			gl.glBindVertexArray(vao[0]); // use handle 0
			
			// 2. generate vertex buffers indexed by vbo: here vertices and colors
			gl.glGenBuffers(vbo.length, vbo, 0);
			
			// 3. enable VAO with loaded VBO data
			gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
			gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
			
 		}
		
	
	public static void main(String[] args) {
		 new JOGL1_4_3_windowClipping();

	}
}


