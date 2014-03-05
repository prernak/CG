import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Jama.Matrix;

//
//  cg1Canvas.java
//  
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

/**
 * This is a simple canvas class for adding functionality for the
 * 2D portion of Computer Graphics I.
 *
 */

public class cg1Canvas extends simpleCanvas {

    Map<Integer, List<List<Float>>> polygonW = new HashMap<Integer, List<List<Float>>>();
    Map<Integer, List<List<Float>>> polygonT = new HashMap<Integer, List<List<Float>>>();
    Map<Integer, List<List<Float>>> polygonV = new HashMap<Integer, List<List<Float>>>();
    Map<Integer, List<List<Float>>> polygonC = new HashMap<Integer, List<List<Float>>>();
    double[][] transMatrix = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    Matrix transformationMatrix = new Matrix(transMatrix);
    Edge clipWindow;
    Edge viewPort;
    Integer polyID = -1;

    /**
     * Constructor
     * 
     * @param w
     *            width of canvas
     * @param h
     *            height of canvas
     */
    cg1Canvas(int w, int h) {
        super(w, h);
    }

    /**
     * 
     * addPoly - Adds and stores a polygon to the canvas. Note that this method
     * does not draw the polygon, but merely stores it for later draw. Drawing
     * is initiated by the draw() method.
     * 
     * Returns a unique integer id for the polygon.
     * 
     * @param x
     *            - Array of x coords of the vertices of the polygon to be
     *            added.
     * @param y
     *            - Array of y coords of the vertices of the polygin to be
     *            added.
     * @param n
     *            - Number of verticies in polygon
     * 
     * @return a unique integer identifier for the polygon
     */
    public int addPoly(float x[], float y[], int n) {
        polyID++;
        List<Float> xVertices = new ArrayList<Float>();
        List<Float> yVertices = new ArrayList<Float>();
        for (int i = 0; i < n; i++) {
            xVertices.add(x[i]);
            yVertices.add(y[i]);
        }

        List<List<Float>> vertices = new ArrayList<List<Float>>();
        vertices.add(xVertices);
        vertices.add(yVertices);
        polygonW.put(polyID, vertices);
        return polyID;
    }

    /**
     * 
     * clearTransform - sets the current transformation to be the identity
     * 
     */
    public void clearTransform() {
    	double[][] transMatrix = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    	transformationMatrix = new Matrix(transMatrix);
    }

