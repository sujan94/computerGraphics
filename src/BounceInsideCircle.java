import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

public class BounceInsideCircle extends JOGL1_1_PointVFfiles {
    private static final float RADIUS = 0.3f;
    FPSAnimator animator; // for thread that calls display() repetitively
    int vfPrograms; // handle to shader programs
    float delta=0.005f, pos=RADIUS, posy = 0.0f;
    boolean isClockWiseDirection = false;

    public BounceInsideCircle() { // it calls supers constructor first

        // Frame per second animator
        animator = new FPSAnimator(canvas, 40); // 40 calls per second; frame rate
        animator.start();
        System.out.println("A thread that calls display() repeatitively.");
    }

    public void display(GLAutoDrawable drawable) {

        // clear the display every frame: another way to set the background color
        float bgColor[] = { 0.0f, 0.0f, 0.0f, 1.0f };
//        FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
//        gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

        gl.glDrawBuffer(GL.GL_FRONT_AND_BACK); //if you want a still image



        //Connect JOGL variable with shader variable by name
        int posLoc = gl.glGetUniformLocation(vfPrograms,  "sPos");
        gl.glProgramUniform1f(vfPrograms,  posLoc,  pos);

        //Connect JOGL variable with shader variable by name
        int posLocy = gl.glGetUniformLocation(vfPrograms,  "sPosy");
        gl.glProgramUniform1f(vfPrograms,  posLocy,  posy);


        // This is the lower left quad
        gl.glViewport(0, 0, 400, 400); // physical coordinates: number in pixels
        gl.glPointSize(4.0f);
        gl.glDrawArrays(GL_POINTS, 0, 1);

        // This is the upper-right quad
        gl.glViewport(400, 400, 800, 800);
        gl.glDrawArrays(GL_POINTS, 0, 1);
    }


    public void init(GLAutoDrawable drawable) { // reading new vertex & fragment shaders
        gl = (GL4) drawable.getGL();
        String vShaderSource[], fShaderSource[] ;

        vShaderSource = readShaderSource("srcp/JOGL1_2_V.shader"); // read vertex shader
        fShaderSource = readShaderSource("srcp/JOGL1_2_F.shader"); // read fragment shader

        vfPrograms = initShaders(vShaderSource, fShaderSource);
    }


    public void dispose(GLAutoDrawable drawable) {
        animator.stop(); // stop the animator thread
        System.out.println("Animator thread stopped.");
        super.dispose(drawable);
    }

    public static void main(String[] args) {
        new BounceInsideCircle();
    }
}