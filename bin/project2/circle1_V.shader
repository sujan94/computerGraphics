
#version 410	

uniform float theta; // input angle for rotation 

layout (location = 0) in vec3 iPosition; // VBO: vbo[0]

out vec3 color; // output to fragment shader

void	main(void)	{	
	float dt = theta/50;

	gl_Position = vec4(iPosition.x, iPosition.y, iPosition.z, 1.0);

// 	if (iPosition.x == 0 && iPosition.y == 0 && iPosition.z == 0) color = vec3(0.5, 0.5, 0.5);
// 	else color = normalize(iPosition); // avoid zero length vector

	// color specified according to the vertex position
    color =	vec3(abs(color.x), abs(color.y), abs(color.x*color.y));

}