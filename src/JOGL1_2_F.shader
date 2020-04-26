#version 410

out vec4 color; // out to the framebuffer

void main(void) { 

	// physical coordinates: in number of pixels corresponding to the viewport

	if (gl_FragCoord.x	<	200)  //fragment in device coordinates 
		color =	vec4(1.0,	0.0,	0.0,	1.0);	
	else
	if (gl_FragCoord.x	<	400)  //fragment in device coordinates 
		color =	vec4(0.0,	1.0,	0.0,	1.0);	
	else
	if (gl_FragCoord.x	<	600)  //fragment in device coordinates 
		color =	vec4(0.0,	0.0,	1.0,	1.0);	
	else 	
		color =	vec4(1.0,	1.0,	1.0,	1.0);	
		
		
}
