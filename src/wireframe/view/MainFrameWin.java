package wireframe.view;

import wireframe.Figure3D;
import wireframe.Model;
import wireframe.matrix.*;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.vision.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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

    ViewPortPanel(Model model) {
        super();
        this.model = model;
        setSize(640, 480);
        changeSize();
        setVisible(true);
    }

    private void changeSize() {
        Dimension size = getSize();
        changeSize(size.width, size.height);
    }

    private void changeSize(int width, int height) {
        setSize(width, height);
        centerP.setX(width);
        centerP.setY(height);
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
            if (first.getA() != 0.) {
                first.multiple(1 / first.getA());
            }
            Vector second = camPoints.get(link[1]);
            if (second.getA() != 0) {
                second.multiple(1 / second.getA());
            }
            g2d.drawLine(
                    (int) (first.getX() + centerP.getX()), (int) (first.getY() + centerP.getY()),
                    (int) (second.getX() + centerP.getX()), (int) (second.getY() + centerP.getY())
            );
        }

    }

}
