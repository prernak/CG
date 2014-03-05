import java.util.ArrayList;
import java.util.List;

//
//  cg1Canvas.java
//  
//
//  Created by Joe Geigel on 1/21/10.
//  Modified by Prerna Keshari
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

/**
 * This is a simple canvas class for adding functionality for the
 * 2D portion of Computer Graphics I.
 *
 */

public class cg1Canvas extends simpleCanvas {
    
    /**
     * Constructor
     *
     * @param w width of canvas
     * @param h height of canvas
     */
    cg1Canvas (int w, int h)
    {
        super (w, h);
    }
    
//    /**
//     * clipPolygon
//     * 
//     * Clip the polygon with vertex count in and vertices inx/iny
//     * against the rectangular clipping region specified by lower-left corner
//     * (x0,y0) and upper-right corner (x1,y1). The resulting vertices are
//     * placed in outx/outy.  
//     * 
//     * The routine should return the with the vertex count of polygon
//     * resultinhg from the clipping.
//     *
//     * @param in the number of vertices in the polygon to be clipped
//     * @param inx - x coords of vertices of polygon to be clipped.
//     * @param int - y coords of vertices of polygon to be clipped.
//     * @param outx - x coords of vertices of polygon resulting after clipping.
//     * @param outy - y coords of vertices of polygon resulting after clipping.
//     * @param x0 - x coord of lower left of clipping rectangle.
//     * @param y0 - y coord of lower left of clipping rectangle.
//     * @param x1 - x coord of upper right of clipping rectangle.
//     * @param y1 - y coord of upper right of clipping rectangle.
//     *
//     * @return number of vertices in the polygon resulting after clipping
//     * 
//     */
//    int clipPolygon(int in, float inx[], float iny[], float outx[], 
//                    float outy[], float x0, float y0, float x1, float y1)
//    {
//    	Edge boundaryPoints = new Edge(x0, y0, x1, y1);
//    	int outCount = 0;
//    	
//    	for (int i = 0; i < 4; i++) {
//	    	List<Edge> edges = createEdges(in, inx, iny);
//	        outCount = 0;
//	        List<float[]> temp;
//	        for (Edge eachEdge:edges){
//	        	if (isInside(eachEdge.getX2(), eachEdge.getY2(), boundaryPoints, i)) {
//	        		if (isInside(eachEdge.getX1(), eachEdge.getY1(), boundaryPoints, i)) { /** If V1 and V2 both are inside **/
//	        			outx[outCount] = eachEdge.getX2();
//	        			outy[outCount++] = eachEdge.getY2();
//	        		}else {
//	        			temp = intersect(eachEdge, outx, outy, outCount, boundaryPoints, i); /** If V2 is inside and V1 is outside **/
//	        			outx = temp.get(0);
//	        			outy = temp.get(1);
//	        			outCount++;
//	        			outx[outCount] = eachEdge.getX2();
//	        			outy[outCount++] = eachEdge.getY2();
//	        		}
//	        	}else {
//	        		if (isInside(eachEdge.getX1(), eachEdge.getY1(), boundaryPoints, i)) { 
//	        			temp = intersect(eachEdge, outx, outy, outCount, boundaryPoints, i); /** If V2 is outside and V1 is inside **/
//	        			outx = temp.get(0);
//	        			outy = temp.get(1);
//	        			outCount++;
//	        		}
//	        	}
//	        }
//	        inx = outx;
//	        iny = outy;
//	        in = outCount;
//    	}
//    	return outCount;  // should return number of verticies of poly after clip
//    }	
//    
//    /**
//     * This method creates the edges for the given set of points
//     *  
//     */
//    private List<Edge> createEdges(int length, float inx[], float iny[]) {
//    	List<Edge> edges = new ArrayList<Edge>();
//    	if (inx.length == iny.length){
//    		Edge edge = null;
//    		for (int i = 0; i <length; i++) {
//    			if ((i + 1) == (length)) {
//    				edge = new Edge(inx[i], iny[i], inx[0], iny[0]);
//    			}else {
//    				edge = new Edge(inx[i], iny[i], inx[i + 1], iny[i + 1]);
//    			}
//    			edges.add(edge);
//    		}
//    	}
//    	return edges;
//    }
//    
//    /**
//     * This method determines whether a point in inside of outside a particular clip side.
//     * 
//     * @param boundaryPoints contains lower left and upper right vertices of clipping window.
//     * @param side determines the clip side.
//     */
//    private boolean isInside(float x, float y, Edge boundaryPoints, int side) {
//    	switch (side) {
//    		case 0:return (x < boundaryPoints.getX2());
//    		case 1:return (y < boundaryPoints.getY2());
//    		case 2:return (x >= boundaryPoints.getX1());
//    		case 3:return (y >= boundaryPoints.getY1());
//    	}
//    	return false;
//    }
//    
//    /**
//     * This method find the intersect point
//     * 
//     * @param edge edge connecting the vertices to be determined for in or out
//     * @param outx x coords of vertices of polygon resulting after clipping.
//     * @param outy y coords of vertices of polygon resulting after clipping.
//     * @param count total number of vertices in output polygon.
//     * @param boundaryPoints contains lower left and upper right vertices of clipping window.
//     * @param side determines the clip side.
//     * @return list containing outx and outy.
//     */
//    private List<float[]> intersect(Edge edge, float[] outx, float[] outy, int count, Edge boundaryPoints, int side) {
//    	float m = (float) ((edge.getY2() - edge.getY1())/(edge.getX2() - edge.getX1()));
//		switch (side) {
//			case 0:outx[count] = boundaryPoints.getX2(); /** Right clipping **/
//				   outy[count] = edge.getY1() + ((boundaryPoints.getX2() - edge.getX1()) * m);
//				   break;
//			case 1:outx[count] = edge.getX1() + ((boundaryPoints.getY2() - edge.getY1()) / m); /** Top clipping **/
//				   outy[count] = boundaryPoints.getY2();
//				   break;
//			case 2:outx[count] = boundaryPoints.getX1(); /** Left clipping **/
//				   outy[count] = edge.getY1() + ((boundaryPoints.getX1() - edge.getX1()) * m);
//				   break;
//			case 3:outx[count] = edge.getX1() + ((boundaryPoints.getY1() - edge.getY1()) / m); /** Botton clipping **/
//				   outy[count] = boundaryPoints.getY1();
//				   break;
//		}
//		List<float[]> temp = new ArrayList<float[]>();
//		temp.add(outx);
//		temp.add(outy);
//		return temp;
//    }
    
