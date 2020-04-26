/*
 * Created on August 2019
 * @author Jim X. Chen: transformation: OpenGL style implementation
 * 
 * Demonstrate myPerspective
 * 
 */

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES2.GL_COMPARE_REF_TO_TEXTURE;
import static com.jogamp.opengl.GL2ES2.GL_DEPTH_COMPONENT;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_TEXTURE_COMPARE_FUNC;
import static com.jogamp.opengl.GL2ES2.GL_TEXTURE_COMPARE_MODE;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;

import java.io.File;
import java.nio.FloatBuffer;


public class JOGL4_11_ShadowMapping extends JOGL4_9_Bumpmapping {
	
	static int vfProgram1; // handle to shader programs
	static int vfProgram2; // handle to shader programs
	private int [] shadowTex = new int[1];
	private int [] shadowBuffer = new int[1];

	


	
	  public void display(GLAutoDrawable glDrawable) {

	  // First pass: 
		  // 1. at the end of init, build a framebuffer called shadowBuffer, build a texture to hold the shadowBuffer values
		  // 2. attach the texture to the shadowBuffer, so it corresponds to the Depth buffer 
			gl.glBindFramebuffer(GL_FRAMEBUFFER, shadowBuffer[0]);
			gl.glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, shadowTex[0], 0);
			