    /**
     * 
     * draw - Draw the polygon with the given id. Draw should draw the polygon
     * after applying the current transformation on the vertices of the polygon.
     * 
     * @param polyID
     *            - the ID of the polygin to be drawn.
     */
    public void draw(int polyID) {
        Map<Double, List<Bucket>> edgeTable = new HashMap<Double, List<Bucket>>();
        ModelTransform mt = new ModelTransform();
        polygonT = mt.transformFigure(polygonW, transformationMatrix);
        boolean isTransformed = !mt.isIdentity(transformationMatrix);
        Clipper c = new Clipper();
        polygonC = c.clip(polygonT, clipWindow);
        boolean isClipped = c.isClipped(clipWindow);
        convert2ViewPort();
        
        double yMin = 0, yMax, xVal = 0, dx, dy, yVal = 0;
        double xMax = 0, xMin = 0;
        List<Float> x = polygonV.get(polyID).get(0);
        List<Float> y = polygonV.get(polyID).get(1);
        
        /*** Create edge table ***/
        for (int i = 0; i < x.size(); i++) {	
            if ((i + 1) == x.size()) {
                yMin = (y.get(i) > y.get(0)) ? y.get(0) : y.get(i);
                yMax = (y.get(i) > y.get(0)) ? y.get(i) : y.get(0);
                xMax = (x.get(i) > x.get(0)) ? x.get(i) : x.get(0);
                xMin = (x.get(i) > x.get(0)) ? x.get(0) : x.get(i);
                xVal = (y.get(i) > y.get(0)) ? x.get(0) : x.get(i);
                dx = x.get(0) - x.get(i);
                dy = y.get(0) - y.get(i);
            } else {
                yMin = (y.get(i) > y.get(i + 1)) ? y.get(i + 1) : y.get(i);
                yMax = (y.get(i) > y.get(i + 1)) ? y.get(i) : y.get(i + 1);
                xMax = (x.get(i) > x.get(i + 1)) ? x.get(i) : x.get(i + 1);
                xMin = (x.get(i) > x.get(i + 1)) ? x.get(i + 1) : x.get(i);
                xVal = (y.get(i) > y.get(i + 1)) ? x.get(i + 1) : x.get(i);
                dx = x.get(i + 1) - x.get(i);
                dy = y.get(i + 1) - y.get(i);
            }

            /*** Check if line is horizontal ***/

            if (dy != 0) {
                Bucket newBucket = new Bucket(yMax, xVal, dx, dy, 0.0f);
                if (edgeTable.get(Math.floor(yMin)) != null) {
                    edgeTable.get(Math.floor(yMin)).add(newBucket);
                } else {
                    List<Bucket> similarBuckets = new ArrayList<Bucket>();
                    similarBuckets.add(newBucket);
                    edgeTable.put(new Double(Math.floor(yMin)), similarBuckets);
                }
            }
            if (i == 0)
                yVal = yMin;
            else if (yMin != 0)
                yVal = (yVal > yMin) ? yMin : yVal;

        }

        /*** Create Active edge table and fill the polygon ***/

        List<Bucket> activeEdgeTable = new ArrayList<Bucket>();
        while (edgeTable.size() > 0 || activeEdgeTable.size() > 0) {
            List<Bucket> temp = new ArrayList<Bucket>();
            temp.addAll(activeEdgeTable);
            for (Bucket bucket : temp) {
            	if (polyID == 0 && isTransformed){
            		if (Math.floor(yVal) >= Math.floor(bucket.getyMax())) {
	                    activeEdgeTable.remove(bucket);
	                }
            	}else if (polyID == 2 || isClipped || isTransformed){
            		if (Math.ceil(yVal) >= bucket.getyMax()) {
                        activeEdgeTable.remove(bucket);
                    }
            	}else {
	                if (Math.ceil(yVal) >= Math.floor(bucket.getyMax())) {
	                    activeEdgeTable.remove(bucket);
	                }
            	}
            }
            
            /*** Move buckets from edge table to active edge table ***/
            
            if (edgeTable.get(Math.floor(yVal)) != null) {
                for (Bucket bucket : edgeTable.get(Math.floor(yVal))) {
                    activeEdgeTable.add(bucket);
                }
                edgeTable.remove(Math.floor(yVal));
            }

            Collections.sort(activeEdgeTable, new BucketSort());
            
            for (int i = 0; i < activeEdgeTable.size(); i++) {
                if (i % 2 == 0) {
                    for (Double xCoord = activeEdgeTable.get(i).getX(); xCoord <= activeEdgeTable.get(i + 1).getX(); xCoord++) {
                        setPixel((int) Math.floor(xCoord), (int) Math.floor(yVal));
                    }
                }
                activeEdgeTable.get(i).setSum(activeEdgeTable.get(i).getSum() + Math.abs(activeEdgeTable.get(i).getDx()));
                while (activeEdgeTable.get(i).getSum() >= Math.abs(activeEdgeTable.get(i).getDy())) {
                    if (activeEdgeTable.get(i).getSlope() < 0) {
                        activeEdgeTable.get(i).setX(activeEdgeTable.get(i).getX() - 1);
                    } else {
                        activeEdgeTable.get(i).setX(activeEdgeTable.get(i).getX() + 1);
                    }
                    activeEdgeTable.get(i).setSum(activeEdgeTable.get(i).getSum() - Math.abs(activeEdgeTable.get(i).getDy()));
                }
            }
            yVal++;
        }
    }

