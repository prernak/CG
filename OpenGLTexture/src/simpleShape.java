/**
 *
 * simpleShape.java
 *
 * class that represnts a shape to be tessellated.  cg1shape, which includes all student
 * code, is derived from this class.
 *
 * Of note is the protected method addTriangles() which is what students should use to
 * define their tessellations.
 *
 * Students are not to modify this file.
 *
 */


import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.util.*;


public class simpleShape
{
    /**
     * our vertex points
     */
    private Vector<Float> points;
    
    private Vector<Float> normals;
    private Vector<Float> textureCoords;
    float u[] = new float[3];
    float v[] = new float[3];
    float nx,ny,nz;
    
    /**
     * our array elements
     */
    private Vector<Short> elements;
    private short nVerts;
    
	/**
	 * constructor
	 */
	public simpleShape()
	{
        points = new Vector<Float>();
        elements = new Vector<Short>();
        normals = new Vector<Float>();
        textureCoords = new Vector<Float>();
        nVerts = 0;
	}
    
    /**
     * add a triangle to the shape
     */
    protected void addTriangle (float x0, float y0, float z0,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                float textureU[], float textureV[]) {
        points.add (new Float(x0));
        points.add (new Float(y0));
        points.add (new Float(z0));
        points.add (new Float(1.0f));
        elements.add (new Short(nVerts));
        nVerts++;
        textureCoords.add (new Float(textureU[0])+0.5f);
        textureCoords.add (new Float(textureV[0])+0.5f);
        
        points.add (new Float(x1));
        points.add (new Float(y1));
        points.add (new Float(z1));
        points.add (new Float(1.0f));
        elements.add (new Short(nVerts));
        nVerts++;
        textureCoords.add (new Float(textureU[1])+0.5f);
        textureCoords.add (new Float(textureV[1])+0.5f);
        
        points.add (new Float(x2));
        points.add (new Float(y2));
        points.add (new Float(z2));
        points.add (new Float(1.0f));
        elements.add (new Short(nVerts));
        nVerts++;
        textureCoords.add (new Float(textureU[2])+0.5f);
        textureCoords.add (new Float(textureV[2])+0.5f);
        
        u[0] = x1-x0;
        u[1] = y1-y0;
        u[2] = z1-z0;
        
        v[0] = x2-x0;
        v[1] = y2-y0;
        v[2] = z2-z0;
        
        /*** Normals vector ****/
        nx = (u[1]*v[2])-(u[2]*v[1]);
        ny = (u[2]*v[0])-(u[0]*v[2]);
        nz = (u[0]*v[1])-(u[1]*v[0]);
        
        for (int i=0;i<3;i++)
        {
            normals.add(nx);
            normals.add(ny);
            normals.add(nz);
        }
    }
    
    /**
     * clear the shape
     */
    public void clear()
    {
        points= new Vector<Float>(); 
        elements = new Vector<Short>();
        normals= new Vector<Float>();
        nVerts = 0;
    }
    
    public Buffer getVerticies ()
    {
        float v[] = new float[points.size()];
        for (int i=0; i < points.size(); i++) {
            v[i] = (points.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap (v);
    }
    
    public Buffer getElements ()
    {
        short e[] = new short[elements.size()];
        for (int i=0; i < elements.size(); i++) {
            e[i] = (elements.elementAt(i)).shortValue();
        }

        return ShortBuffer.wrap (e);
    }

    
    public Buffer getNormals () {
        float v[] = new float[normals.size()];
        for (int i=0; i < normals.size(); i++) {
            v[i] = (normals.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap (v);
    }
    
    public Buffer getTextures()
    {
		float v[] = new float[textureCoords.size()];
		for (int i=0; i < textureCoords.size(); i++) {
			v[i] = (textureCoords.elementAt(i)).floatValue();
		}
		return FloatBuffer.wrap (v);
    }
    
    public short getNVerts()
    {
        return nVerts;
    }
}
