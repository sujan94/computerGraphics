/*************************************************
 * Created on September 17, 2018, @author: Jim X. Chen
 *
 * Draw a circle using line segments
 * 
 * This is to implement the text book's example: J_1_3_LineCircle
 * 
 * The program use JOGL1_4_2_V.shader and JOGL1_4_2_F.shader
 */


import com.jogamp.opengl.*;


public class JOGL1_4_3_LineCircle extends JOGL1_4_3_Line {

	
	  public void display(GLAutoDrawable drawable) {
		  
		    // a blue circle 
			color[0] = 0; 
			color[1] = 0; 
			color[2] = 1; // make it blue 
			
			uploadColor(color); 
		    circle(WIDTH/2, HEIGHT/2, HEIGHT/3);
		  }


		  // Our circle algorithm: center (cx, cy); radius: r
		  void circle(double cx, double cy, double r) {
		    double xn, yn, theta = 0;
		    double delta = 100/r; // the delta angle for a line segment: 100 pixels
		    double x0 = r*Math.cos(theta)+cx;
		    double y0 = r*Math.sin(theta)+cy;


		    while (theta<2*Math.PI) {
		    	theta = theta + delta; 
		         xn = r*Math.cos(theta)+cx;
		         yn = r*Math.sin(theta)+cy;
		         bresenhamLine((int)x0, (int)y0, (int)xn, (int)yn);
		   	     x0 = xn; 
		   	     y0 = yn; 
		    }
		  }



	
	
	public static void main(String[] args) {
		 new JOGL1_4_3_LineCircle();

	}
}




