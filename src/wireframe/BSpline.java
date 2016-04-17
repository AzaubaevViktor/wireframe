package wireframe;

import wireframe.matrix.Point2DI;
import wireframe.matrix.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BSpline {
    List<Vector> points = new ArrayList<>();

    private void generateStart4Points() {
        points.add(new Vector(new double[]{-0.1, -0.1}));
        points.add(new Vector(new double[]{-0.1, 0.1}));
        points.add(new Vector(new double[]{0.1, 0.1}));
        points.add(new Vector(new double[]{0.2, 0.}));
    }

    public BSpline() {
        generateStart4Points();
    }

    public Iterator<Vector> getPointsIterator() {
        return points.iterator();
    }
}
