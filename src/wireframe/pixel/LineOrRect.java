package wireframe.pixel;

import wireframe.matrix.Vector;

public class LineOrRect {
    public Point2DI leftUp;
    public Point2DI rightDown;

    public LineOrRect(Point2DI leftUp, Point2DI rightDown) {
        this.leftUp = new Point2DI(leftUp);
        this.rightDown = new Point2DI(rightDown);
    }

    public LineOrRect(Vector leftUp, Vector rightDown) {
        this.leftUp = leftUp.getPoint2DI();
        this.rightDown = rightDown.getPoint2DI();
    }

    public LineOrRect(Vector center, double width, double height) {
        this.leftUp = new Point2DI(center.getX() - width / 2, center.getY() - height / 2);
        this.rightDown = new Point2DI(center.getX() + width / 2, center.getY() + height / 2);
    }
}
