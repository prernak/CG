/** This class does the model transformations **/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Jama.Matrix;


public class ModelTransform {
	
	public Map<Integer, List<List<Float>>> transformFigure(Map<Integer, List<List<Float>>> polygon, Matrix transformationMatrix) {
		Map<Integer, List<List<Float>>> resultPoly = new HashMap<Integer, List<List<Float>>>();
        for (int i = 0; i < 4; i++) {
        	List<Float> vx = new ArrayList<Float>();
        	List<Float> vy = new ArrayList<Float>();
            List<List<Float>> poly = polygon.get(i);
            for (int j = 0; j < poly.get(0).size(); j++) {
                double[][] m2 = { { (poly.get(0).get(j)) },
                        { (poly.get(1).get(j)) }, { 1 } };
                Matrix vertex = new Matrix(m2);
                Matrix output = transformationMatrix.times(vertex);
                vx.add((float) output.get(0, 0));
                vy.add((float) output.get(1, 0));
            }
            List<List<Float>> temp = new ArrayList<List<Float>>();
            temp.add(vx);
            temp.add(vy);
            resultPoly.put(i, temp);
        }
        return resultPoly;
    }
	
	public boolean isIdentity(Matrix matrix) {
		if (matrix.get(0, 0) == 1 && matrix.get(1, 1) == 1 && matrix.get(2, 2) == 1)
			return true;
		return false;
	}

}
