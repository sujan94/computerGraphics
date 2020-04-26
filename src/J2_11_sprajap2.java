/* draw a cone solar system with collisions of the moons */

public class J2_11_sprajap2 extends JOGL2_11_ConeSolar {
    // direction and speed of rotation
    static float coneD = WIDTH / 110000f;

    static float sphereD = -WIDTH / 64000f;

    static float cylinderD = WIDTH / 300000f;

    static float spherem = 0.000012f, cylinderm = 0.000000024f;

    static float tmpD = 0, conem = 0; // centers of the objects

    static float[] coneC = new float[3];

    static float[] sphereC = new float[3];

    static float[] cylinderC = new float[3];

    // current matrix on the matrix stack

    static float[] currM = new float[16];

    void drawSolar(float E, float e, float M, float m) {

        // Global coordinates
        gl.glLineWidth(3);
        //coordOff = false; // cjx
        drawSphere(); // for loading matrix purpose
        drawColorCoord(WIDTH / 4, 0.0f, 0.0f);

        myPushMatrix();
        myRotatef(e * dg, 0.0f, 1.0f, 0.0f);
        // rotating around the "sun"; proceed angle
        //TODO enable this tilt angle.
        //    myRotatef(tiltAngle*dg, 0.0f, 0.0f, 1.0f); // tilt angle
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

        // cone
        myPushMatrix();
        conem = conem + coneD;
        myRotatef(conem, 0.0f, 1.0f, 0.0f);
        // rotating around the "earth"
        myTranslatef(M * 2, 0.0f, 0.0f);
        gl.glLineWidth(2);
        myScalef(E / 8, E / 8, E / 8);
        myRotatef(cnt * dg, 0.0f, 1.0f, 0.0f); // self rotation
        drawCone();
        drawColorCoord(2, 2, 2);
        // retrieve the center of the cone
        get_Matrix(currM);

        coneC[0] = currM[12];
        coneC[1] = currM[13];
        coneC[2] = currM[14];
        myPopMatrix();

        // sphere
        myPushMatrix();
        spherem = spherem + sphereD;
        myRotatef( spherem, 0.0f, 1.0f, 0.0f);
        // rotating around the "earth"
        myTranslatef(M * 2, 0.0f, M);
        gl.glLineWidth(2);
        myScalef(E / 8, E / 8, E / 8);
        myRotatef(cnt * dg * 5, 0.0f, 1.0f, 0.0f); // self rotation
        drawSphere();
        drawColorCoord(2, 2, 2);
        // retrieve the center of the sphere
        get_Matrix(currM);
        sphereC[0] = currM[12];
        sphereC[1] = currM[13];
        sphereC[2] = currM[14];
        myPopMatrix();

        // cylinder
        myPushMatrix();
        cylinderm = cylinderm + cylinderD;
        myRotatef(2f * cylinderm, 0.0f, 1.0f, 0.0f);
        // rotating around the "earth"
        myTranslatef(M*2, 0.0f, M*2);
        gl.glLineWidth(2);
        myScalef(E / 8, E / 8, E / 8);
        myRotatef(cnt * dg * 2, 0.0f, 1.0f, 0.0f); // self rotation
        drawCylinder();
        drawColorCoord(2, 2, 2);
        // retrieve the center of the cylinder
        // the matrix is stored column major left to right
        get_Matrix(currM);
        cylinderC[0] = currM[12];
        cylinderC[1] = currM[13];
        cylinderC[2] = currM[14];
        myPopMatrix();
        myPopMatrix();

        if (distance(coneC, sphereC) < E / 5) {
            System.out.println("Collision Occured between cone and sphereC");
            // collision detected, swap the rotation directions
//            tmpD = coneD;
//            coneD = sphereD;
//            sphereD = tmpD;
            sphereD = -1 * sphereD;
            coneD = -1 *coneD;
        }
        if (distance(coneC, cylinderC) < E / 5) {
            System.out.println("Collision Occured between cone and cylinder");
            // collision detected, swap the rotation directions
//            tmpD = coneD;
//            coneD = cylinderD;
//            cylinderD = tmpD;
            coneD = -1 *coneD;
            cylinderD = -1 *cylinderD;
        }
        if (distance(cylinderC, sphereC) < E / 5) {
            System.out.println("Collision Occured between sphere and cylinder");
            // collision detected, swap the rotation directions
            tmpD = cylinderD;
//            cylinderD = sphereD;
//            sphereD = tmpD;
            cylinderD = -1 *cylinderD;
            sphereD = -1 *sphereD;
        }
    }


    // distance between two points
    float distance(float[] cl, float[] c2) {
        float tmp = (c2[0] - cl[0]) * (c2[0] - cl[0]) + (c2[1] - cl[1]) * (c2[1] - cl[1]) + (c2[2] - cl[2]) * (c2[2] - cl[2]);
        return ((float) Math.sqrt(tmp));
    }


    public static void main(String[] args) {
        J2_11_sprajap2 f = new J2_11_sprajap2();
        f.setTitle("JOGL J2_11_ConeSolarCollision");
        f.setSize(WIDTH, HEIGHT);
        f.setVisible(true);
    }
}