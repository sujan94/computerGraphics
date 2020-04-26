#version 410

in  vec3 color; // (interpolated) value from vertex shader
out vec4 fColor; // out to display


void main(void) { 

	 fColor = vec4(color, 1.0);  
	 
	if (gl_FragCoord.x	<	251)  //fragment in device coordinates // WIDTH/4
		fColor =	vec4(0.2,	0.2,	0.2,	1.0);	
	else
	if (gl_FragCoord.x	>	750)  //fragment in device coordinates //3*WIDTH/4
		fColor =	vec4(0.2,	0.2,	0.2,	1.0);	

	if (gl_FragCoord.y	<	251)  //fragment in device coordinates 
		fColor =	vec4(0.2,	0.2,	0.2,	1.0);	
	else 	
	if (gl_FragCoord.y	>	750)  //fragment in device coordinates 
		fColor =	vec4(0.2,	0.2,	0.2,	1.0);	
		
}
