/**
 * cg1Shape.java
 *
 * Class that includes routines for tessellating a number of basic shapes
 *
 * Students are to supply their implementations for the
 * functions in this file using the function "addTriangle()" to do the 
 * tessellation.
 *
 */

import java.awt.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.io.*;


public class cg1Shape extends simpleShape
{
	/**
	 * constructor
	 */
	public cg1Shape()
	{
	}

	/**
	 * makeCube - Create a unit cube, centered at the origin, with a given number
	 * of subdivisions in each direction on each face.
	 *
	 * @param subdivision - number of equal subdivisons to be made in each 
	 *        direction along each face
	 *
	 * Can only use calls to addTriangle()
	 */
	public void makeCube (int subdivisions)
	{
		float divs = (float) (1.0/(float)subdivisions);
		float temp = (float) (divs /(float)2.0);

		// Draw the triangles
		for (float i = (float)-0.5; i < (0.5 - temp); i+=divs){ 
			for (float j = (float)-0.5; j < (0.5 - temp); j+=divs) {
				addTriangle(i, j, (float)0.5, i + divs, j, (float)0.5, i + divs, j + divs, (float)0.5);
				addTriangle(i, j, (float)0.5, i + divs, j + divs, (float)0.5, i, j + divs, (float)0.5);
				addTriangle(i + divs, j, (float)-0.5, i, j, (float)-0.5, i + divs, j + divs, (float)-0.5);
				addTriangle(i + divs, j + divs, (float)-0.5, i, j, (float)0.5, i, j + divs, (float)-0.5);
				addTriangle((float)0.5, i, j + divs, (float)0.5, i, j, (float)0.5, i + divs, j);
				addTriangle((float)0.5, i, j + divs, (float)0.5, i + divs, j, (float)0.5, i + divs, j + divs);
				addTriangle((float)-0.5, i, j, (float)-0.5, i, j + divs, (float)-0.5, i + divs, j);
				addTriangle((float)-0.5, i + divs, j, (float)-0.5, i, j + divs, (float)-0.5, i + divs, j + divs);
				addTriangle(i, (float)0.5, j, i + divs, (float)0.5, j + divs, i + divs, (float)0.5, j);
				addTriangle(i,(float)0.5, j + divs, i + divs, (float)0.5, j + divs, i, (float)0.5, j);
				addTriangle(i + divs, (float)-0.5, j + divs, i, (float)-0.5, j, i + divs, (float)-0.5, j);
				addTriangle(i + divs, (float)-0.5, j + divs, i, (float)-0.5, j + divs, i, (float)-0.5, j);
			}
		}
	}

	/**
	 * makeCylinder - Create polygons for a cylinder with unit height, centered at
	 * the origin, with separate number of radial subdivisions and height 
	 * subdivisions.
	 *
	 * @param radius - Radius of the base of the cylinder
	 * @param radialDivision - number of subdivisions on the radial base
	 * @param heightDivisions - number of subdivisions along the height
	 *
	 * Can only use calls to addTriangle()
	 */
	public void makeCylinder (float radius, int radialDivisions, int heightDivisions)
	{
		float X1 = (float)0.5;
		float z1 = 0;

		float radDivs = (float)((float)3.14 * 2)/((float)radialDivisions);
		float heightDivs = (float)((float)1.0/((float)heightDivisions));
		float temp1 = (float)(radDivs/(float)2.0);
		float temp2 = (float)(heightDivs/(float)2.0);

		for (float rad = radDivs; rad < (((float)3.14 * 2) - temp1); rad += radDivs) {

			float x = (float)(Math.cos(rad) * radius);
			float z = (float)(Math.sin(rad) * radius);

			for (float y = (float)-0.5; y < (0.5 - temp2); y += heightDivs) {
				addTriangle(x, y, z, X1, y, z1, X1, y + heightDivs, z1);
				addTriangle(x, y, z, X1, y+heightDivs, z1, x, y+heightDivs, z);
			}

			addTriangle(0, (float)0.5, 0, x, (float)0.5, z, X1, (float)0.5, z1);
			addTriangle(0, (float)-0.5, 0, X1, (float)-0.5, z1, x, (float)-0.5, z);

			X1 = x;
			z1 = z;
		}

		for (float y = (float)-0.5; y < (0.5 - temp2); y += heightDivs) {
			addTriangle((float)0.5, y, 0, X1, y, z1, X1, y+heightDivs, z1);
			addTriangle((float)0.5, y, 0, X1, y+heightDivs, z1, (float)0.5, y+heightDivs, 0);
		}

		addTriangle(0, (float)0.5, 0, (float)0.5, (float)0.5, 0, X1, (float)0.5, z1);
		addTriangle(0, (float)-0.5, 0, X1, (float)-0.5, z1, (float)0.5, (float)-0.5, 0);
	}


