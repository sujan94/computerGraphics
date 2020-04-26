#version 410

uniform float sPos; // value from JOGL main program

uniform float sPosy; // value from JOGL main program


void	main(void)	{	

	gl_Position = vec4(sPos, sPosy, 0.0, 1.0);	
	
}