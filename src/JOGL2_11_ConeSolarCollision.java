/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 */



public class JOGL2_11_ConeSolarCollision extends JOGL2_11_ConeSolar {
	  //direction and speed of rotation
	  static float coneD = 0.03f;
	  static float sphereD = -0.04f;
	  static float cylinderD = 0.02f;
	  
	  // initial position
	  static float spherem = 0, cylinderm = 120, conem = 240;

	  // centers of the objects
	  static float[] earthC = new float[4];
	  static float[] coneC = new float[4];
	  static float[] sphereC = new float[4];
	  static float[] cylinderC = new float[4];
  
	 void drawSolar(float E, float e, float M, float m) {
		 float tiltAngle = 45*dg; 
		 float[] tmp = {0, 0, 0, 1};

		    myPushMatrix();
			    // Global coordinates
			    gl.glLineWidth(3);
			    //coordOff = false; // cjx
			    drawSphere(); // for loading matrix purpose
			    drawColorCoord(WIDTH/5, WIDTH/5, WIDTH/5);
			    
			    myRotatef(e, 0.0f, 1.0f, 0.0f);
			    // rotating around the "sun"; proceed angle
			    myRotatef(tiltAngle, 0.0f, 0.0f, 1.0f); // tilt angle
			    myTranslatef(0.0f, E, 0.0f);
			    myPushMatrix();
				    myScalef(WIDTH/8, WIDTH/8, WIDTH/8);
				    drawSphere();
				    gl.glLineWidth(2);
				    drawColorCoord(3, 3, 3);
				    // get the center of the earth
				    myTransHomoVertex(tmp, earthC);
	
				myPopMatrix();
			    myPushMatrix();
				    myScalef(E/4, E, E/4);
				    myRotatef(90*dg, 1.0f, 0.0f, 0.0f); // orient the cone
				    drawCone();
			    myPopMatrix();
		  
			    myPushMatrix();
			      cylinderm = cylinderm+cylinderD;
			      myRotatef(cylinderm, 0.0f, 1.0f, 0.0f);
			      // rotating around the "earth"
			      myTranslatef(M, 0.0f, 0.0f);
			      myScalef(E/8, E/8, E/8);
			      drawCylinder();
				  gl.glLineWidth(1);
			      drawColorCoord(2, 2, 2);
			      // retrieve the center of the cylinder
			      // the matrix is stored column major left to right
				    myTransHomoVertex(tmp, cylinderC);
			    myPopMatrix();
			    myPushMatrix();
			      spherem = spherem+sphereD;
			      myRotatef(spherem, 0.0f, 1.0f, 0.0f);
			      // rotating around the "earth"
			      myTranslatef(M, 0.0f, 0.0f);
			      myScalef(E/8, E/8, E/8);
			      drawSphere();
			      drawColorCoord(2, 2, 2);
			      // retrieve the center of the sphere
				  myTransHomoVertex(tmp, sphereC);
			    myPopMatrix();

			    myPushMatrix();
			      conem = conem+coneD;
			      myRotatef(conem, 0.0f, 1.0f, 0.0f);
			      // rotating around the "earth"
			      myTranslatef(M, 0.0f, 0.0f);
			      myScalef(E/8, E/8, E/8);
			      drawCone();
			      drawColorCoord(2, 2, 2);
			      // retrieve the center of the cone
				  myTransHomoVertex(tmp, coneC);
			    myPopMatrix();
			myPopMatrix();

		    if (distance(coneC, sphereC)<E/5) {
		        // collision detected, swap the rotation directions
		        float tmpD = coneD;
		        coneD = sphereD;
		        sphereD = tmpD;
		      }
		      if (distance(coneC, cylinderC)<E/5) {
		        // collision detected, swap the rotation directions
		        float tmpD = coneD;
		        coneD = cylinderD;
		        cylinderD = tmpD;
		      }
		      if (distance(cylinderC, sphereC)<E/5) {
		        // collision detected, swap the rotation directions
		        float tmpD = cylinderD;
		        cylinderD = sphereD;
		        sphereD = tmpD;
		      } 
	    }


	    // distance between two points
	    float distance (float[] c1, float[] c2) {
	    	
	      float tmp = (c2[0]-c1[0])*(c2[0]-c1[0])+
	                  (c2[1]-c1[1])*(c2[1]-c1[1])+
	                  (c2[2]-c1[2])*(c2[2]-c1[2]);

	      return (float)Math.sqrt((double)tmp);
	    }

	 
	 
	  public static void main(String[] args) {
	    new JOGL2_11_ConeSolarCollision();
	  }

}
