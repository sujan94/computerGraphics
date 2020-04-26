
#version 410

uniform mat4 proj_matrix; 
uniform mat4 mv_matrix; 

uniform vec4 Me; // emission 
uniform vec4 La; // light source ambient  
uniform vec4 Ma; // material ambient

layout (location = 0) in vec3 iPosition; // VBO: vbo[0]

out vec4 color; // output to fragment shader

void	main(void)	{	
	vec3 vColor; // vertex color

	
	gl_Position = proj_matrix*mv_matrix*vec4(iPosition.x, iPosition.y, iPosition.z, 1.0);	


	// Lighting calculation
    color =	La*Ma;

}