package wireframe.pixel;


import wireframe.matrix.Vector;
import wireframe.matrix.errors.VectorDimensionException;

public class Point2DI {
    private int x = 0;
    private int y = 0;

    public Point2DI(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2DI(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public Point2DI(Point2DI p2di) {
        this.x = p2di.x;
        this.y = p2di.y;
    }

    public Point2DI(Vector v) throws VectorDimensionException {
        if (v.length() != 2) {
            throw new VectorDimensionException("Вектор должен быть длинны 2, а он длинны " + v.length());
        }
        this.x = (int) v.getX();
        this.y = (int) v.getY();
    }

    // GETTER + SETTER

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    // UTILS

    public double distance(Point2DI p) {
        return Math.sqrt(distance2(p));
    }

    public int distance2(Point2DI p) {
        int x = this.x - p.x;
        int y = this.y - p.y;
        return x*x + y*y;
    }

    public Point2DI divided(Point2DI p, double a) {
        // this -----a----- result ---1--- p
        return new Point2DI(
                ((this.x + p.x) * a / (a + 1)),
                ((this.y + p.y) * a / (a + 1))
        );
    }

    public String toString() {
        return "Point(" + x + "; " + y + ")";
    }

}
