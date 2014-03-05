/* This class stored all the information needed in a bucket */

public class Bucket {
	
	private int yMax;
	private int x;
	private int dx;
	private int dy;
	private int sum;
	
	public Bucket(int yMax, int x, int dx, int dy, int sum) {
		this.yMax = yMax;
		this.x = x;
		this.dx = dx;
		this.dy = dy;
		this.sum = sum;
	}

	public int getyMax() {
		return yMax;
	}

	public void setyMax(int yMax) {
		this.yMax = yMax;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public double getSlope() {
        return (dy * 1.0) / dx;
    }
	
}