			// make sure the attachment is complete
			if(gl.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
				 System.out.println("The depth frame buffer is not ready!"); // false;
			
			// we don't draw into color buffers
			gl.glDrawBuffer(GL_NONE);
			gl.glEnable(GL_DEPTH_TEST);
			gl.glDepthFunc(GL_LEQUAL);
			gl.glClear(GL_DEPTH_BUFFER_BIT);
			gl.glEnable(GL_POLYGON_OFFSET_FILL);	// for reducing
			gl.glPolygonOffset(2.0f, 4.0f);			//  shadow artifacts
					  		  
		  // 3. view from light source's point of view
			vfPrograms = vfProgram1; 
			gl.glUseProgram(vfPrograms);

			// need to reset projection, because we use need to use the corresponding shader program here
			myLoadIdentity();		    
		    myFrustum(-WIDTH/4,HEIGHT/4,-WIDTH/4,HEIGHT/4,WIDTH/4,4*WIDTH);

			  //float V_position[] = {0, 0, 2*WIDTH, 1}; // View position
		    
		    L_position[0] = 0;
		    L_position[1] = -1.5f*WIDTH;
		    L_position[2] = 0;
		    
		    // lights spource's point of view 
		    myLookAt(L_position[0], L_position[1], L_position[2], 0, 0, 0, 1, 1, 1); 
			float MVL[] = new float [16];			  
			getMatrix(MVL); // get the modelview matrix from the matrix stack
			int mvlLoc = gl.glGetUniformLocation(vfPrograms,  "light_matrix"); 
			gl.glProgramUniformMatrix4fv(vfPrograms, mvlLoc,  1,  false,  MVL, 0);

			myLoadIdentity(); 
			
			super.display(glDrawable);
		    
			gl.glDisable(GL_POLYGON_OFFSET_FILL);	// artifact reduction, continued			
			
		  // Second pass: 
		  // 1. deal with vertex then fragment from light's point of view
		  // 2. decide whether the pixel is in the shadow		  
			
				gl.glUseProgram(vfProgram2); // loads them onto the GPU hardware
				vfPrograms = vfProgram2; 

				// this is to restore to default system buffer
				gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
				gl.glDrawBuffer(GL_BACK);

				// this is the precious depth image from the light's point of view 
				gl.glActiveTexture(GL_TEXTURE4);
				gl.glBindTexture(GL_TEXTURE_2D, shadowTex[0]);
							  				
				// Send the same light's point of view matrix to the new vertex shader
				mvlLoc = gl.glGetUniformLocation(vfPrograms,  "light_matrix"); 
				gl.glProgramUniformMatrix4fv(vfPrograms, mvlLoc,  1,  false,  MVL, 0);
				
				myLoadIdentity();		    
			    myFrustum(-WIDTH/4,HEIGHT/4,-WIDTH/4,HEIGHT/4,WIDTH/4,4*WIDTH);
			    // we need to separate this translate from Model_view matrix, to be consistent with the light_matrix
			    myTranslatef(0, -WIDTH/4, -1.5f*WIDTH); // moving the viewing volume 
		  	    float PROJ[] = new float [16];
				getMatrix(PROJ); 				
				// connect the PROJECTION matrix to the vertex shader
				int projLoc = gl.glGetUniformLocation(vfPrograms,  "proj_matrix"); 
				gl.glProgramUniformMatrix4fv(vfPrograms, projLoc,  1,  false,  PROJ, 0);
				
				// at this point, the lookat is just an additional transformation
				myLoadIdentity();			    
			    super.display(glDrawable);
	  }

	  
	public void init(GLAutoDrawable drawable) {
		
		
		gl = (GL4) drawable.getGL();
		String vShaderSource1[], vShaderSource2[], fShaderSource1[], fShaderSource2[] ;
					
		vShaderSource1 = readShaderSource("src/JOGL4_11_V1.shader"); // read vertex shader
		vShaderSource2 = readShaderSource("src/JOGL4_11_V2.shader"); // read fragment shader
		fShaderSource1 = readShaderSource("src/JOGL4_11_F1.shader"); // read fragment shader
		fShaderSource2 = readShaderSource("src/JOGL4_11_F2.shader"); // read fragment shader

		vfProgram1 = initShader1(vShaderSource1);		
		vfProgram2 = initShaders(vShaderSource2, fShaderSource2);		

		// 1. generate vertex arrays indexed by vao
		gl.glGenVertexArrays(vao.length, vao, 0); // vao stores the handles, starting position 0
		gl.glBindVertexArray(vao[0]); // use handle 0
		
		// 2. generate vertex buffers indexed by vbo: here vertices and colors
		gl.glGenBuffers(vbo.length, vbo, 0);
		
		// 3. enable VAO with loaded VBO data
		gl.glEnableVertexAttribArray(0); // enable the 0th vertex attribute: position
		
		// if you don't use it, you should not enable it
		gl.glEnableVertexAttribArray(1); // enable the 1th vertex attribute: Normal
					
		// if you don't use it, you should not enable it
		gl.glEnableVertexAttribArray(2); // enable the 2th vertex attribute: TextureCoord
					
		//4. specify drawing into only the back_buffer
		gl.glDrawBuffer(GL_BACK); 
		
		// 5. Enable zbuffer and clear framebuffer and zbuffer
		gl.glEnable(GL_DEPTH_TEST); 

		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// read an image as texture
		Texture	joglTexture	=	loadTexture("src/gmu.jpg");						
		gmuTexture	=	joglTexture.getTextureObject(); 
		joglTexture	=	loadTexture("src/VSE.jpg");						
		vseTexture	=	joglTexture.getTextureObject(); 
		joglTexture	=	loadTexture("src/earthCube.jpg");						
		cubeTexture	=	joglTexture.getTextureObject(); 
		joglTexture	=	loadTexture("src/natureCube.jpg");						
		cube1Texture	=	joglTexture.getTextureObject(); 
		joglTexture	=	loadTexture("src/flowerBump.jpg");						
		bumpTexture	=	joglTexture.getTextureObject(); 
		joglTexture	=	loadTexture("src/randomBump.jpg");						
		bump1Texture	=	joglTexture.getTextureObject(); 

		// activate texture #0 and bind it to the texture object
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D,	gmuTexture);
		gl.glActiveTexture(GL_TEXTURE1);
		gl.glBindTexture(GL_TEXTURE_2D,	vseTexture);
		gl.glActiveTexture(GL_TEXTURE2);
		gl.glBindTexture(GL_TEXTURE_2D,	cubeTexture);
		gl.glActiveTexture(GL_TEXTURE3);
		gl.glBindTexture(GL_TEXTURE_2D,	bumpTexture);
				

		// 1. this is to say we are creating our own buffer: for shadow
	    gl.glGenFramebuffers(1, shadowBuffer, 0);
		gl.glBindFramebuffer(GL_FRAMEBUFFER, shadowBuffer[0]);
		
