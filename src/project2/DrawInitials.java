package project2;

import static com.jogamp.opengl.GL2ES3.GL_COLOR;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * {@link DrawInitials} class to draw my initials (PD) using lines in JOGL
 *
 * @author Prakash Dhimal
 *         George Mason University
 *         Computer Graphics (CS 551)
 *         Spring 2020
 *         Homework 2
 */
class DrawInitials extends JOGL1_4_3_Line
{
    public DrawInitials()
    {
        // Super class starts the animator
    }


    /**
     * Draws the Letters P D
     *
     * Uses 6 lines to draw P and six lines to draw D
     * Used 2 lines on each character to make the curve
     * The characters are Capital Letter P and D
     */
    @Override
    public void display(GLAutoDrawable drawable)
    {
        // Clear the frame, so that we can see the color changing (Animation)
        float[] bgColor = { 0.0f, 0.0f, 0.0f, 1.0f };
        FloatBuffer bgColorBuffer = Buffers.newDirectFloatBuffer(bgColor);
        gl.glClearBufferfv(GL_COLOR, 0, bgColorBuffer); // clear every frame

        /**
         * Make the color random
         */
        vColor[0] = (float) Math.random();
        vColor[1] = (float) Math.random();
        vColor[2] = (float) Math.random();

        float offset_x = 0;
        float offset_y = 0;

        // Uncomment to move the letters around
        // offset_x = (float) (2 * Math.random() - 1);
        // offset_y = (float) (2 * Math.random() - 1);

        // Hard-code the lines to show my initials
        /**
         * For all of the lines
         */
        float distance_X = 300;
        float north_Y = 200;
        float south_Y = -300;

        /**
         * For P
         */
        float east_P_X = -400;
        float west_P_X = east_P_X + distance_X;
        // not doing zero to avoid NaN
        float mid_P_Y = -50;

        float curve_P_X = 30;
        float far_west_P_X = west_P_X + curve_P_X;
        float curve_P_Y = 40;
        float north_curve_P_Y = north_Y - curve_P_Y;
        float mid_curve_P_Y = mid_P_Y + curve_P_Y;

        // | in P + offset
        vPoint[0] = east_P_X / WIDTH + offset_x;
        vPoint[1] = north_Y / HEIGHT + offset_y;
        vPoint[3] = east_P_X / WIDTH + offset_x;
        vPoint[4] = south_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // other end of P
        vPoint[0] = far_west_P_X / WIDTH + offset_x;
        vPoint[1] = north_curve_P_Y / HEIGHT + offset_y;
        vPoint[3] = far_west_P_X / WIDTH + offset_x;
        vPoint[4] = mid_curve_P_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // top line of P
        vPoint[0] = east_P_X / WIDTH + offset_x;
        vPoint[1] = north_Y / HEIGHT + offset_y;
        vPoint[3] = west_P_X / WIDTH + offset_x;
        vPoint[4] = north_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // bottom line of P
        vPoint[0] = east_P_X / WIDTH + offset_x;
        vPoint[1] = mid_P_Y / HEIGHT + offset_y;
        vPoint[3] = west_P_X / WIDTH + offset_x;
        vPoint[4] = mid_P_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // top curve of P
        vPoint[0] = west_P_X / WIDTH + offset_x;
        vPoint[1] = north_Y / HEIGHT + offset_y;
        vPoint[3] = far_west_P_X / WIDTH + offset_x;
        vPoint[4] = north_curve_P_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // bottom curve
        vPoint[0] = west_P_X / WIDTH + offset_x;
        vPoint[1] = mid_P_Y / HEIGHT + offset_y;
        vPoint[3] = far_west_P_X / WIDTH + offset_x;
        vPoint[4] = mid_curve_P_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        /**
         * For D
         *
         * Made using six lines
         */
        float east_D_X = 50;
        float west_D_X = east_D_X + distance_X;
        float curve_D_X = 50;
        float far_west_D_X = west_D_X + curve_D_X;
        float curve_D_Y = 70;
        float north_curve_D_Y = north_Y - curve_D_Y;
        float south_curve_D_Y = south_Y + curve_D_Y;

        // | in D
        vPoint[0] = east_D_X / WIDTH + offset_x;
        vPoint[1] = north_Y / HEIGHT + offset_y;
        vPoint[3] = east_D_X / WIDTH + offset_x;
        vPoint[4] = south_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // west in D
        vPoint[0] = far_west_D_X / WIDTH + offset_x;
        vPoint[1] = north_curve_D_Y / HEIGHT + offset_y;
        vPoint[3] = far_west_D_X / WIDTH + offset_x;
        vPoint[4] = south_curve_D_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // top line in D
        vPoint[0] = east_D_X / WIDTH + offset_x;
        vPoint[1] = north_Y / HEIGHT + offset_y;
        vPoint[3] = west_D_X / WIDTH + offset_x;
        vPoint[4] = north_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // bottom line in D
        vPoint[0] = east_D_X / WIDTH + offset_x;
        vPoint[1] = south_Y / HEIGHT + offset_y;
        vPoint[3] = west_D_X / WIDTH + offset_x;
        vPoint[4] = south_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // top curve
        vPoint[0] = west_D_X / WIDTH + offset_x;
        vPoint[1] = north_Y / HEIGHT + offset_y;
        vPoint[3] = far_west_D_X / WIDTH + offset_x;
        vPoint[4] = north_curve_D_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // bottom curve
        vPoint[0] = west_D_X / WIDTH + offset_x;
        vPoint[1] = south_Y / HEIGHT + offset_y;
        vPoint[3] = far_west_D_X / WIDTH + offset_x;
        vPoint[4] = south_curve_D_Y / HEIGHT + offset_y;
        drawLineJOGL(vPoint, vColor);

        // slow down to show the animation
        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            Logger.getGlobal().severe(e.getMessage());
        }
    }


    public static void main(String[] args)
    {
        new DrawInitials();
    }

}