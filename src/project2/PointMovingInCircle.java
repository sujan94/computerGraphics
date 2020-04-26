package project2;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.logging.Logger;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

/**
 * @author Prakash Dhimal
 * George Mason University
 * Computer Graphics (CS 551)
 * Spring 2020
 * <p>
 * Homework 1:
 * <p>
 * Draw a point that moves on a circle or bounces in a circle.
 * You should have a file name: H1_pdhimal.java.
 * If you have shader files, they are H1_pdhimal_V.shader and H1_pdhimal_F.shader.
 * <p>
 * <p>
 * Vertex shader and fragment shader
 * <p>
 * - the vertex shader runs for all vertices in parallel
 * - the fragment shader is to set the RGB color of the pixel to be displayed.
 * <p>
 * The size of the window is 800 x 800 set in {@link JOGL1_0_Frame}
 * <p>
 * This class will first initiale a circle with 360 points.
 * Each pair of points (x,y) are stored.
 * <p>
 * A stack is used to draw the moving cirle
 * The whole circle is also drawn using the saved list of points
 * <p>
 * This class uses the following shader files:
 * - JOGL1_3_V.shader
 * - JOGL1_3_F.shader
 */
class PointMovingInCircle extends JOGL1_0_Point {

    /**
     * Inner class that contains the x, y values for a circle.
     */
    static class PointPair {
        private float x;

        private float y;


        PointPair(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

    }

    // vertex array object (handle), for sending to the vertex shader
    int[] vao = new int[1];

    // vertex buffers objects (handles) to stores position, color, normal, etc
    int[] vbo = new int[2];

    private final float[] movingPointColor;

    private final float[] allPointsColor;

    private float[] vPoint;

    /**
     * I am going to maintain a stack for drawing the current circle, and a list for storing
     * the points in the whole circle
     * <p>
     * When the current circle is complete, I will copy the full circle stored in the list and do it all over again
     */
    private Stack<PointPair> currentStack;

    private List<PointPair> savedCircle;

    // for the animation
    FPSAnimator animator;

    // handle to shader programs
    int vfPrograms;


    public PointMovingInCircle() {
        /**
         * I will keep this at 60 frames per seconds.
         *
         * Since there are 360 points to go around, I don't need to call {@link Thread#sleep(long)} to slow things down.
         */
        animator = new FPSAnimator(canvas, 60);
        animator.start();
        // two stacks
        currentStack = new Stack<>();
        savedCircle = new ArrayList<>();
        initializeCircleStack();

        movingPointColor = new float[]{255.0f, 0.0f, 0.0f};

        // all white circle
        allPointsColor = new float[]{0.0f, 0.0f, 0.0f};

        vPoint = new float[]{0.0f, 0.0f, 0.0f};
    }

    FPSAnimator animator() {
        return animator;
    }


    void start() {
        animator().start();
    }

    /**
     * Puts the circle to the current stack
     */
    private void initializeCircleStack() {

        /**
         * Add 360 points to the list
         */
        int numberOfPoints = 720;
        double radiusOfCircle = 0.5;
        double currentAngle;
        double angleIncrement = 2 * Math.PI / numberOfPoints;

        for (int i = 0; i < numberOfPoints; i++) {
            currentAngle = i * angleIncrement;
            float x = (float) (radiusOfCircle * Math.cos(currentAngle));
            float y = (float) (radiusOfCircle * Math.sin(currentAngle));

            // now that we have our X, and Y, lets add it to our list
            PointPair pointPair = new PointPair(x, y);
            savedCircle.add(pointPair);
            currentStack.push(pointPair);
        }
    }


