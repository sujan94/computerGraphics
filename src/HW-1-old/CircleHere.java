import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
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
public class CircleHere extends JOGL1_1_PointVFfiles {

    FPSAnimator animator; // for thread that calls display() repetitively
    int vfPrograms; // handle to shader programs
    float delta = 0.005f;
    float pos = 0.0f;

    public CircleHere() {
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

        // vfPrograms = initShaders(vShaderSource, fShaderSource);
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        int numVertices = 20;
        double radius = 0.5;

        // clear the window
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // gl.glColor3f(0, 0, 0); //set pen color to black
        // approximate  a circle with a polygon
        gl.glBegin(GL2.GL_POLYGON);
        // gl.glBegin(GL2.GL_TRIANGLE_FAN);
        {
            double angle;
            double angleIncrement = 2 * Math.PI / numVertices;
            for (int i = 0; i < numVertices; i++) {
                angle = i * angleIncrement;
                double x = radius * Math.cos(angle);
                double y = radius * Math.sin(angle);
                gl.glVertex2d(x, y);
            }
        }
        gl.glEnd();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        animator().stop(); // stop the animator thread
        Logger.getGlobal().fine("Animator thread stopped.");
        super.dispose(drawable);
    }

    public static void main(String[] args) {
        CircleHere circleHere = new CircleHere();
        circleHere.start();
    }
}
