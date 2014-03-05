attribute vec4 vPosition;
uniform vec3 theta;
uniform vec3 trans;
uniform vec3 scale;

void main()
{
    // Compute the sines and cosines of each rotation
    // about each axis
    vec3 angles = radians (theta);
    vec3 c = cos (angles);
    vec3 s = sin (angles);
    
    // rotation matricies
    mat4 rx = mat4 (1.0,  0.0,  0.0,  0.0, 
                    0.0,  c.x,  s.x,  0.0,
                    0.0, -s.x,  c.x,  0.0,
                    0.0,  0.0,  0.0,  1.0);
                    
    mat4 ry = mat4 (c.y,  0.0, -s.y,  0.0, 
                    0.0,  1.0,  0.0,  0.0,
                    s.y,  0.0,  c.y,  0.0,
                    0.0,  0.0,  0.0,  1.0);

    mat4 rz = mat4 (c.z, -s.z,  0.0,  0.0, 
                    s.z,  c.z,  0.0,  0.0,
                    0.0,  0.0,  1.0,  0.0,
                    0.0,  0.0,  0.0,  1.0);

    mat4 transMat = mat4 ( 1.0, 0.0, 0.0, 0.0,
			0.0, 1.0, 0.0, 0.0,
			0.0, 0.0, 1.0, 0.0,
			trans.x, trans.y, trans.z, 1.0);
            
    mat4 scaleMat = mat4 (scale.x,  0.0,     0.0,     0.0,
                          0.0,      scale.y, 0.0,     0.0, 
                          0.0,      0.0,     scale.z, 0.0,
                          0.0,      0.0,     0.0,     1.0);
          
    
    gl_Position = transMat * rz * ry * rx * vPosition;
}
