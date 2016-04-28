package wireframe;

import wireframe.matrix.Vector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Figure3D {
    public BSpline bSpline;
    public Color color;
    public List<Vector> points;

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
        double n = paramsInt[0];
        double m = paramsInt[1];
        double k = paramsInt[2];

        double[] paramsDouble = model.getParamsDouble();

        double a = paramsDouble[0];
        double b = paramsDouble[1];

        points = new ArrayList<>();

        for (double i = a; i < b; i += (b - a) / (n * k)) {

        }

    }
}
