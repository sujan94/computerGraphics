import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

public class H3_sprajap2 extends JOGL1_1_PointVFfiles{

    private static final double RADIUS = 0.7;
    private static final int BOUNCING_POINTS_COUNT = 1000;
    private static final double ANGLE_INCREMENT = 2 * Math.PI / BOUNCING_POINTS_COUNT;
    private static final float PENTAGON_POINT_MOVE_THETA_VALUE = 0.5f;

    FPSAnimator animator; // for thread that calls display() repetitively

    // vertex array object (handle), for sending to the vertex shader
    int[] vao = new int[1];
    // vertex buffers objects (handles) to stores position, color, normal, etc
    int[] vbo = new int[2];
    // handle to shader programs
    int vfPrograms;
    private final float[] circlePointList;
    private final float[] nameFirstInitials;
    private final float[] nameLastInitials;
    private double currentAngle = 0;
    private float angle = 0;

    private static float[][][] myMatStack = new float[24][4][4]; // 24 layers for push and pop
    private static int stackPtr = 0;

    public H3_sprajap2() {
        // Frame per second animator
        animator = new FPSAnimator(canvas, 40); // 40 calls per second; frame rate
        animator.start();
        System.out.println("A thread that calls display() repeatitively.");
        circlePointList = generateCirclePoints();
        nameFirstInitials = generateInitialPoints('s');
        nameLastInitials = generateInitialPoints('p');
    }

