package wireframe.view;

import wireframe.BSpline;
import wireframe.pixel.Point2DI;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.VectorDimensionException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

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
    private BSpline bSplineOrigin;
    private double drawK = 0.;
    private Point2DI size = new Point2DI(0, 0);
    private Vector centerV = new Vector(0, 0);
    private Point2DI centerP;
    private final int MIN_POINT_RECT_SIZE = 9;
    private int pointRectSide = 0;
    private int middleCircleSize = 0;

    private int curMovingPointIndex = -1;

    private List<Point2DI> points;
    private List<Point2DI> middlePoints;

    GraphViewPanel() {
        super();
        changeSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        changeSize();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, size.getX(), size.getY());

        g2d.setColor(Color.WHITE);
        g2d.drawLine(0, centerP.getY(), size.getX(), centerP.getY());
        g2d.drawLine(centerP.getX(), 0, centerP.getX(), size.getY());
        reCalcObjects();
        drawBSpline(g2d);

        mouseListenerInit();
        componentListenerInit();
    }

    // GRAPHIC HELP

    private void componentListenerInit() {
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                changeSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    private void changeSize(int width, int height) {
        if ((size.getX() == width) && (size.getY() == height)) {
            return;
        }
        size.setX(width);
        size.setY(height);
        drawK = Math.min(width, height);
        pointRectSide = (int) Math.max(drawK / 300, MIN_POINT_RECT_SIZE);
        middleCircleSize = pointRectSide * 2 / 3;

        centerV.setX(width / 2);
        centerV.setY(height / 2);
        centerP = centerV.getPoint2DI();
    }

    private void changeSize() {
        Dimension size = getSize();
        changeSize(size.width, size.height);
    }

    private void mouseListenerInit() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (curMovingPointIndex != -1) {
                    return;
                }

                Point2DI p = new Point2DI(e.getX(), e.getY());
                curMovingPointIndex = getPointIndex(points, pointRectSide, pointRectSide, p);
                if (curMovingPointIndex != -1) {
                    if (e.getButton() == 3) {
                        bSpline.deletePoint(curMovingPointIndex);
                        curMovingPointIndex = -1;
                        reCalcObjects();
                    }
                } else {
                    int addingIndex = getPointIndex(middlePoints,
                            middleCircleSize,
                            middleCircleSize,
                            p) + 1;
                    if (addingIndex != 0) {
                        bSpline.addPoint(addingIndex, pointToValue(p.getX(), p.getY()));
                        curMovingPointIndex = addingIndex;
                    }
                }
                repaint();
            }
            public void mouseReleased(MouseEvent e) {
                curMovingPointIndex = -1;
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (curMovingPointIndex != -1) {
                    bSpline.movePoint(curMovingPointIndex, pointToValue(e.getX(), e.getY()));
                }
                repaint();
            }
        });
    }

    public int getPointIndex(List<Point2DI> list, int width, int height, Point2DI curPoint) {
        for (int i = 0; i < list.size(); ++i) {
            Point2DI lPoint = list.get(i);
            if (
                    ((lPoint.getX() - width / 2 - 1) < curPoint.getX())
                    && (curPoint.getX()  < (lPoint.getX() + width / 2 + 1))
                    && ((lPoint.getY() - height / 2 - 1) < curPoint.getY())
                    && (curPoint.getY() < (lPoint.getY() + height / 2 + 1))
                    ) {
                return i;
            }
        }
        return -1;
    }

    private Vector valueToPoint(Vector v) {
        Vector vTemp = new Vector(v);
        vTemp.multiple(drawK);
        try {
            vTemp = vTemp.move(centerV);
            return vTemp;
        } catch (VectorDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Vector pointToValue(int x, int y) {
        try {
            Vector pTemp = (new Vector(x, y)).move(centerV, -1);
            pTemp.multiple(1 / drawK);
            return pTemp;
        } catch (VectorDimensionException e) {
            e.printStackTrace();
        }
        return null;
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
        g2d.setColor(Color.red);

        Iterator<Point2DI> pointsIterator = points.iterator();

        Point2DI prevPoint = pointsIterator.next();
        drawPointRect(g2d, prevPoint);


        for (; pointsIterator.hasNext();) {
            Point2DI point = pointsIterator.next();

            drawPointRect(g2d, point);
            drawLine(g2d, prevPoint, point);

            prevPoint = point;
        }

        for (Point2DI mPoint: middlePoints) {
            drawMiddleCircle(g2d, mPoint);
        }

        g2d.setColor(Color.blue);

        for (double t = 0; t < bSpline.pointsCount() - 3; t += .01) {
            Point2DI point = valueToPoint(bSpline.calc(t)).getPoint2DI();
            g2d.drawLine(point.getX(), point.getY(), point.getX(), point.getY());
        }

    }

    public void reCalcObjects() {
        points = new ArrayList<>();
        middlePoints = new ArrayList<>();

        for (Iterator<Vector> vectorIterator = bSpline.getPointsIterator(); vectorIterator.hasNext();) {
            points.add(valueToPoint(vectorIterator.next()).getPoint2DI());
        }

        Iterator<Point2DI> pointsIterator = points.iterator();
        Point2DI prevPoint = pointsIterator.next();
        for (; pointsIterator.hasNext();) {
            Point2DI point = pointsIterator.next();
            middlePoints.add(prevPoint.divided(point, 1));
            prevPoint = point;
        }
    }

    public void setBSpline(BSpline bSpline) {
        this.bSplineOrigin = bSpline;
        this.bSpline = new BSpline(this.bSplineOrigin);
        reCalcObjects();
    }
}
