/* This class stored all the information needed in a bucket */

public class Bucket {

    private double yMax;
    private double x;
    private double dx;
    private double dy;
    private double sum;

    public Bucket(double yMax, double x, double dx, double dy, float sum) {
        this.yMax = yMax;
        this.x = x;
        this.dx = dx;
        this.dy = dy;
        this.sum = sum;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSlope() {
        return (dy * 1.0) / dx;
    }

}