		// 2. We create an empty texture holder, to be associated with shadowBuffer
		// later, instead of writing the depth value into the system default, it will write into this texture
		gl.glGenTextures(1, shadowTex, 0);
		gl.glActiveTexture(GL_TEXTURE4);
		gl.glBindTexture(GL_TEXTURE_2D, shadowTex[0]); 
		// the empty texture corresponds to the viewport area, and we are saving depth components (32 bits)
		// as I understand it, it should save RGB the same depth value
		gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32,
						WIDTH, HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, null);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		// For textureProj so the system will return between (0, 1), in the shadow or not 
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL); 
		// (r<=Dt? 0 : 1), r is tc, and Dt is depth texture value

	}
	
	public int initShaders(String vShaderSource[], String fShaderSource[]) {

		// 1. create, load, and compile vertex shader
		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		gl.glShaderSource(vShader, vShaderSource.length, vShaderSource, null, 0);
		gl.glCompileShader(vShader);

		// 2. create, load, and compile fragment shader
		int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);
		gl.glShaderSource(fShader, fShaderSource.length, fShaderSource, null, 0);
		gl.glCompileShader(fShader);

		// 3. attach the shader programs
		int vfProgram = gl.glCreateProgram(); // for attaching v & f shaders
		gl.glAttachShader(vfProgram, vShader);
		gl.glAttachShader(vfProgram, fShader);

		// 4. link the program
		gl.glLinkProgram(vfProgram); // successful linking --ready for using

		//#### because we switch between multiple shaders, we cannot mark them for deletion
		
		//gl.glDeleteShader(vShader); // attached shader object will be flagged for deletion until 
									// it is no longer attached
		//gl.glDeleteShader(fShader);

		// 5. Use the program
		gl.glUseProgram(vfProgram); // loads them onto the GPU hardware
		//gl.glDeleteProgram(vfProgram); // in-use program object will be flagged for deletion until 
										// it is no longer in-use

		return vfProgram;
	}

	public int initShader1(String vShaderSource[]) {

		// 1. create, load, and compile vertex shader
		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
		gl.glShaderSource(vShader, vShaderSource.length, vShaderSource, null, 0);
		gl.glCompileShader(vShader);


		// 3. attach the shader programs
		int vfProgram = gl.glCreateProgram(); // for attaching v & f shaders
		gl.glAttachShader(vfProgram, vShader);

		// 4. link the program
		gl.glLinkProgram(vfProgram); // successful linking --ready for using

		//#### because we switch between multiple shaders, we cannot mark them for deletion
		
		//gl.glDeleteShader(vShader); // attached shader object will be flagged for deletion until 
									// it is no longer attached
		//gl.glDeleteShader(fShader);

		// 5. Use the program
		gl.glUseProgram(vfProgram); // loads them onto the GPU hardware
		//gl.glDeleteProgram(vfProgram); // in-use program object will be flagged for deletion until 
										// it is no longer in-use

		return vfProgram;
	}

	
	  public void myLookAt(
		      double eX, double eY, double eZ,
		      double cX, double cY, double cZ,
		      double upX, double upY, double upZ) {
		    //eye and center are points, but up is a vector

		    //1. change center into a vector:
		    // glTranslated(-eX, -eY, -eZ);
		    cX = cX-eX;
		    cY = cY-eY;
		    cZ = cZ-eZ;

		    //2. The angle of center on xz plane and x axis
		    // i.e. angle to rot so center in the neg. yz plane
		    double a = Math.atan(cZ/cX);
		    if (cX==0) a = 0; 
		    else 
		    if (cX>=0) {
		      a = a+Math.PI/2;
		    } else {
		      a = a-Math.PI/2;
		    }

		    //3. The angle between the center and y axis
		    // i.e. angle to rot so center in the negative z axis
		    double b = Math.acos(cY/Math.sqrt(cX*cX+cY*cY+cZ*cZ));
		    b = b-Math.PI/2;

		    //4. up rotate around y axis (a) radians
		    double upx = upX*Math.cos(a)+upZ*Math.sin(a);
		    double upz = -upX*Math.sin(a)+upZ*Math.cos(a);
		    upX = upx;
		    upZ = upz;

		    //5. up rotate around x axis (b) radians
		    double upy = upY*Math.cos(b)-upZ*Math.sin(b);
		    upz = upY*Math.sin(b)+upZ*Math.cos(b);
		    upY = upy;
		    upZ = upz;

		    double c = Math.atan(upX/upY);
		    if (upY<0) {
		      //6. the angle between up on xy plane and y axis
		      c = c+Math.PI;
		    }
		    myRotatef((float) c, 0, 0, 1);
		    // up in yz plane
		    myRotatef((float) b, 1, 0, 0);
		    // center in negative z axis
		    myRotatef((float) a, 0, 1, 0);
		    //center in yz plane
		    myTranslatef((float)-eX, (float)-eY, (float)-eZ);
		    //eye at the origin
		  }

	  public static void main(String[] args) {
	    new JOGL4_11_ShadowMapping();
	  }

}
