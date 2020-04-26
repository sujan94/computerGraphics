#version 410

uniform float sPos;

layout (location = 0) in vec3 iPosition; // VBO: vbo[0]
layout (location = 1) in vec3 vColor;    // VBO: vbo[1]
uniform mat4 mv_matrix; // current matrix from the JOGL program

out vec3 color; // output to fragment shader

void	main(void)	{
	//gl_Position = vec4(sPos, sPos, 0.0, 1.0);

	gl_Position = mv_matrix*vec4(iPosition.x, iPosition.y, iPosition.z, 1.0);
	color = vColor;

}