    private float[] generateInitialPoints(char c) {
        int count = 0;
        ArrayList<Float> vPoint = new ArrayList<>();
        switch (java.lang.Character.toUpperCase(c)) {
            case 'S':
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.5));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.5));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count, (float) (RADIUS+ 0.2));
                break;
            case 'P':
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.5));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.2));
                vPoint.add(count++, (float) (RADIUS+ 0.5));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count++, (float) (RADIUS+ 0.5));
                vPoint.add(count++, (float) (RADIUS+ 0.8));
                vPoint.add(count, (float) (RADIUS+ 0.8));
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid input. We only support 's', 'S', 'p' and 'P' at the moment.", "Unsupported Input Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                System.exit(0);
                break;
        }

        float[] list = new float[vPoint.size()];
        for (int i = 0; i < vPoint.size(); i++) {
            list[i] = vPoint.get(i);
        }
        return list;
    }

    private float[] generateCirclePoints() {
        double currentAngle = 0;
        // I could do theta = theta + 1/r;
        float[] circlePoints = new float[3 * BOUNCING_POINTS_COUNT]; // predefined number of pixels on the line

        for (int i = 0; i < BOUNCING_POINTS_COUNT; i++) {
            currentAngle += ANGLE_INCREMENT;
            float x = (float) (RADIUS * Math.cos(currentAngle));
            float y = (float) (RADIUS * Math.sin(currentAngle));

            // write a pixel into the framebuffer, here we write into an array
            circlePoints[(i * 3)] = x; // normalize -1 to 1
            circlePoints[(i * 3) + 1] = y; // normalize -1 to 1
            circlePoints[(i * 3) + 2] = 0.0f;
        }
        return circlePoints;
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        // Clear the frame, so that we can see the color changing (Animation)
        float[] bgColor = {0.0f, 0.0f, 0.0f, 1.0f};
        FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
        gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame
        myLoadIdentity();
        // change the size of the points
        drawCircle();
        gl.glPointSize(2.0f);
        drawBouncingPoint();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        drawPentagon();
        myLoadIdentity();


        myLoadIdentity();

        float valueX = getPointForPentagon()[0]<0? getPointForPentagon()[0]-0.3f:getPointForPentagon()[0];
        float valueY = getPointForPentagon()[1]<0? getPointForPentagon()[1]-0.2f:getPointForPentagon()[1];
        myTranslatef(valueX,valueY,0);
        myScalef(0.1f, 0.1f, 0);
        drawWithAnimation(nameFirstInitials);
        myTranslatef(0.8f,0, 0);
        drawWithAnimation(nameLastInitials);

        myLoadIdentity();

        myLoadIdentity();
        myTranslatef(0.0f, 0.0f,0);
        myScalef(0.1f, 0.1f, 0);
        myRotatef(angle,0,0,1);
        drawWithAnimation(nameFirstInitials);
        myTranslatef(0.8f,0, 0);
        drawWithAnimation(nameLastInitials);

        myLoadIdentity();

        angle-= Math.toRadians(5);
    }

    private float[] getPointForPentagon() {

        int limit = 5;
        float angleIncrement = (360f / 5) + PENTAGON_POINT_MOVE_THETA_VALUE;
        // I could do theta = theta + 1/r;
        float[] pentagonPoints = new float[3 * limit]; // predefined number of pixels on the line
        int i = 0;
        while (i < pentagonPoints.length) {
            currentAngle = currentAngle + angleIncrement;
            float x = (float) (RADIUS * Math.cos(Math.toRadians(currentAngle % 360)));
            float y = (float) (RADIUS * Math.sin(Math.toRadians(currentAngle % 360)));

            // write a pixel into the framebuffer, here we write into an array
            pentagonPoints[(i++)] = x; // normalize -1 to 1
            pentagonPoints[i++] = y; // normalize -1 to 1
            pentagonPoints[i++] = 0.0f;
        }
        return pentagonPoints;
    }

    public void drawPentagon() {
        float[] pentagonPoints = getPointForPentagon();

        float[] color = new float[15];
        for (int i = 0; i < color.length; i++) {
            color[i] = (float) Math.random();
        }

        gl.glPointSize(15.0f);
        drawPointArray(pentagonPoints, color, GL_POINTS);
        drawPointArray(pentagonPoints, color, GL_LINE_LOOP);
    }


    void drawCircle() {
        // drawing the circle by passing the array to glDrawArray
        drawPointArray(circlePointList, new float[]{0.0f, 0.0f, 0.0f}, GL_LINE_LOOP);
    }

    void drawBouncingPoint() {
        int numberOfPoints = 20;
        float[] pointsInCircle = new float[3 * numberOfPoints];
        float[] color = new float[pointsInCircle.length];
        for (int i = 0; i < color.length; i++) {
            color[i] = (float) Math.random();
        }

        int i = 0;
        while (i < numberOfPoints) {
            // randomly pick a theta, in radians
            double angle = (2 * Math.random() - 1) * (2 * Math.PI);
            double random_radius = ((2 * Math.random() - 1) * RADIUS);

            float x = (float) (random_radius * Math.cos(angle));
            float y = (float) (random_radius * Math.sin(angle));

            pointsInCircle[(i * 3)] = x; // normalize -1 to 1
            pointsInCircle[(i * 3) + 1] = y; // normalize -1 to 1
            pointsInCircle[(i * 3) + 2] = 0.0f;
            i++;
        }
        drawPointArray(pointsInCircle, color, GL_POINTS);
    }

    public void drawPointArray(float[] vPoint, float[] colorArray, int mode) {

        // prepare Modelview matrix to be sent to the vertex shader as uniform
        float MV[] = new float[16];
        get_Matrix(MV); // get the modelview matrix from the matrix stack

        // connect the modelview matrix
        int mvLoc = gl.glGetUniformLocation(vfPrograms, "mv_matrix");
        gl.glProgramUniformMatrix4fv(vfPrograms, mvLoc, 1, false, MV, 0);

        // 3. load vbo[0] with vertex data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
        FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoint);
        gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
                vBuf, // the vertex positions
                GL_STATIC_DRAW); // the data is static
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer

        // 4. load vbo[1] with color data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1
        FloatBuffer cBuf = Buffers.newDirectFloatBuffer(colorArray);
        gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
                cBuf, // the vertex colors
                GL_STATIC_DRAW);
        gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer

        // Connect JOGL variable with shader variable by name
        int colorLoc = gl.glGetUniformLocation(vfPrograms, "iColor");
        gl.glProgramUniform3fv(vfPrograms, colorLoc, 1, cBuf);

        // 6. draw points: VAO has 1 array of corresponding vertices
        gl.glDrawArrays(mode, 0, (vBuf.limit() / 3));
    }

    // the vertices are transformed first then drawn
    public void drawWithAnimation(float[] v1) {

        // send color data to vertex shader through uniform (array): color here is not per-vertex
        FloatBuffer cBuf = Buffers.newDirectFloatBuffer(new float[]{1.0f, 1.0f, 1.0f});

        //Connect JOGL variable with shader variable by name
        int colorLoc = gl.glGetUniformLocation(vfPrograms, "iColor");
        gl.glProgramUniform3fv(vfPrograms, colorLoc, 1, cBuf);

        // prepare Modelview matrix to be sent to the vertex shader as uniform
        float MV[] = new float[16];
        get_Matrix(MV); // get the modelview matrix from the matrix stack

        // connect the modelview matrix
        int mvLoc = gl.glGetUniformLocation(vfPrograms, "mv_matrix");
        gl.glProgramUniformMatrix4fv(vfPrograms, mvLoc, 1, false, MV, 0);

        // load vbo[0] with vertex data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
        FloatBuffer vBuf = Buffers.newDirectFloatBuffer(v1);
        gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES,  //# of float * size of floats in bytes
                vBuf, // the vertex positions
                GL_STATIC_DRAW); // the data is static
        gl.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer

        // draw a triangle
        gl.glDrawArrays(GL_LINE_STRIP, 0, vBuf.limit() / 2);
    }

    // return the current matrix on top of the Modelview matrix stack
    public void get_Matrix(float M[]) {

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                M[i * 4 + j] = myMatStack[stackPtr][j][i];
    }

    // initialize a matrix to all zeros
    private void myClearMatrix(float mat[][]) {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mat[i][j] = 0.0f;
            }
        }
    }


    // initialize a matrix to Identity matrix
    private void myIdentity(float mat[][]) {

        myClearMatrix(mat);
        for (int i = 0; i < 4; i++) {
            mat[i][i] = 1.0f;
        }
    }


    // initialize the current matrix to Identity matrix
    public void myLoadIdentity() {
        myIdentity(myMatStack[stackPtr]);
    }


    // multiply the current matrix with mat
    public void myMultMatrix(float mat[][]) {
        float matTmp[][] = new float[4][4];

        myClearMatrix(matTmp);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    matTmp[i][j] +=
                            myMatStack[stackPtr][i][k] * mat[k][j];
                }
            }
        }
        // save the result on the current matrix
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                myMatStack[stackPtr][i][j] = matTmp[i][j];
            }
        }
    }


    // multiply the current matrix with a translation matrix
    public void myTranslatef(float x, float y, float z) {
        float T[][] = new float[4][4];

        myIdentity(T);

        T[0][3] = x;
        T[1][3] = y;
        T[2][3] = z;

        myMultMatrix(T);
    }


    // multiply the current matrix with a rotation matrix
    public void myRotatef(float angle, float x, float y, float z) {
        float R[][] = new float[4][4];

        // normalize the vector: I notices a drifting effect in my implementation
        // if I am not rotating around a primary axis, it will drift to be larger or smaller
        x = x / (float) Math.sqrt(x * x + y * y + z * z);
        y = y / (float) Math.sqrt(x * x + y * y + z * z);
        z = z / (float) Math.sqrt(x * x + y * y + z * z);

        float c = (float) Math.cos(angle); // gradian
        float s = (float) Math.sin(angle);

        myIdentity(R);

        R[0][0] = x * x * (1 - c) + c;
        R[0][1] = x * y * (1 - c) - z * s;
        R[0][2] = x * z * (1 - c) + y * s;
        R[1][0] = y * x * (1 - c) + z * s;
        R[1][1] = y * y * (1 - c) + c;
        R[1][2] = y * z * (1 - c) - x * s;
        R[2][0] = z * x * (1 - c) - y * s;
        R[2][1] = z * y * (1 - c) + x * s;
        R[2][2] = z * z * (1 - c) + c;

        myMultMatrix(R);
    }


    // multiply the current matrix with a scale matrix
    public void myScalef(float x, float y, float z) {
        float S[][] = new float[4][4];

        myIdentity(S);

        S[0][0] = x;
        S[1][1] = y;
        S[2][2] = z;

        myMultMatrix(S);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        System.out.println("\na) Init is called once: ");

        String[] vShaderSource;
        String[] fShaderSource;
        gl = (GL4) drawable.getGL();

        System.out.println("	load the shader programs; ");

        vShaderSource = readShaderSource("src/project2/h3_V.shader"); // read vertex shader
        fShaderSource = readShaderSource("src/project2/hw3_F.shader"); // read fragment shader
        vfPrograms = initShaders(vShaderSource, fShaderSource);

        // 1. generate vertex arrays indexed by vao
        gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
        System.out.println("	Generate VAO: " + vao.length); // we only use one vao
        gl.glBindVertexArray(vao[0]); // use handle 0

        // 2. generate vertex buffers indexed by vbo: here to store vertices and colors
        gl.glGenBuffers(vbo.length, vbo, 0);
        System.out.println("	Generate VBO: " + vbo.length); // we use two: position and color

        // 5. enable VAO with loaded VBO data
        gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
        gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
        System.out.println("	Enable corresponding vertex attributes.\n"); // we use two: position and color
    }


    public static void main(String[] args) {
        H3_sprajap2 project = new H3_sprajap2();
        project.setTitle("HW3");
        project.setVisible(true);
    }
}
