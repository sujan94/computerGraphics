/* draw a cone solar system with collisions of the moons */

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.gl2.GLUT;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;

public class H3_sprajap2 extends JOGL2_11_ConeSolar {
    // direction and speed of rotation
    static float coneD = WIDTH / 110000f;

    static float sphereD = -WIDTH / 640000f;

    static float cylinderD = WIDTH / 300000f;

    static float spherem = 0.00012f, cylinderm = 0.000024f;

    static float  conem = 0.05f; // centers of the objects

    static float[] coneC = new float[3];

    static float[] sphereC = new float[3];

    static float[] cylinderC = new float[3];
    static float[] earthC = new float[3];

    // current matrix on the matrix stack

    static float[] currM = new float[16];

    int w = WIDTH, h = HEIGHT;

    @Override
    public void display(GLAutoDrawable glDrawable) {
        cnt++;
        depth = (cnt / 50) % 6;

        gl.glClear(GL_COLOR_BUFFER_BIT |
                GL_DEPTH_BUFFER_BIT);

        viewPort1();
        viewPort2();
    }

    public void viewPort1() {

        gl.glViewport(0, 0, w, h);
        //drawRobot(O, A, B, C, alpha, beta, gama);
        drawSolar(WIDTH / 4, 2.5f * cnt, WIDTH / 6, 0);
        // the objects' centers are retrieved from the above call
    }

    public void viewPort2() {
        int w = WIDTH, h = HEIGHT;

        viewPort1();
        gl.glViewport(0, h / 2, w / 2, h / 2);
        myPushMatrix();
        // earthC retrieved in drawSolar() before viewPort3
        mygluLookAt(sphereC[0], sphereC[1], sphereC[2], earthC[0], earthC[1], earthC[2], earthC[0], earthC[1], earthC[2]);
        drawSolar(WIDTH / 4, 2.5f * cnt, WIDTH / 6, 0);
        myPopMatrix();
    }

    void drawSolar(float E, float e, float M, float m) {

        // Global coordinates
        gl.glLineWidth(3);
        drawSphere(); // for loading matrix purpose
        drawColorCoord(WIDTH / 4, WIDTH / 4, WIDTH / 4);

        myPushMatrix();
        myRotatef(e * dg, 0.0f, 1.0f, 0.0f);
        // rotating around the "sun"; proceed angle
      //  myRotatef(tiltAngle * dg, 0.0f, 0.0f, 1.0f); // tilt angle
        myTranslatef(0.0f, E, 0.0f);
        myPushMatrix();
        myScalef(WIDTH / 20, WIDTH / 20, WIDTH / 20);
        drawSphere();
        get_Matrix(currM);
        earthC[0] = currM[12];
        earthC[1] = currM[13];
        earthC[2] = currM[14];
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
        myRotatef(spherem, 0.0f, 1.0f, 0.0f);
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
        myRotatef(3 * cylinderm, 0.0f, 1.0f, 0.0f);
        // rotating around the "earth"
        myTranslatef(M * 1.5f, 0.0f, M );
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
            sphereD = -1 * sphereD;
            coneD = -1 * coneD;
        }
        if (distance(coneC, cylinderC) < E / 5) {
            System.out.println("Collision Occured between cone and cylinder");
            // collision detected, swap the rotation directions
            coneD = -1 * coneD;
            cylinderD = -1 * cylinderD;
        }
        if (distance(cylinderC, sphereC) < E / 5) {
            System.out.println("Collision Occured between sphere and cylinder");
            // collision detected, swap the rotation directions
            cylinderD = -1 * cylinderD;
            sphereD = -1 * sphereD;
        }
    }

    // return the current matrix on top of the Modelview matrix stack
    public void get_Matrix(float M[]) {

        for (int i = 0; i < 4; i++ )
            for (int j = 0; j < 4; j++ )
                M[i*4+j] = myMatStack[stackPtr][j][i];
    }

    public void drawColorCoord(float xlen, float ylen,
                               float zlen) {
        GLUT glut = new GLUT();

        // if (coordOff) return; // cjx for images

        float vColor[] = {1, 0, 0};
        float vPoint[] = {0, 0, 0, 0, 0, 0};

        vPoint[3] = xlen;
        drawLineJOGL(vPoint, vColor);
        vColor[0] = 0; vColor[1] = 1;
        vPoint[3] = 0;
        vPoint[4] = ylen;
        drawLineJOGL(vPoint, vColor);
        vColor[1] = 0; vColor[2] = 1;
        vPoint[4] = 0;
        vPoint[5] = zlen;
        drawLineJOGL(vPoint, vColor);
    }


    // distance between two points
    float distance(float[] cl, float[] c2) {
        float tmp = (c2[0] - cl[0]) * (c2[0] - cl[0]) + (c2[1] - cl[1]) * (c2[1] - cl[1]) + (c2[2] - cl[2]) * (c2[2] - cl[2]);
        return ((float) Math.sqrt(tmp));
    }


    public void mygluLookAt(
            double eX, double eY, double eZ,
            double cX, double cY, double cZ,
            double upX, double upY, double upZ) {
        //eye and center are points, but up is a vector

        double[] F = new double[3];
        double[] UP = new double[3];
        double[] s = new double[3];
        double[] u = new double[3];

        F[0] = cX - eX;
        F[1] = cY - eY;
        F[2] = cZ - eZ;
        UP[0] = upX;
        UP[1] = upY;
        UP[2] = upZ;
        normalize(F);
        normalize(UP);
        crossProd(F, UP, s);
        crossProd(s, F, u);

        float[][] M = new float[4][4];

        M[0][0] = (float) s[0];
        M[1][0] = (float) u[0];
        M[2][0] = (float) -F[0];
        M[3][0] = (float) 0;
        M[0][1] = (float) s[1];
        M[1][1] = (float) u[1];
        M[2][1] = (float) -F[1];
        M[3][1] = (float) 0;
        M[0][2] = (float) s[2];
        M[1][2] = (float) u[2];
        M[2][2] = (float) -F[2];
        M[3][2] = 0;
        M[0][3] = 0;
        M[1][3] = 0;
        M[2][3] = 0;
        M[3][3] = 1;

        myMultMatrix(M);
        myTranslatef((float) -eX, (float) -eY, (float) -eZ);
    }


    public void normalize(double v[]) {

        double d = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);

        if (d == 0) {
            System.out.println("0 length vector: normalize().");
            return;
        }
        v[0] /= d;
        v[1] /= d;
        v[2] /= d;
    }


    public void crossProd(double U[],
                          double V[], double W[]) {
        // W = U X V
        W[0] = U[1] * V[2] - U[2] * V[1];
        W[1] = U[2] * V[0] - U[0] * V[2];
        W[2] = U[0] * V[1] - U[1] * V[0];
    }


    public static void main(String[] args) {
        H3_sprajap2 f = new H3_sprajap2();
        f.setTitle("JOGL H3_sprajap2");
        f.setSize(WIDTH, HEIGHT);
        f.setVisible(true);
    }
}