
#version 410

uniform mat4 proj_matrix, mv_matrix, light_matrix; 


layout (location = 0) in vec3 iPosition; // VBO: vbo[0]
//layout (location = 1) in vec3 iNormal; // VBO: vbo[1]


//out vec4 position, normal; // output to fragment shader for lighting calculation
//out vec3 tc; 

// TNB only for spheres
//out vec3 tNormal, nNormal, bNormal; 

//out vec4 shadowCoord;
 
void	main(void)	{	
	
	gl_Position = light_matrix*mv_matrix*vec4(iPosition, 1.0);	

/*
	position = mv_matrix*vec4(iPosition, 1.0);	
	
	// used to interpolate for each pixel, to retrieve shadowmap and compare
	shadowCoord = light_matrix*mv_matrix*vec4(iPosition, 1.0);	
	

	// normal is transformed by inverse-transpose of the current matrix
	mat4 mv_it = transpose(inverse(mv_matrix)); 
	normal  = mv_it*vec4(iNormal, 1); 

	 nNormal = normal.xyz; 
	 tNormal = vec3(1, 0, 1);  
	 bNormal = cross(tNormal, nNormal); 
	 tNormal = cross(nNormal, bNormal); 

	 tNormal = normalize(tNormal); 
	 nNormal = normalize(nNormal);  
	 bNormal = normalize(bNormal); 
 
	
	// texture coordinates from vertex position
	tc = iPosition; 
*/
}

