#version 410

uniform float sPos;

layout (location = 0) in vec3 iPosition; // VBO: vbo[0]
layout (location = 1) in vec3 iColor;    // VBO: vbo[1]

void	main(void)	{
	//gl_Position = vec4(sPos, sPos, 0.0, 1.0);

	gl_Position = vec4(iPosition.x, iPosition.y + sPos, 0.0, 1.0);

	
}