import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;

/**
 * Draw a point that moves on a circle or bounces in a circle.
 * You should have a file name: H1_pdhimal.java.
 * If you have shader files, they are H1_pdhimal_V.shader and H1_pdhimal_F.shader.
 * <p>
 * Vertex shader and fragment shader
 * <p>
 * Extend the code he provide, don't modify his code
 * <p>
 * Do we need more shader files? probably not
 *
 * <p>
 * <p>
 * Inherit and override
 */
public class PointCircle extends JOGL1_1_PointVFfiles {

    FPSAnimator animator; // for thread that calls display() repetitively
    int vfPrograms; // handle to shader programs
    float delta = 0.005f;
    float pos = 0.0f;

    public PointCircle() {
        // initialize the animator with frames per second
        animator = new FPSAnimator(canvas, 60);
    }

    FPSAnimator animator() {
        return animator;
    }


    void start() {
        animator().start();
    }

    /**
     * using some sort of shader code.
     *
     * @param drawable
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        gl = (GL4) drawable.getGL();
        String[] vShaderSource;
        String[] fShaderSource;

        vShaderSource = readShaderSource("src/JOGL1_2_V.shader"); // read vertex shader
        fShaderSource = readShaderSource("src/JOGL1_2_F.shader"); // read fragment shader

        vfPrograms = initShaders(vShaderSource, fShaderSource);
    }


    @Override
    public void display(GLAutoDrawable drawable) { // overwrite super's display
        // clear the display every frame: another way to set the background color
        float[] bgColor = {0.0f, 0.0f, 0.0f, 1.0f};
        FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
        gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame
        gl.glDrawBuffer(GL.GL_FRONT_AND_BACK); //if you want a still image

        // pos goes from -1 to 1 back and forth
        pos += delta;
        if (pos <= -1.0f) delta = 0.005f;
        else if (pos >= 1.0f) delta = -0.005f;

        //Connect JOGL variable with shader variable by name
        int posLoc = gl.glGetUniformLocation(vfPrograms, "sPos");
        gl.glProgramUniform1f(vfPrograms, posLoc, pos);


        // This is the lower left quad
        gl.glViewport(0, 0, 400, 400); // physical coordinates: number in pixels
        gl.glPointSize(4.0f);
        gl.glDrawArrays(GL_POINTS, 0, 1);

        // This is the upper-right quad
        gl.glViewport(400, 400, 800, 800);
        gl.glDrawArrays(GL_POINTS, 0, 1);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        animator().stop(); // stop the animator thread
        Logger.getGlobal().fine("Animator thread stopped.");
        super.dispose(drawable);
    }

    public static void main(String[] args) {
        PointCircle pointCircle = new PointCircle();
        pointCircle.start();
    }
}