    /**
     * 
     * rotate - Add a rotation to the current transformation by premultiplying
     * the appropriate rotation matrix to the current transformtion matrix.
     * 
     * @param degrees
     *            - Amount of rotation in degrees.
     * 
     */
    public void rotate(float degrees) {
    	double x, y;
        x = Math.cos(Math.toRadians(degrees));
        y = Math.sin(Math.toRadians(degrees)); 
        double[][] rotateMatrix = { { x, -y, 0 },
                { y, x, 0 }, { 0, 0, 1 } };
        Matrix rotate = new Matrix(rotateMatrix);
        transformationMatrix = rotate.times(transformationMatrix);
    }
    
    /**
     * 
     * scale - Add a scale to the current transformation by premultiplying the
     * appropriate scaling matrix to the current transformtion matrix.
     * 
     * @param x
     *            - Amount of scaling in x.
     * @param y
     *            - Amount of scaling in y.
     * 
     */
    public void scale(float x, float y) {
        double[][] m1 = { { x, 0, 0 }, { 0, y, 0 }, { 0, 0, 1 } };
        Matrix translate = new Matrix(m1);
        transformationMatrix = translate.times(transformationMatrix);
    }

    /**
     * 
     * setClipWindow - defines the clip window
     * 
     * @param bottom
     *            - y coord of bottom edge of clip window (in world coords)
     * @param top
     *            - y coord of top edge of clip window (in world coords)
     * @param left
     *            - x coord of left edge of clip window (in world coords)
     * @param right
     *            - x coord of right edge of clip window (in world coords)
     * 
     */
    public void setClipWindow(float bottom, float top, float left, float right) {
        clipWindow = new Edge(left, bottom, right, top);
    }
    
    /**
     * 
     * setViewport - defines the viewport
     * 
     * @param xmin
     *            - x coord of lower left of view window (in screen coords)
     * @param ymin
     *            - y coord of lower left of view window (in screen coords)
     * @param width
     *            - width of view window (in world coords)
     * @param height
     *            - width of view window (in world coords)
     * 
     */
    public void setViewport(int x, int y, int width, int height) {
    	viewPort = new Edge(x, y, width, height);
    }
    
    private void convert2ViewPort() {
        float sx = (float) ((viewPort.getX2() - viewPort.getX1()) / (clipWindow.getX2() - clipWindow.getX1()));
        float sy = (float) ((viewPort.getY2()- viewPort.getY1()) / (clipWindow.getY2() - clipWindow.getY1()));
        float tx = (((clipWindow.getX2() * viewPort.getX1()) - (clipWindow.getX1() * (viewPort.getX2()))) / (clipWindow.getX2() - clipWindow.getX1()));
        float ty = (((clipWindow.getY2() * viewPort.getY1()) - (clipWindow.getY1() * (viewPort.getY2()))) / (clipWindow.getY2() - clipWindow.getY1()));
        
        double[][] m1 = { { sx, 0, tx }, { 0, sy, ty }, { 0, 0, 1 } };
        Matrix viewPort = new Matrix(m1);
        List<List<Float>> temp;
        for (int i = 0; i < 4; i++) {
            List<List<Float>> poly = polygonC.get(i);
            temp = new ArrayList<List<Float>>();
            List<Float> vx = new ArrayList<Float>();
        	List<Float> vy = new ArrayList<Float>();
            for (int j = 0; j < poly.get(0).size(); j++) {
                double[][] m2 = { { (poly.get(0).get(j)) },
                        { (poly.get(1).get(j)) }, { 1 } };
                Matrix vertex = new Matrix(m2);
                Matrix output = viewPort.times(vertex);
                vx.add((float) Math.abs(output.get(0, 0)));
                vy.add((float) Math.abs(output.get(1, 0)));
            }
            temp.add(vx);
            temp.add(vy);
            polygonV.put(i, temp);
        }
    }

    /**
     * 
     * translate - Add a translation to the current transformation by
     * premultiplying the appropriate translation matrix to the current
     * transformtion matrix.
     * 
     * @param x
     *            - Amount of translation in x.
     * @param y
     *            - Amount of translation in y.
     * 
     */
    public void translate(float x, float y) {
        double[][] m1 = { { 1, 0, x }, { 0, 1, y }, { 0, 0, 1 } };
        Matrix translate = new Matrix(m1);
        transformationMatrix = translate.times(transformationMatrix);
    }
}
