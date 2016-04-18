package wireframe.view;

import wireframe.BSpline;
import wireframe.matrix.Matrix;
import wireframe.matrix.Point2DI;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.VectorDimensionException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;

public class BSplineWin extends JDialog {
    private GraphViewPanel graphViewPanel = new GraphViewPanel();

    BSplineWin() {
        this.setTitle("B-сплайн");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        initGraphViewPanel();

        pack();
        this.setSize(640, 480);
    }

    private void initGraphViewPanel() {
        graphViewPanel.setBSpline(new BSpline());
        this.add(graphViewPanel);
    }
}

class GraphViewPanel extends JPanel {
    private BufferedImage img;
    private BSpline bSpline;
    private double drawK = 0.;
    private Vector centerV = new Vector(0, 0);
    private Point2DI centerP;
    private int pointRectSide = 6;
    private int middleCircleSize = 4;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();

        drawK = Math.min(size.width, size.height);
        pointRectSide = (int) Math.max(drawK / 300, 6);
        middleCircleSize = pointRectSide * 2 / 3;

        centerV.setX(size.width / 2);
        centerV.setY(size.height / 2);
        centerP = centerV.getPoint2DI();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, size.width, size.height);

        g2d.setColor(Color.WHITE);
        g2d.drawLine(0, centerP.getY(), size.width, centerP.getY());
        g2d.drawLine(centerP.getX(), 0, centerP.getX(), size.height);
        drawBSpline(g2d);
    }

    private Point2DI coordToPoint(Vector v) {
        Vector vTemp = new Vector(v);
        vTemp.multiple(drawK);
        try {
            vTemp = vTemp.move(centerV);
        } catch (VectorDimensionException e) {
            e.printStackTrace();
        }
        return vTemp.getPoint2DI();
    }

    private void drawLine(Graphics2D g2d, Point2DI p1, Point2DI p2) {
        g2d.drawLine(
                p1.getX(), p1.getY(),
                p2.getX(), p2.getY()
        );
    }

    private void drawPointRect(Graphics2D g2d, Point2DI p) {
        g2d.drawRect(
                p.getX() - pointRectSide / 2, p.getY() - pointRectSide / 2,
                pointRectSide, pointRectSide
        );
    }

    private void drawMiddleCircle(Graphics2D g2d, Point2DI p) {
        g2d.drawArc(p.getX() - middleCircleSize / 2, p.getY() - middleCircleSize / 2,
                middleCircleSize, middleCircleSize, 0, 360);
    }

    private void drawBSpline(Graphics2D g2d) {
        Iterator<Vector> vectorIterator = bSpline.getPointsIterator();
        java.util.List<Point2DI> points = new ArrayList<>();

        g2d.setColor(Color.RED);

        for (; vectorIterator.hasNext();) {
            Point2DI point = coordToPoint(vectorIterator.next());
            drawPointRect(g2d, point);
            points.add(point);

        }

        Iterator<Point2DI> pointsIterator = points.iterator();
        Point2DI prevPoint = pointsIterator.next();
        for (; pointsIterator.hasNext();) {
            Point2DI point = pointsIterator.next();
            Point2DI middlePoint = prevPoint.divided(point, 1);

            drawLine(g2d, prevPoint, point);
            drawMiddleCircle(g2d, middlePoint);

            prevPoint = point;
        }

        g2d.setColor(Color.blue);

        for(double t = 0; t < bSpline.pointsCount() - 3; t += 0.01) {
            Point2DI point = coordToPoint(bSpline.calc(t));
            g2d.drawLine(point.getX(), point.getY(), point.getX(), point.getY());
        }
    }

    public void setBSpline(BSpline bSpline) {
        this.bSpline = bSpline;
    }
}
