package wireframe.view;

import wireframe.Figure3D;
import wireframe.Model;
import wireframe.matrix.*;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
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
                model.zoom(e.getPreciseWheelRotation());
                repaint();
            }

        });
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
    }

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
            Vector first = camPoints.get(link[0]);
            int z1 = (int) first.getZ();
            if (Math.abs(first.getA()) > 0.00000001) {
                first.multiple(1 / first.getA());
            }

            Vector second = camPoints.get(link[1]);
            int z2 = (int) second.getZ();
            if (Math.abs(second.getA()) > 0.00000001) {
                second.multiple(1 / second.getA());
            }

            int x1 = (int) (first.getX() + centerP.getX());
            int y1 = (int) (first.getY() + centerP.getY());
            int x2 = (int) (second.getX() + centerP.getX());
            int y2 = (int) (second.getY() + centerP.getY());
//            int z1 = (int) (first.getZ() * )

            Dimension size = getSize();

            if ((x1 > 0) && (x1 < size.width)
                    && (x2 > 0) && (x2 < size.width)
                    && (y1 > 0) && (y1 < size.height)
                    && (y2 > 0) && (y2 < size.height)) {
                g2d.setColor(new Color(Math.abs(z2 - z1) * 6 % 255, 0, 0));
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }

}
