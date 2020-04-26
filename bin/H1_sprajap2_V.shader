#version 410

uniform float xPos; // value from JOGL main program
uniform float yPos; // value from JOGL main program
uniform mat4 mv_matrix; // current matrix from the JOGL program

void	main(void)	{	

	gl_Position = mv_matrix*vec4(xPos, yPos, 0.0, 1.0);
	
}