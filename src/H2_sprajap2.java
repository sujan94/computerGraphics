import java.nio.FloatBuffer;

import javax.swing.JOptionPane;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

import static com.jogamp.opengl.GL.*;

public class H2_sprajap2 extends JOGL1_4_1_Point {
    float vPoint[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
    float vPoint2[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
    float vPoint3[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
    float vPoint4[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
    float vPoint5[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
    float vColor[] = { 1.0f, 1.0f, 1.0f };

    private static float myMatStack[][][] = new float[24][4][4]; // 24 layers for push and pop
    private static int stackPtr = 0;
    static float vdata[][] = { {1.0f, 0.0f, 0.0f}
            , {0.0f, 1.0f, 0.0f}
            , {-1.0f, 0.0f, 0.0f}
            , {0.0f, -1.0f, 0.0f}
    };
    static int cnt = 1;

    public void init(GLAutoDrawable drawable) {
        gl = (GL4) drawable.getGL();
        String vShaderSource[], fShaderSource[];

        vShaderSource = readShaderSource("srcp/H2_sprajap2_V.shader"); // read vertex shader
        fShaderSource = readShaderSource("srcp/H2_sprajap2_F.shader"); // read fragment shader
        vfPrograms = initShaders(vShaderSource, fShaderSource);

        // 1. generate vertex arrays indexed by vao
        gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
        gl.glBindVertexArray(vao[0]); // use handle 0

        // 2. generate vertex buffers indexed by vbo: here vertices and colors
        // gl.glGenBuffers(vbo.length, vbo, 0);
        gl.glGenBuffers(1, vbo, 0);

        // 3. enable VAO with loaded VBO data
        gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
//		gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: color
    }

    /**
     * Invoke this function to draw upper case characters on the frame.
     * @param c - character to draw. Supports lower case characters as well
     * @param vColor - character's color.
     * TODO add more support for other characters.
     */
    private void myCharacter(Character c, float[] vColor) {
        switch(Character.toUpperCase(c)) {
            case 'S':
                vPoint[0] = (float) (0.8);
                vPoint[1] = (float) (0.8);
                vPoint[3] = (float) (0.2);
                vPoint[4] = (float) (0.8);

                vPoint2[0] = (float) (0.2);
                vPoint2[1] = (float) (0.8);
                vPoint2[3] = (float) (0.2);
                vPoint2[4] = (float) (0.5);

                vPoint3[0] = (float) (0.2);
                vPoint3[1] = (float) (0.5);
                vPoint3[3] = (float) (0.8);
                vPoint3[4] = (float) (0.5);

                vPoint4[0] = (float) (0.8);
                vPoint4[1] = (float) (0.5);
                vPoint4[3] = (float) (0.8);
                vPoint4[4] = (float) (0.2);

                vPoint5[0] = (float) (0.8);
                vPoint5[1] = (float) (0.2);
                vPoint5[3] = (float) (0.2);
                vPoint5[4] = (float) (0.2);

                drawLineJOGL(vPoint, vColor);
                drawLineJOGL(vPoint2, vColor);
                drawLineJOGL(vPoint3, vColor);
                drawLineJOGL(vPoint4, vColor);
                drawLineJOGL(vPoint5, vColor);

                break;
            case 'P':
                vPoint[0] = (float) (2 * 0.8 - 1);
                vPoint[1] = (float) (2 * 0.8 - 1);
                vPoint[3] = (float) (2 * 0.2 - 1);
                vPoint[4] = (float) (2 * 0.8 - 1);

                vPoint2[0] = (float) (2 * 0.2 - 1);
                vPoint2[1] = (float) (2 * 0.8 - 1);
                vPoint2[3] = (float) (2 * 0.2 - 1);
                vPoint2[4] = (float) (2 * 0.2 - 1);

                vPoint3[0] = (float) (2 * 0.8 - 1);
                vPoint3[1] = (float) (2 * 0.8 - 1);
                vPoint3[3] = (float) (2 * 0.8 - 1);
                vPoint3[4] = (float) (2 * 0.5 - 1);

                vPoint4[0] = (float) (2 * 0.8 - 1);
                vPoint4[1] = (float) (2 * 0.5 - 1);
                vPoint4[3] = (float) (2 * 0.2 - 1);
                vPoint4[4] = (float) (2 * 0.5 - 1);

                drawLineJOGL(vPoint, vColor);
                drawLineJOGL(vPoint2, vColor);
                drawLineJOGL(vPoint3, vColor);
                drawLineJOGL(vPoint4, vColor);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid input. We only support 's', 'S', 'p' and 'P' at the moment.", "Unsupported Input Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                System.exit(0);
                break;
        }
    }


    public void display(GLAutoDrawable drawable) {


        myLoadIdentity();
        myCharacter('s', vColor);
        myScalef((float) cnt/200f, (float) cnt/200f, 1f);
    }

    // specify to draw a line using OpenGL
    public void drawLineJOGL(float[] vPoint, float[] vColor) {

        // 1. load vbo[0] with vertex data
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); // use handle 0
        FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPoint);
        gl.glBufferData(GL_ARRAY_BUFFER, vBuf.limit() * Float.BYTES, // # of float * size of floats in bytes
                vBuf, // the vertex array
                GL_STATIC_DRAW);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // associate vbo[0] with active VAO buffer

        // 2. send color data to vertex shader through uniform (array): color here is
        // not per-vertex
        FloatBuffer cBuf = Buffers.newDirectFloatBuffer(vColor);

        // Connect JOGL variable with shader variable by name
        int colorLoc = gl.glGetUniformLocation(vfPrograms, "vColor");
        gl.glProgramUniform3fv(vfPrograms, colorLoc, 1, cBuf);


        // 3. draw a line: VAO has one array of corresponding two vertices
        gl.glDrawArrays(GL_LINES, 0, 2);
    }





    // return the current matrix on top of the Modelview matrix stack
    public void get_Matrix(float M[]) {

        for (int i = 0; i < 4; i++ )
            for (int j = 0; j < 4; j++ )
                M[i*4+j] = myMatStack[stackPtr][j][i];
    }

    // initialize a matrix to all zeros
    private void myClearMatrix(float mat[][]) {

        for (int i = 0; i<4; i++) {
            for (int j = 0; j<4; j++) {
                mat[i][j] = 0.0f;
            }
        }
    }


    // initialize a matrix to Identity matrix
    private void myIdentity(float mat[][]) {

        myClearMatrix(mat);
        for (int i = 0; i<4; i++) {
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

        for (int i = 0; i<4; i++) {
            for (int j = 0; j<4; j++) {
                for (int k = 0; k<4; k++) {
                    matTmp[i][j] +=
                            myMatStack[stackPtr][i][k]*mat[k][j];
                }
            }
        }
        // save the result on the current matrix
        for (int i = 0; i<4; i++) {
            for (int j = 0; j<4; j++) {
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
        x = x/(float) Math.sqrt(x*x + y*y + z*z);
        y = y/(float) Math.sqrt(x*x + y*y + z*z);
        z = z/(float) Math.sqrt(x*x + y*y + z*z);

        float c = (float)Math.cos(angle); // gradian
        float s = (float)Math.sin(angle);

        myIdentity(R);

        R[0][0] = x*x*(1-c) + c;	 R[0][1] = x*y*(1-c) - z*s;		R[0][2] = x*z*(1-c) + y*s;
        R[1][0] = y*x*(1-c) + z*s;	 R[1][1] = y*y*(1-c) + c;		R[1][2] = y*z*(1-c) - x*s;
        R[2][0] = z*x*(1-c) - y*s;	 R[2][1] = z*y*(1-c) + x*s;		R[2][2] = z*z*(1-c) + c;

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

    public static void main(String[] args) {
        new H2_sprajap2();

    }
}
