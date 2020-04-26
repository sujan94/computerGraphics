/* draw a cone solar system with collisions of the moons */

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL2;

public class J2_11_ConeSolarCollision extends JOGL2_11_ConeSolar
{
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

    J2_11_ConeSolarCollision()
    {

    }


    void drawSolar(float E, float e, float M, float m)
    {
        // Global coordinates gl.glLineWidth(8);
        drawColorCoord(WIDTH / 4, WIDTH / 4, WIDTH / 4);
        myPushMatrix();

        myRotatef(e, 0.0f, 1.0f, 0.0f); //
        // rotating around the "sun"; proceed angle
        myRotatef(alpha, 0.0f, 0.0f, 1.0f);
        // tilt angle
        myTranslatef(0.0f, E, 0.0f);

        myPushMatrix();
        myScalef(WIDTH / 20, WIDTH / 20, WIDTH / 20);
        drawSphere();
        myPopMatrix();
        ;
        myPushMatrix();
        myScalef(E / 8, E, E / 8);
        myRotatef(90, 1.0f, 0.0f, 0.0f);
        // orient the cone
        drawCone();
        myPopMatrix();
        myPushMatrix();
        cylinderm = cylinderm + cylinderD;
        myRotatef(cylinderm, 0.0f, 1.0f, 0.0f);
        // rotating around the "earth"
        myTranslatef(M * 2, 0.0f, 0.0f);
        gl.glLineWidth(4);
        drawColorCoord(WIDTH / 8, WIDTH / 8, WIDTH / 8);
        myScalef(E / 8, E / 8, E / 8);
        drawCylinder();
        // retrieve the center of the cylinder
        // the matrix is stored column major left to right
        get_Matrix(currM);
        cylinderC[0] = currM[12];
        cylinderC[1] = currM[13];
        cylinderC[2] = currM[14];
        myPopMatrix();

        myPushMatrix();
        spherem = spherem + sphereD;
        myRotatef(spherem, 0.0f, 1.0f, 0.0f);
        // rotating around the "earth"
        myTranslatef(M * 2, 0.0f, 0.0f);
        drawColorCoord(WIDTH / 8, WIDTH / 8, WIDTH / 8);
        myScalef(E / 8, E / 8, E / 8);
        drawSphere();
        // retrieve the center of the sphere
        get_Matrix(currM);
        sphereC[0] = currM[12];
        sphereC[1] = currM[13];
        sphereC[2] = currM[14];
        myPopMatrix();
        myPushMatrix();
        conem = conem + coneD;
        myRotatef(conem, 0.0f, 1.0f, 0.0f);

        // rotating around the "earth"
        myTranslatef(M * 2, O, O);
        drawColorCoord(WIDTH / 8, WIDTH / 8, WIDTH / 8);
        myScalef(E / 8, E / 8, E / 8);
        drawCone();
        // retrieve the center of the cone
        get_Matrix(currM);

        coneC[0] = currM[12];
        coneC[1] = currM[13];
        coneC[2] = currM[14];
        myPopMatrix();
        myPopMatrix();

        if (distance(coneC, sphereC) < E / 5)
        {
            System.out.println("Collision Occured between cone and sphereC "+ distance(coneC, sphereC));
            // collision detected, swap the rotation directions
            tmpD = coneD;
            coneD = sphereD;
            sphereD = tmpD;
        }
        if (distance(coneC, cylinderC) < E / 5)
        {
            System.out.println("Collision Occured between cone and cylinder "+ distance(coneC, sphereC));
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

    // return the current matrix on top of the Modelview matrix stack
    public void get_Matrix(float M[]) {

        for (int i = 0; i < 4; i++ )
            for (int j = 0; j < 4; j++ )
                M[i*4+j] = myMatStack[stackPtr][j][i];
    }


    // distance between two points
    float distance(float[] cl, float[] c2)
    {
        float tmp = (c2[0] - cl[0]) * (c2[0] - cl[0]) + (c2[1] - cl[1]) * (c2[1] - cl[1]) + (c2[2] - cl[2]) * (c2[2] - cl[2]);
        return ((float) Math.sqrt(tmp));
    }


    public static void main(String[] args)
    {
        J2_11_ConeSolarCollision f = new J2_11_ConeSolarCollision();
        f.setTitle("JOGL J2_11_ConeSolarCollision");
        f.setSize(WIDTH, HEIGHT);
        f.setVisible(true);
    }
}