import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    /**
	 * Draw a line from vertex (x0,y0) to vertex (x1,y1)
     *
     * You are to add the implementation here using only calls
	 * to setPixel();
	 */
	public void drawLine (int x0, int y0, int x1, int y1)
	{
		// Your implementation goes here
    }  
    
    
    /**
     * Draw a filled polygon. The polygon has n distinct vertices. The 
     * coordinates of the vertices making up the polygon are stored in the 
     * x and y arrays.  The ith vertex will have coordinate  (x[i], y[i])
     *
     * You are to add the implementation here using only calls
	 * to setPixel()
     */
	void drawPolygon(int n, int x[], int y[]) {
        Map<Integer, List<Bucket>> edgeTable = new HashMap<Integer, List<Bucket>>();

        int yMin = 0, yMax, xVal = 0, dx, dy, yVal = 0;
        int xMax = 0, xMin = 0;
        
        /*** Create edge table ***/
        
        for (int i = 0; i < n; i++) {
            if ((i + 1) == n) {
                yMin = (y[i] > y[0]) ? y[0] : y[i];
                yMax = (y[i] > y[0]) ? y[i] : y[0];
                xMax = (x[i] > x[0]) ? x[i] : x[0];
                xMin = (x[i] > x[0]) ? x[0] : x[i];
                xVal = (y[i] > y[0]) ? x[0] : x[i];
                dx = x[0] - x[i];
                dy = y[0] - y[i];
            } else {
                yMin = (y[i] > y[i + 1]) ? y[i + 1] : y[i];
                yMax = (y[i] > y[i + 1]) ? y[i] : y[i + 1];
                xMax = (x[i] > x[i + 1]) ? x[i] : x[i + 1];
                xMin = (x[i] > x[i + 1]) ? x[i + 1] : x[i];
                xVal = (y[i] > y[i + 1]) ? x[i + 1] : x[i];
                dx = x[i + 1] - x[i];
                dy = y[i + 1] - y[i];
            }

            /*** Check if line is horizontal ***/
            
            if (dy != 0) {
                Bucket newBucket = new Bucket(yMax, xVal, dx, dy, 0);
                if (edgeTable.get(yMin) != null) {
                    edgeTable.get(yMin).add(newBucket);
                } else {
                    List<Bucket> similarBuckets = new ArrayList<Bucket>();
                    similarBuckets.add(newBucket);
                    edgeTable.put(new Integer(yMin), similarBuckets);
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
                if (yVal == bucket.getyMax()) {
                    activeEdgeTable.remove(bucket);
                }
            }
            if (edgeTable.get(yVal) != null) {
                for (Bucket bucket : edgeTable.get(yVal)) {
                    activeEdgeTable.add(bucket);
                }
                edgeTable.remove(yVal);
            }

            Collections.sort(activeEdgeTable, new BucketSort());
            for (int i = 0; i < activeEdgeTable.size(); i++) {
                if (i % 2 == 0) {
                    for (int xCoord = activeEdgeTable.get(i).getX(); xCoord <= activeEdgeTable.get(i + 1).getX(); xCoord++) {
                        setPixel(xCoord, yVal);
                    }
                    yVal++;
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
        }
    }

}
