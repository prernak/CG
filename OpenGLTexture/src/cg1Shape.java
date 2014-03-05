/**
 * cg1Shape.java
 *
 * Class that includes routines for tessellating a number of basic shapes
 *
 * Students are to supplb their implementations for the
 * functions in this file using the function "addTriangle()" to do the 
 * tessellation.
 *
 */

public class cg1Shape extends simpleShape
{
	
	public cg1Shape()
	{
	}
	
	public void makeCylinder() {
		float radius = 0.5f;
		float X1 = (float)0.5;
		float z1 = 0;

		float radDivs = (float)((float)3.14 * 2)/((float)70);
		float heightDivs = (float)((float)1.0/((float)70));
		float temp1 = (float)(radDivs/(float)2.0);
		float temp2 = (float)(heightDivs/(float)2.0);

		for (float rad = radDivs; rad < (((float)3.14 * 2) - temp1); rad += radDivs) {
			float u[] = new float[3];
			float v[] = new float[3];
			
			float x = (float)(Math.cos(rad) * radius);
			float z = (float)(Math.sin(rad) * radius);
			
			for (float y = (float)-0.5; y < (0.5 - temp2); y += heightDivs) {
				u[0] = x; u[1] = X1; u[2] = X1; v[0] = y; v[1] = y; v[2] = y + heightDivs;
				addTriangle(x, y, z, X1, y, z1, X1, y + heightDivs, z1, u, v);
				addTriangle(x, y, z, X1, y+heightDivs, z1, x, y+heightDivs, z, u, v);
			}

			u[0] = 0; u[1] = x; u[2] = X1; v[0] = (float)0.5; v[1] = (float)0.5; v[2] = (float)-0.5;
			addTriangle(0, (float)0.5, 0, x, (float)0.5, z, X1, (float)0.5, z1, u, v);
			addTriangle(0, (float)-0.5, 0, X1, (float)-0.5, z1, x, (float)-0.5, z, u, v);

			X1 = x;
			z1 = z;
		}

		for (float y = (float)-0.5; y < (0.5 - temp2); y += heightDivs) {
			u[0] = (float)0.5; u[1] = X1; u[2] = X1; v[0] = y; v[1] = y; v[2] = y + heightDivs;
			addTriangle((float)0.5, y, 0, X1, y, z1, X1, y+heightDivs, z1, u, v);
			addTriangle((float)0.5, y, 0, X1, y+heightDivs, z1, (float)0.5, y+heightDivs, 0, u, v);
		}
		
		u[0] = 0; u[1] = (float)0.5; u[2] = X1; v[0] = (float)0.5; v[1] = (float)0.5; v[2] = (float)0.5;
		addTriangle(0, (float)0.5, 0, (float)0.5, (float)0.5, 0, X1, (float)0.5, z1, u, v);
		addTriangle(0, (float)-0.5, 0, X1, (float)-0.5, z1, (float)0.5, (float)-0.5, 0, u, v);
	}
}