    int clipPolygon(int in, float inx[], float iny[], float outx[], 
          float outy[], float x0, float y0, float x1, float y1)
{
    	List<Float> inxx = new ArrayList<Float>();
    	List<Float> inyy = new ArrayList<Float>();
    	for (int i = 0; i < in; i ++) {
    		inxx.add(inx[i]);
    		inyy.add(iny[i]);
    	}
    	List<Float> outxx = new ArrayList<Float>();
		List<Float> outyy = new ArrayList<Float>();
    	return clipPolygon(in, inxx, inyy, outxx, outyy, x0, y0, x1, y1);
}
    
    public int clipPolygon(int in, List<Float> inx, List<Float> iny, List<Float> outx, 
			List<Float> outy, float x0, float y0, float x1, float y1) {
		Edge boundaryPoints = new Edge(x0, y0, x1, y1);

		for (int i = 0; i < 4; i++) {
			List<Edge> edges = createEdges(in, inx, iny);
		    List<List<Float>> temp;
		    outx.removeAll(outx);
		    outy.removeAll(outy);
		    for (Edge eachEdge:edges){
		    	if (isInside(eachEdge.getX2(), eachEdge.getY2(), boundaryPoints, i)) {
		    		if (isInside(eachEdge.getX1(), eachEdge.getY1(), boundaryPoints, i)) { /** If V1 and V2 both are inside **/
		    			outx.add(eachEdge.getX2());
		    			outy.add(eachEdge.getY2());
		    		}else {
		    			temp = intersect(eachEdge, outx, outy, boundaryPoints, i); /** If V2 is inside and V1 is outside **/
		    			outx = temp.get(0);
		    			outy = temp.get(1);
		    			outx.add(eachEdge.getX2());
		    			outy.add(eachEdge.getY2());
		    		}
		    	}else {
		    		if (isInside(eachEdge.getX1(), eachEdge.getY1(), boundaryPoints, i)) { 
		    			temp = intersect(eachEdge, outx, outy, boundaryPoints, i); /** If V2 is outside and V1 is inside **/
		    			outx.add(eachEdge.getX2());
		    			outy.add(eachEdge.getY2());
		    		}
		    	}
		    }
		    inx.removeAll(inx);
		    iny.removeAll(iny);
		    inx.addAll(outx);
		    iny.addAll(outy);
		    in = inx.size();
		}
		return in;  // should return number of verticies of poly after clip
	}	
		