	/**
	 * makeCone - Create polygons for a cone with unit height, centered at the
	 * origin, with separate number of radial subdivisions and height 
	 * subdivisions.
	 *
	 * @param radius - Radius of the base of the cone
	 * @param radialDivision - number of subdivisions on the radial base
	 * @param heightDivisions - number of subdivisions along the height
	 *
	 * Can only use calls to addTriangle()
	 */
	public void makeCone (float radius, int radialDivisions, int heightDivisions)
	{
		float x1 = (float)0.5;
		float z1 = 0;
		float oldRad = 0;

		float radDivs = (float)((float)3.14 * 2) /((float)radialDivisions);
		float heightDivs = (float)((float)1.0 /((float)heightDivisions));
		float temp1 = (float)(radDivs / 2.0);
		float temp2 = (float)(heightDivs/(float)2.0);

		for (float rad = radDivs; rad < ((3.14 * 2) - temp1); rad += radDivs) {
			float x = (float)(Math.cos(rad) * radius);
			float z = (float)(Math.sin(rad) * radius);

			float sideX1Coor = x1;
			float sideX2Coor = x;
			float sideZ1Coor = z1;
			float sideZ2Coor = z;
			float sideYCoor = (float)-0.5;

			addTriangle(sideX1Coor, sideYCoor, sideZ1Coor, 0, (float)0.5, 0, sideX2Coor, sideYCoor, sideZ2Coor);

			for (float yCoord = (float)-0.5 + heightDivs; yCoord < (0.5 - temp2); yCoord += heightDivs) {
				float newRad = (float) ((1 - (yCoord + (float)0.5)) * 0.5);

				Edge newSide = new Edge();
				newSide.setX1((float)(Math.cos(oldRad) * newRad));
				newSide.setZ1((float)(Math.sin(oldRad) * newRad));
				newSide.setX2((float)(Math.cos(rad) * newRad));
				newSide.setZ2((float)(Math.sin(rad) * newRad));

				addTriangle(0, (float)-0.5, 0, x1, (float)-0.5, z1, x, (float)-0.5, z);
				addTriangle(sideX1Coor, sideYCoor, sideZ1Coor, newSide.getX2(), yCoord, newSide.getZ2(), sideX2Coor, sideYCoor, sideZ2Coor);
				addTriangle(sideX1Coor, sideYCoor, sideZ1Coor, newSide.getX1(), yCoord, newSide.getZ1(), newSide.getX2(), yCoord, newSide.getZ2());

				sideYCoor = yCoord;
				sideX1Coor = newSide.getX1();
				sideX2Coor = newSide.getX2();
				sideZ1Coor = newSide.getZ1();
				sideZ2Coor = newSide.getZ2();
			}
			oldRad = rad;
			z1 = z;
			x1 = x;
		}

		float sideX1Coor = x1;
		float sideZ1Coor = z1;
		float newX = (float)0.5;
		float newZ = 0;
		float sideX2Coor = newX;
		float sideZ2Coor = newZ;
		float sideYCoor = (float)-0.5;

		addTriangle(sideX1Coor, sideYCoor, sideZ1Coor, 0, (float)0.5, 0, sideX2Coor, sideYCoor, sideZ2Coor);

		for (float yCoord = (float)-0.5 + heightDivs; yCoord < (0.5 - temp2); yCoord += heightDivs) {
			float newRad = (float)((1 - (yCoord + 0.5)) * 0.5);

			Edge newSide = new Edge();
			newSide.setX1((float)(Math.cos(oldRad) * newRad));
			newSide.setZ1((float)(Math.sin(oldRad) * newRad));
			newSide.setX2(newRad);
			newSide.setZ2(0);

			addTriangle(0, (float)-0.5, 0, x1, (float)-0.5, z1, newX, (float)-0.5, newZ);
			addTriangle(sideX1Coor, sideYCoor, sideZ1Coor, newSide.getX2(), yCoord, newSide.getZ2(), sideX2Coor, sideYCoor, sideZ2Coor);
			addTriangle(sideX1Coor, sideYCoor, sideZ1Coor, newSide.getX1(), yCoord, newSide.getZ1(), newSide.getX2(), yCoord, newSide.getZ2());

			sideX1Coor = newSide.getX1();
			sideZ1Coor = newSide.getZ1();
			sideX2Coor = newSide.getX2();
			sideZ2Coor = newSide.getZ2();
			sideYCoor = yCoord;
		}

	}