    /**
     * Copied from {@link JOGL1_3_VertexArray#init(GLAutoDrawable)}
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        // array of vertices and colors corresponding to the vertices: a triangle
        float[] vPoints = {-0.5f, 0.0f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f};
        float[] vColors = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};

        System.out.println("\na) Init is called once: ");

        String[] vShaderSource;
        String[] fShaderSource;
        gl = (GL4) drawable.getGL();

        System.out.println("	load the shader programs; ");

        vShaderSource = readShaderSource("srcp/JOGL1_3_V.shader"); // read vertex shader
        fShaderSource = readShaderSource("srcp/JOGL1_3_F.shader"); // read fragment shader
        vfPrograms = initShaders(vShaderSource, fShaderSource);

        // 1. generate vertex arrays indexed by vao
        gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
        System.out.println("	Generate VAO: " + vao.length); // we only use one vao
        gl.glBindVertexArray(vao[0]); // use handle 0

        // 2. generate vertex buffers indexed by vbo: here to store vertices and colors
        gl.glGenBuffers(vbo.length, vbo, 0);
        System.out.println("	Generate VBO: " + vbo.length); // we use two: position and color

        // 3. load vbo[0] with vertex data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
        FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoints);
        gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES,  //# of float * size of floats in bytes
                vBuf, // the vertex positions
                GL_STATIC_DRAW); // the data is static
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active vao buffer

        // 4. load vbo[1] with color data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1
        FloatBuffer cBuf = Buffers.newDirectFloatBuffer(vColors);
        gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit() * Float.BYTES,  //# of float * size of floats in bytes
                cBuf, //the vertex colors
                GL_STATIC_DRAW);
        gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer

        // 5. enable VAO with loaded VBO data
        gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
        gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
        System.out.println("	Enable corresponding vertex attributes.\n"); // we use two: position and color
    }


    /**
     * Copied from {@link JOGL1_3_VertexArray#readShaderSource(String)}
     */
    public String[] readShaderSource(String filename) { // read a shader file into an array
        Vector<String> lines = new Vector<String>(); // Vector object for storing shader program
        Scanner sc;

        try {
            sc = new Scanner(new File(filename)); //Scanner object for reading a shader program
        } catch (IOException e) {
            System.err.println("IOException reading file: " + e);
            return null;
        }
        while (sc.hasNext()) {
            lines.addElement(sc.nextLine());
        }
        String[] shaderProgram = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            shaderProgram[i] = (String) lines.elementAt(i) + "\n";
        }
        sc.close();
        return shaderProgram; // a string of shader programs
    }


    /**
     * This is where the magic happens.
     *
     * We pop a point from the stack and draw it
     * If the stack is full, we "restore" the stack to the full circle
     *
     * We should also display the full circle for "reference
     * @param drawable
     */
    @Override
    public void display(GLAutoDrawable drawable) {

        // clear the display every frame: another way to set the background color
        float bgColor[] = {0.0f, 0.0f, 0.0f, 1.0f};
        FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
        gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame
        // gl.glDrawBuffer(GL.GL_FRONT_AND_BACK); //if you want a still image

        /**
         * Check if the current stack is empty
         */
        if (currentStack.isEmpty()) {
            //copy all of the next stack
            currentStack.addAll(savedCircle);
        }

        // get our x,y pair ready to send over to draw point
        PointPair pair = currentStack.pop();

        vPoint[0] = pair.getX();
        vPoint[1] = pair.getY();

        gl.glPointSize(6.0f);
        drawPoint(vPoint, movingPointColor);

        // show all of the points
        savedCircle.forEach(pointPair ->
        {
            vPoint[0] = pointPair.getX();
            vPoint[1] = pointPair.getY();

            // change the size of the points
            gl.glPointSize(1.0f);
            drawPoint(vPoint, allPointsColor);
        });
    }


    /**
     * Copied from {@link JOGL1_4_1_Point#drawPoint(float[], float[])}
     */
    public void drawPoint(float[] vPoint, float[] vColor) {

        // 1. load vbo[0] with vertex data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
        FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoint);
        gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES,  //# of float * size of floats in bytes
                vBuf, // the vertex array
                GL_STATIC_DRAW);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

        // 2. load vbo[1] with color data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]); // use handle 1
        FloatBuffer cBuf = Buffers.newDirectFloatBuffer(vColor);
        gl.glBufferData(GL_ARRAY_BUFFER, cBuf.limit() * Float.BYTES,  //# of float * size of floats in bytes
                cBuf, //the color array
                GL_STATIC_DRAW);
        gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); // associate vbo[1] with active vao buffer

        // 3. draw a point: VAO has two arrays of corresponding vertices and colors
        gl.glDrawArrays(GL_POINTS, 0, 1);
    }


    @Override
    public void dispose(GLAutoDrawable drawable) {
        animator().stop(); // stop the animator thread
        Logger.getGlobal().fine("Animator thread stopped.");
        super.dispose(drawable);
    }

    public static void main(String[] args) {
        // some constructor starts the process
        PointMovingInCircle pointMovingInCircle = new PointMovingInCircle();
        pointMovingInCircle.start();
    }
}