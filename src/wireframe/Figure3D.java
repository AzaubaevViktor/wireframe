package wireframe;

import wireframe.matrix.Vector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Figure3D {
    public BSpline bSpline;
    public Color color;
    public Vector[][] points3D;

    public Figure3D() {
        bSpline = new BSpline();
        color = new Color(0, 150, 150);
    }

    public Figure3D(Figure3D figure3D) {
        bSpline = figure3D.bSpline;
        color = figure3D.color;
    }

    public void calcPoints(Model model) {
        int[] paramsInt = model.getIntParams();
        int n = paramsInt[0];
        int m = paramsInt[1];
        int k = paramsInt[2];

        double[] paramsDouble = model.getParamsDouble();

        double a = paramsDouble[0];
        double b = paramsDouble[1];
        double c = paramsDouble[2];
        double d = paramsDouble[3];

        points3D = new Vector[n * k][m];

        for (int u = 0; u < n * k; ++u) {
            double curL = u * (b - a) / (double) (n * k) + a;
            curL *= bSpline.getLen();

            Vector point2D = bSpline.calcL(curL);
            for (int v = 0; v < m; ++v) {
                Vector point3D = new Vector(3);
                double phi = v * (d - c) / (double) m + c;
                point3D.setX(point2D.getY() * Math.cos(phi));
                point3D.setY(point2D.getY() * Math.sin(phi));
                point3D.setZ(point2D.getX());
                points3D[u][v] = point3D;
            }
        }
    }
}
