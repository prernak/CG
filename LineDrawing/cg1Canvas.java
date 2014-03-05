//
//  cg1Canvas.java
//  
//
//  Created by Joe Geigel on 1/21/10.
//  Modified by Prerna Keshari 09/19/2012 
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//
import java.lang.*;

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
	 *  You are to add the implementation here using only calls
	 * to setPixel();
	 */
    public void drawLine (int x0, int y0, int x1, int y1)
	{
		int dE, dNE, d;
		int dX = x1 - x0, dY = y1 - y0;
		
		if (dX == 0) {
			drawVertical(x0, y0, x1, y1);
			return;
		}
		
		float m = (float) dY/dX;
		
		/* Draw lines from left to right */
		if (x0 > x1){
			int temp = x0;
			x0 = x1;
			x1 = temp;
		}
		if (y0 > y1){
			int temp = y0;
			y0 = y1;
			y1 = temp;
		}
		
		if (m > 1){    /* Slope above 1 */
			dX = x1 - x0; dY = y1 - y0;
			dE = 2 * dX;
			dNE = 2 * (dX - dY);		
			d = dE - dY;
			
			int x = x0;
			for (int y = y0; y <= y1; ++y) {
				setPixel(x, y);
				if (d <= 0){
					 d += dE;
				} else {
					++x;
					d += dNE;
				}
			} 
			return;
		}
		
		if (m <= -1) {   /* Slope -1 and below  */
			dX = x1 - x0; dY = y1 - y0;
			dE = 2 * dX;
			dNE = 2 * (dX - dY);		
			d = dE - dY;
			int x = x0;
			for (int y = y1; y >= y0; --y) {
				 setPixel(x, y);
				 if (d <= 0){
					 d += dE;
				 } else {
					 ++x;
					 d += dNE;
				 }
			}
			return;
		}
		
		if (m >= 0 && m <= 1) {   /* Slope from 0 to 1 */
			dX = x1 - x0; dY = y1 - y0;
			dE = 2 * dY;
			dNE = 2 * (dY - dX);		
			d = dE - dX;
			int y = y0;
			for (int x = x0; x <= x1; ++x) {
				 setPixel(x, y);
				 if (d <= 0){
					 d += dE;
				 } else {
					 ++y;
					 d += dNE;
				 }
			 }
			return;
		}
		
		if (m < 0 && m > -1) {	/* Slope between 0 and -1 */
			dX = x1 - x0; dY = y1 - y0;
			dE = 2 * dY;
			dNE = 2 * (dY - dX);		
			d = dE - dX;
			int y = y1;
			for (int x = x0; x <= x1; ++x) {
				 setPixel(x, y);
				 if (d <= 0){
					 d += dE;
					 y--;
				 } else {
					 d += dNE;
				 }
			 }
			return;
		}	
		
    }    
	
    /**
     * Method to draw vertical lines.
     * 
     */
	private void drawVertical(int x0, int y0, int x1, int y1) {
		int x = x0;
		if (y0 > y1){
			int temp = x0;
			x0 = x1;
			x1 = temp;
			temp = y0;
			y0 = y1;
			y1 = temp;
		}
		for (int y = y0; y <= y1; ++y){
			setPixel(x, y);
		}
	}

}
