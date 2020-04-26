package project2;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

/**
 * todo - The result animation is like HW1_4_font in joglExamples2013
 * <pre>
 * You should implement the following:
 *
 * 1. Draw a circle and multiple points bouncing in a circle
 *  ◦ Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
 *  ◦ draw points bouncing in the circle by sending them as an array in VBO; you should use random colors for the points;
 * 2. Draw a pentagon that rotates on the circle, with your initials moving with the vertices
 *  ◦ In addition to the circle and points bouncing, you draw a pentagon with colored vertices.
 *    You draw a pentagon by send line vertices as an array in VBO to the vertex shader at the same time.
 *    Then you draw the vertices with a different color. The animation is achieved by rotation around the z axis.
 *  ◦ Scale and translate your initials to the vertices; you need to transform and display each character serially
 *    because your function draws one character at a time;
 *       ▪ If the characters are rotated with the pentagon, they should stay vertical;
 * 3. Draw your initials around the center and rotate in the opposite direction as an animation
 *  ◦ Scale, translate, and rotate each of your initials
 *
 * </pre>
 */
public class ProjectTransformation extends JOGL1_2_Animate {

    // vertex array object (handle), for sending to the vertex shader
    int[] vao = new int[1];

    // vertex buffers objects (handles) to stores position, color, normal, etc
    int[] vbo = new int[2];

    // handle to shader programs
    int vfPrograms;

    private final float[] allPointsColor;

    private final float[] circlePoints;
    double currentAngle = 0;

    double radius = 0.5;

    int numberOfPoints = 720;

    double angleIncrement = 2 * Math.PI / numberOfPoints;

    float theta = 0.5f;

    int cx = WIDTH / 2;

    int cy = HEIGHT / 2;
    private double r = 0.5;

    public ProjectTransformation() {
        super();
        // all white circle
        allPointsColor = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        circlePoints = getCirclePoints();
    }


    /**
     * Puts the circle to the current stack
     * From the book
     * As an example, a simple 2D circle equation with radius (r) and centered at (cx, cy)
     * can be expressed in parametric function as:
     * x = r * cos(theta) + cx
     * y = r * sin(theta) + cy
     * r is the radius of the circle
     * cx - center x
     * cy - center y
     * if nothing is give, it is effectively 0,0
     */
    private float[] getCirclePoints() {
        double currentAngle;
        // I could do theta = theta + 1/r;
        float[] circlePoints = new float[3 * numberOfPoints]; // predefined number of pixels on the line

        for (int i = 0; i < numberOfPoints; i++) {
            currentAngle = i * angleIncrement;
            float x = (float) (radius * Math.cos(currentAngle));
            float y = (float) (radius * Math.sin(currentAngle));

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
        // change the size of the points
        circleOfPoints();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        pentagon();

        // drawInitial();
    }

    private float[] getPointForPentagon() {

        int limit = 5;
        float angleIncrement = (360f / 5) + theta;
        // I could do theta = theta + 1/r;
        float[] pentagonPoints = new float[3 * limit]; // predefined number of pixels on the line
        int i = 0;
        while (i < pentagonPoints.length) {
            currentAngle = currentAngle + angleIncrement;
            System.out.println(currentAngle + " " + currentAngle % 360);
            float x = (float) (radius * Math.cos(Math.toRadians(currentAngle % 360)));
            float y = (float) (radius * Math.sin(Math.toRadians(currentAngle % 360)));

            // write a pixel into the framebuffer, here we write into an array
            pentagonPoints[(i++)] = x; // normalize -1 to 1
            pentagonPoints[i++] = y; // normalize -1 to 1
            pentagonPoints[i++] = 0.0f;
        }
        return pentagonPoints;
    }

    public void pentagon() {
        float[] pentagonPoints = getPointForPentagon();

        float[] color = new float[15];
        for (int i = 0; i < color.length; i++) {
            color[i] = (float) Math.random();
        }

        drawPointArray(pentagonPoints, color, GL_LINE_LOOP);
    }


    /**
     * 1. Draw a circle and multiple points bouncing in a circle
     * ◦ Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
     * ◦ draw points bouncing in the circle by sending them as an array in VBO; you should use random colors for the points;
     */
    void circleOfPoints() {
        // drawing the circle by passing the array to glDrawArray
        drawPointArray(circlePoints, allPointsColor, GL_LINE_LOOP);
        gl.glPointSize(2.0f);
        drawBouncingPoint();
    }


    /**
     * 1. Draw a circle and multiple points bouncing in a circle
     * ◦ Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
     * ◦ draw points bouncing in the circle by sending them as an array in VBO; you should use random colors for the points;
     */
    void drawBouncingPoint() {
        int numberOfPoints = 20;
        float[] pointsInCircle = new float[3 * numberOfPoints];
        float[] color = new float[3];
        color[0] = (float) Math.random();
        color[1] = (float) Math.random();
        color[2] = (float) Math.random();

        int i = 0;
        while (i < numberOfPoints) {
            // randomly pick a theta, in radians
            double angle = (2 * Math.random() - 1) * (2 * Math.PI);
            double random_radius = ((2 * Math.random() - 1) * radius);

            float x = (float) (random_radius * Math.cos(angle));
            float y = (float) (random_radius * Math.sin(angle));

            pointsInCircle[(i * 3)] = x; // normalize -1 to 1
            pointsInCircle[(i * 3) + 1] = y; // normalize -1 to 1
            pointsInCircle[(i * 3) + 2] = 0.0f;
            i++;
        }
        drawPointArray(pointsInCircle, color, GL_POINTS);
    }

    /**
     * Send circle points as an array in VBO to the vertex shader at the same time, and call glDrawArray;
     */
    public void drawPointArray(float[] vPoint, float[] colorArray, int mode) {
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
        int colorLoc = gl.glGetUniformLocation(vfPrograms, "vColor");
        gl.glProgramUniform3fv(vfPrograms, colorLoc, 1, cBuf);

        // 6. draw points: VAO has 1 array of corresponding vertices
        gl.glDrawArrays(mode, 0, (vBuf.limit() / 3));
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

        // 5. enable VAO with loaded VBO data
        gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
        gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
        System.out.println("	Enable corresponding vertex attributes.\n"); // we use two: position and color
    }


    public static void main(String[] args) {
        ProjectTransformation projectTransformation = new ProjectTransformation();
        projectTransformation.setTitle("Transformation");
        projectTransformation.setSize(WIDTH, HEIGHT);
        projectTransformation.setVisible(true);
    }
}
