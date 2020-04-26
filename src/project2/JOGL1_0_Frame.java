package project2;

/*************************************************
 * Created on August 1, 2017, @author: Jim X. Chen
 *
 * JOGL initial set up: 
 * Open a frame with a background color: Cyan
 *
 *
 */

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.jogamp.opengl.GL4.GL_COLOR_BUFFER_BIT;


public class JOGL1_0_Frame extends Frame implements GLEventListener {
	public static GLCanvas canvas; // drawable in a frame
	public static GL4 gl; // handle to OpenGL functions
	public static int WIDTH = 800, HEIGHT = 800; // used to set the window size

	public JOGL1_0_Frame() { // constructor that runs at instantiation

		GLProfile glp = GLProfile.getMaxProgrammableCore(true);
		GLCapabilities caps = new GLCapabilities(glp);
		// 1. specify a drawable: canvas
		canvas = new GLCanvas(caps);
		System.out.println("Hello, JOGL!\n");

		// 2. listen to the events related to canvas: init, reshape, display, and dispose
		canvas.addGLEventListener(this); // "this" is the current instantiated object in main()
		this.add(canvas,0);

		// 3. set the size of the frame and make it visible
		setSize(WIDTH, HEIGHT);
		setVisible(true);

		System.out.println("Select 'Run->Run Configurations', 'Arguments'; add the following lines under 'VM arguments': ");
		System.out.println("-Djogamp.gluegen.UseTempJarCache=false");

		// 4. external window destroy event: dispose resources before exit
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose(); // Notifies the listener to perform the release of all OpenGL resources.
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		// 0. starts by instantiating a class, which calls the constructor
		new JOGL1_0_Frame();
		
		// constructor: 1, 2, 3, 4
		// 5. after the instantiation, the system gets into the event loop: init, reshape, display, and dispose
	}

	// called once for OpenGL initialization
	public void init(GLAutoDrawable drawable) {
		System.out.println("\na) init is called once for initialization.");

		// 6. interface to OpenGL functions
		gl = (GL4) drawable.getGL();
		
		// 7. specify background color
	    gl.glClearColor(0.0f, 1.0f, 1.0f, 1.0f); // Cyan
	}

	// called for handling drawing area when it is reshaped
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		
		System.out.println("b) reshape is called whenever the frame is resized.");
	}

	// called for OpenGL rendering for every reshaping
	public void display(GLAutoDrawable drawable) {
		System.out.println("c) display is called for every reshape.");

		// 8. clear the back-buffer into the background color
		gl.glClear(GL_COLOR_BUFFER_BIT);
	}

	// For releasing of all OpenGL resources related to a drawable manually called
	// at the end 
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("d) dispose is called before exiting.");
	}
}