	/**
	 * makeSphere - Create sphere of a given radius, centered at the origin, 
	 * using spherical coordinates with separate number of thetha and 
	 * phi subdivisions.
	 *
	 * @param radius - Radius of the sphere
	 * @param slides - number of subdivisions in the theta direction
	 * @param stacks - Number of subdivisions in the phi direction.
	 *
	 * Can only use calls to addTriangle
	 */
	public void makeSphere(float radius, int slices, int stacks) {
		double a = 2.0 / (1 + Math.sqrt(5.0));
		Point[] verts = new Point[12];
		verts[0] = new Point(0.0, a, -1.0).normalize().scale(radius);
		verts[1] = new Point(-a, 1.0, 0.0).normalize().scale(radius);
		verts[2] = new Point(a, 1, 0).normalize().scale(radius);
		verts[3] = new Point(0.0, a, 1).normalize().scale(radius);
		verts[4] = new Point(-1, 0, a).normalize().scale(radius);
		verts[5] = new Point(0, -a, 1).normalize().scale(radius);
		verts[6] = new Point(1, 0, a).normalize().scale(radius);
		verts[7] = new Point(1, 0, -a).normalize().scale(radius);
		verts[8] = new Point(0, -a, -1).normalize().scale(radius);
		verts[9] = new Point(-1, 0, -a).normalize().scale(radius);
		verts[10] = new Point(-a, -1, 0).normalize().scale(radius);
		verts[11] = new Point(a, -1, 0).normalize().scale(radius);

		process(verts[0], verts[1], verts[2], slices, radius);
		process(verts[3], verts[2], verts[1], slices, radius);
		process(verts[3], verts[4], verts[5], slices, radius);
		process(verts[3], verts[5], verts[6], slices, radius);
		process(verts[0], verts[7], verts[8], slices, radius);
		process(verts[0], verts[8], verts[9], slices, radius);
		process(verts[5], verts[10], verts[11], slices, radius);
		process(verts[8], verts[11], verts[10], slices, radius);
		process(verts[1], verts[9], verts[4], slices, radius);
		process(verts[10], verts[4], verts[9], slices, radius);
		process(verts[2], verts[6], verts[7], slices, radius);
		process(verts[11], verts[7], verts[6], slices, radius);
		process(verts[3], verts[1], verts[4], slices, radius);
		process(verts[3], verts[6], verts[2], slices, radius);
		process(verts[0], verts[9], verts[1], slices, radius);
		process(verts[0], verts[2], verts[7], slices, radius);
		process(verts[8], verts[10], verts[9], slices, radius);
		process(verts[8], verts[7], verts[11], slices, radius);
		process(verts[5], verts[4], verts[10], slices, radius);
		process(verts[5], verts[11], verts[6], slices, radius);

	}

	/*
	 * Normalize the point.
	 */
	void process(Point p1, Point p2, Point p3, int sub, float radius) {
		if (sub > 1) {
			Point mid1 = Point.calcMidPointOnSphere(p1, p2, radius);
			Point mid2 = Point.calcMidPointOnSphere(p3, p2, radius);
			Point mid3 = Point.calcMidPointOnSphere(p1, p3, radius);
			// subdivide
			process(p1, mid1, mid3, sub - 1, radius);
			process(p2, mid2, mid1, sub - 1, radius);
			process(p3, mid3, mid2, sub - 1, radius);
			process(mid1, mid2, mid3, sub - 1, radius);
		} else {
			addTriangle(p1, p2, p3);
		}
	}

	void addTriangle(Point p1, Point p2, Point p3) {
		addTriangle((float) p1.x, (float) p1.y, (float) p1.z, (float) p2.x,
				(float) p2.y, (float) p2.z, (float) p3.x, (float) p3.y,
				(float) p3.z);
	}

	private static class Point {
		public double x;
		public double y;
		public double z;

		public Point() {
			x = y = z = 0;
		}

		public static Point calcMidPointOnSphere(Point p1, Point p2,
				float radius) {
			Point m = new Point();
			m.x = ((p1.x - p2.x) / 2) + p2.x;
			m.y = ((p1.y - p2.y) / 2) + p2.y; 
			m.z = (p1.z - p2.z) / 2 + p2.z;

			return m.normalize().scale(radius);
		}

		public Point(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Point normalize() {
			double div = Math.sqrt(x * x + y * y + z * z);
			x /= div;
			y /= div;
			z /= div;
			return this;
		}

		public Point setOrigin(Point O) {
			x = x - O.x;
			y = y - O.y;
			z = z - O.z;
			return this;
		}

		public Point scale(double val) {
			x *= val;
			y *= val;
			z *= val;
			return this;
		}

	}

}