	/**
	* This method creates the edges for the given set of points
	*  
	*/
	private List<Edge> createEdges(int length, List<Float> inx, List<Float> iny) {
	List<Edge> edges = new ArrayList<Edge>();
	if (inx.size() == iny.size()){
		Edge edge = null;
		for (int i = 0; i <length; i++) {
			if ((i + 1) == (length)) {
				edge = new Edge(inx.get(i), iny.get(i), inx.get(0), iny.get(0));
			}else {
				edge = new Edge(inx.get(i), iny.get(i), inx.get(i + 1), iny.get(i + 1));
			}
			edges.add(edge);
		}
	}
	return edges;
	}
	
	/**
	* This method determines whether a point in inside of outside a particular clip side.
	* 
	* @param boundaryPoints contains lower left and upper right vertices of clipping window.
	* @param side determines the clip side.
	*/
	private boolean isInside(float x, float y, Edge boundaryPoints, int side) {
	switch (side) {
		case 0:return (x < boundaryPoints.getX2());
		case 1:return (y < boundaryPoints.getY2());
		case 2:return (x >= boundaryPoints.getX1());
		case 3:return (y >= boundaryPoints.getY1());
	}
	return false;
	}
	
	/**
	* This method find the intersect point
	* 
	* @param edge edge connecting the vertices to be determined for in or out
	* @param outx x coords of vertices of polygon resulting after clipping.
	* @param outy y coords of vertices of polygon resulting after clipping.
	* @param count total number of vertices in output polygon.
	* @param boundaryPoints contains lower left and upper right vertices of clipping window.
	* @param side determines the clip side.
	* @return list containing outx and outy.
	*/
	private List<List<Float>> intersect(Edge edge, List<Float> outx, List<Float> outy, Edge boundaryPoints, int side) {
	float m = (float) ((edge.getY2() - edge.getY1())/(edge.getX2() - edge.getX1()));
	switch (side) {
		case 0:outx.add(boundaryPoints.getX2()); /** Right clipping **/
			   outy.add(edge.getY1() + ((boundaryPoints.getX2() - edge.getX1()) * m));
			   break;
		case 1:outx.add(edge.getX1() + ((boundaryPoints.getY2() - edge.getY1()) / m)); /** Top clipping **/
			   outy.add(boundaryPoints.getY2());
			   break;
		case 2:outx.add(boundaryPoints.getX1()); /** Left clipping **/
			   outy.add(edge.getY1() + ((boundaryPoints.getX1() - edge.getX1()) * m));
			   break;
		case 3:outx.add(edge.getX1() + ((boundaryPoints.getY1() - edge.getY1()) / m)); /** Botton clipping **/
			   outy.add(boundaryPoints.getY1());
			   break;
	}
	List<List<Float>> temp = new ArrayList<List<Float>>();
	temp.add(outx);
	temp.add(outy);
	return temp;
	}
}
