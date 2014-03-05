// vertex shader
attribute vec4 vPosition;
attribute vec3 vNormal;
varying vec4 color;
attribute vec2 vTexCoord;
uniform vec4 AmbientProduct, DiffuseProduct,
  SpecularProduct;
 
uniform vec4 LightPosition;
uniform float Shininess;

uniform vec3 theta;
uniform vec3 eyePoint;
uniform vec3 lookAt;
uniform vec3 up;
varying vec2 texCoord;

void main()
{
    // Compute the sines and cosines of each rotation
    // about each axis
    vec3 angles = radians (theta);
    vec3 c = cos (angles);
    vec3 s = sin (angles);
    
    // Compute the camera coordinates
	vec3 n = normalize(eyePoint-lookAt);
	vec3 u = normalize(cross(up,n));
	vec3 v = cross(n,u);
	
	// Model to View Transform
	mat4 ModelView = mat4(u.x, v.x, n.x, 0.0,
		u.y, v.y, n.y, 0.0,
		u.z, v.z, n.z, 0.0,
		dot(-u, eyePoint), dot(-v, eyePoint), dot(-n, eyePoint), 1);

	float l = -1.0;
	float r = 1.0;
	float t = 1.0;
	float b = -1.0;
	float nn = 1.0;
	float f = 3.5;
	
	mat4 Projection = mat4( (2.0 * nn)/(r - l), 0.0, 0.0, 0.0,
				0.0, (2.0 * nn)/(t - b), 0.0, 0.0,
				(r + l)/(r - l), (t + b)/(t - b), -(f + nn)/(f - nn), -1.0,
				0.0, 0.0, (-2.0 * f * nn)/ (f - nn), 0.0);
							
	//Transform vertex position into eye coordinates
	vec3 pos = (ModelView * vPosition).xyz;
	
	vec3 L = normalize(LightPosition.xyz - pos);
	vec3 E = normalize(-pos);
	vec3 H = normalize(L + E);
	
	// Transform vertex normal into eye coordinates
	// Should really use the normal matrix
	vec3 N = normalize(ModelView * vec4(vNormal, 0.0)).xyz;
	
	//Compute terms in the illumination equation
	vec4 ambient = AmbientProduct;
	float Kd = max( dot(L,N), 0.0);
	vec4 diffuse = Kd*DiffuseProduct;
	float Ks = pow( max(dot(N,H), 0.0), Shininess);
	vec4 specular = Ks * SpecularProduct;
	if( dot(L,N) < 0.0)
		specular = vec4(0.0, 0.0, 0.0, 1.0);
	
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
	      
    gl_Position = rz * ry * rx * Projection * ModelView * vPosition;
    color = vec4(1.0, 1.0, 1.0, 1.0);
    texCoord = vTexCoord;
}
