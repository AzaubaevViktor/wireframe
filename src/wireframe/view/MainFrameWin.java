package wireframe.view;

import wireframe.Figure3D;
import wireframe.Model;
import wireframe.matrix.*;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;
import wireframe.pixel.Point2DI;
import wireframe.vision.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrameWin extends MainFrame {
    private final BSplineWin bsplineWin;
    private final AboutWin about;
    private final Model model;

    ViewPortPanel viewPortPanel;

    public MainFrameWin(int x, int y, String title, Model model) {
        super(x, y, title);

        about = new AboutWin();
        bsplineWin = new BSplineWin(model);
        this.model = model;

        viewPortPanel = new ViewPortPanel(model);

        setContentPane(viewPortPanel);
        pack();

        try {
            createAllMenus();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        setSize(x, y);

        setVisible(true);
    }

    private void createAllMenus() throws NoSuchMethodException {
        addSubMenu("Menu", KeyEvent.VK_M);
        addMenuItem("Menu/wireframe.BSpline", "Lol", KeyEvent.VK_B, "openBSplineWin");
        addSubMenu("About", KeyEvent.VK_A);
        addMenuItem("About/About", "Azaz", KeyEvent.VK_A, "showAbout");
    }

    public void openBSplineWin() {
        bsplineWin.setVisible(true);
    }

    public void showAbout() { about.setVisible(true); }
}

class ViewPortPanel extends JPanel {
    private final Model model;
    private Vector centerP = new Vector(0, 0);
    private Point2DI lastMouse;
    private Vector maxCrop = new Vector(3), minCrop = new Vector(3);

    ViewPortPanel(Model model) {
        super();
        this.model = model;
        setSize(640, 480);
        changeSize();
        mouseListenerInit();
        componentListenerInit();
        setVisible(true);
    }

    private void mouseListenerInit() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastMouse = new Point2DI(e.getX(), e.getY());
            }
            public void mouseReleased(MouseEvent e) {
                lastMouse = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point2DI curMouse = new Point2DI(e.getX(), e.getY());

                model.getFigure(0).rotateX(- (curMouse.getY() - lastMouse.getY()) / 100.);
                model.getFigure(0).rotateY((curMouse.getX() - lastMouse.getX()) / 100.);

                lastMouse = curMouse;
                repaint();
            }
        });
        addMouseWheelListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                zoom(e);
                repaint();
            }

        });
    }

    private void zoom(MouseWheelEvent e) {
        model.zoom(e.getPreciseWheelRotation());
        minCrop.setZ(model.getZn());
        maxCrop.setZ(model.getZf());
    }

    private void componentListenerInit() {
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                changeSize();
                repaint();
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

    private void changeSize() {
        Dimension size = getSize();
        changeSize(size.width, size.height);
    }

    private void changeSize(int width, int height) {
        setSize(width, height);
        centerP.setX(width / 2);
        centerP.setY(height / 2);
        // ПОПРАВИТЬ
        minCrop.setX(-1000);
        minCrop.setY(-1000 - centerP.getY());
        maxCrop.setX(1000 - centerP.getX());
        maxCrop.setY(1000 - centerP.getY());
    }

    // CROP

    private boolean cropVector(Vector first, Vector second, int pos, double left, double right) {
        double c1 = first.get(pos);
        double c2 = second.get(pos);
        if ((c1 < left) && (c2 < left)) return false;
        if ((c1 > right) && (c2 > right)) return false;
        try {
            if ((c1 < left) && (left < c2)) first.apply(first.divided(second, c2 - left, left - c1));
            if ((c1 > right) && (right > c2)) second.apply(second.divided(first, c2 - right, right - c1));
            return true;
        } catch (VectorDimensionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void cropAndDrawLine(Graphics2D g2d, Vector first, Vector second) {
        int z1 = (int) first.getZ();
        if (Math.abs(first.getA()) > 0.00000001) {
            first.multiple(1 / first.getA());
        }
        first.setZ(z1);

        int z2 = (int) second.getZ();
        if (Math.abs(second.getA()) > 0.00000001) {
            second.multiple(1 / second.getA());
        }
        second.setZ(z2);

        if (/*cropVector(first, second, 0, minCrop.getX() , maxCrop.getX())
                && cropVector(first, second, 1, minCrop.getY(), maxCrop.getY())
                && cropVector(first, second, 2, minCrop.getZ(), maxCrop.getZ())*/true) {
            g2d.setColor(new Color(Math.abs(z2 - z1) * 6 % 255, 0, 0));
            int x1 = (int) (first.getX() + centerP.getX());
            int y1 = (int) (first.getY() + centerP.getY());
            int x2 = (int) (second.getX() + centerP.getX());
            int y2 = (int) (second.getY() + centerP.getY());
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    // DRAWING ALL

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Figure3D figure3D = model.getFigure(0);
        Camera camera = model.camera;

        Matrix figureToWorldMat = figure3D.getToWorldMatrix();

        figure3D.calcPoints(model);
        List<Vector> points = figure3D.getPoints();
        List<int[]> links = figure3D.getLinks();

        Matrix worldToViewPortMat = camera.getWorldToViewPortMat(model.getZn());

        Matrix figureToCamMat = null;

        try {
            figureToCamMat = worldToViewPortMat.multiple(figureToWorldMat);
//            figureToCamMat = figureToWorldMat;
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
            return;
        }

        List<Vector> camPoints = new ArrayList<>();
        try {
            for (Vector point: points) {
                camPoints.add(figureToCamMat.multiple(point));
            }
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }

        for (int[] link: links) {
            cropAndDrawLine(g2d, camPoints.get(link[0]), camPoints.get(link[1]));
        }

        g2d.drawString("Zn:" + model.getZn(), 0, 20);
    }

}
