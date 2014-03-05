/** This class is used for clipping the polygons **/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Clipper {
	
	public Clipper () {
		
	}
	
    public Map<Integer, List<List<Float>>> clip(Map<Integer, List<List<Float>>> polygon, Edge clipWindow) {
    	Map<Integer, List<List<Float>>> polygonC = new HashMap<Integer, List<List<Float>>>();
    	List<Float> inx = new ArrayList<Float>();
        List<Float> iny = new ArrayList<Float>();
        Clipper clipper = new Clipper();
    	for (int i = 0; i < 4; i++) {
            List<List<Float>> poly = polygon.get(i);
            inx.removeAll(inx);
            inx.addAll(poly.get(0));
            iny.removeAll(iny);
            iny.addAll(poly.get(1));
            List<Float> outx = new ArrayList<Float>();
            List<Float> outy = new ArrayList<Float>();
            List<List<Float>> temp = clipper.clipPolygon(inx.size(), inx, iny,
                    outx, outy, clipWindow);
            polygonC.put(i, temp);
        }
    	return polygonC;
    }
	
	private List<List<Float>> clipPolygon(int in, List<Float> inx, List<Float> iny, List<Float> outx, 
			List<Float> outy, Edge clipWindow) {
		for (int i = 0; i < 4; i++) {
			List<Edge> edges = createEdges(in, inx, iny);
		    List<Float> temp;
		    outx.removeAll(outx);
		    outy.removeAll(outy);
		    for (Edge eachEdge:edges){
		    	if (isInside(eachEdge.getX2(), eachEdge.getY2(), clipWindow, i)) {
		    		if (isInside(eachEdge.getX1(), eachEdge.getY1(), clipWindow, i)) { /** If V1 and V2 both are inside **/
		    			outx.add(eachEdge.getX2());
		    			outy.add(eachEdge.getY2());
		    		}else {
		    			temp = intersect(eachEdge, clipWindow, i); /** If V2 is inside and V1 is outside **/
		    			outx.add(temp.get(0));
		    			outy.add(temp.get(1));
		    			outx.add(eachEdge.getX2());
		    			outy.add(eachEdge.getY2());
		    		}
		    	}else {
		    		if (isInside(eachEdge.getX1(), eachEdge.getY1(), clipWindow, i)) { 
		    			temp = intersect(eachEdge, clipWindow, i); /** If V2 is outside and V1 is inside **/
		    			outx.add(temp.get(0));
		    			outy.add(temp.get(1));
		    		}
		    	}
		    }
		    inx.removeAll(inx);
		    iny.removeAll(iny);
		    inx.addAll(outx);
		    iny.addAll(outy);
		    in = inx.size();
		}
		List<List<Float>> temp = new ArrayList<List<Float>>();
		List<Float> tempx = new ArrayList<Float>();
    	List<Float> tempy = new ArrayList<Float>();
    	tempx.addAll(inx);
    	tempy.addAll(iny);
    	temp.add(tempx);
		temp.add(tempy);
		return temp;  // should return number of verticies of poly after clip
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
		case 0:return (x <= boundaryPoints.getX2());
		case 1:return (y <= boundaryPoints.getY2());
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
	private List<Float> intersect(Edge edge, Edge boundaryPoints, int side) {
	float m = (float) ((edge.getY2() - edge.getY1())/(edge.getX2() - edge.getX1()));
	List<Float> temp = new ArrayList<Float>();
	switch (side) {
		case 0:temp.add(boundaryPoints.getX2()); /** Right clipping **/
			   temp.add(edge.getY1() + ((boundaryPoints.getX2() - edge.getX1()) * m));
			   break;
		case 1:temp.add(edge.getX1() + ((boundaryPoints.getY2() - edge.getY1()) / m)); /** Top clipping **/
			   temp.add(boundaryPoints.getY2());
			   break;
		case 2:temp.add(boundaryPoints.getX1()); /** Left clipping **/
			   temp.add(edge.getY1() + ((boundaryPoints.getX1() - edge.getX1()) * m));
			   break;
		case 3:temp.add(edge.getX1() + ((boundaryPoints.getY1() - edge.getY1()) / m)); /** Botton clipping **/
			   temp.add(boundaryPoints.getY1());
			   break;
	}
	return temp;
	}
	
	public boolean isClipped(Edge clipWindow) {
		if (clipWindow.getX1() > 100 || clipWindow.getY1() > 100 || clipWindow.getX2() < 100 || clipWindow.getY2() < 100)
			return true;
		return false;
	}
}
