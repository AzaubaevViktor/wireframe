package wireframe;

import wireframe.matrix.Matrix;
import wireframe.matrix.ParametricCurve;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BSpline {
    private List<Vector> points = new ArrayList<>();
    private List<ParametricCurve> curves = new ArrayList<>();
    static final Matrix M = new Matrix(new double[][]{
            new double[] {-1,  3, -3, 1},
            new double[] { 3, -6,  3, 0},
            new double[] {-3,  0,  3, 0},
            new double[] { 1,  4,  1, 0}
    }, 1./6.);

    private void generateStart6Points() {
        points.add(new Vector(-0.2, -0.1));
        points.add(new Vector(-0.1, 0.1));
        points.add(new Vector(0.1, 0.1));
        points.add(new Vector(0.1, -0.1));
        points.add(new Vector(0.1, -0.2));
        points.add(new Vector(0, -0.1));
        points.add(new Vector(0.2, -0.1));
        try {
            reCalcCurves();
        } catch (VectorDimensionException | MatrixDimensionException e) {
            e.printStackTrace();
            System.out.println("Да быть такого не может Оо");
        }
    }

    public BSpline(){
        generateStart6Points();
    }

    // CALCULATE SPLINE

    private Vector getXVector(int segmentIndex) {
        return new Vector(
                points.get(segmentIndex - 1).getX(),
                points.get(segmentIndex).getX(),
                points.get(segmentIndex + 1).getX(),
                points.get(segmentIndex + 2).getX()
        );
    }

    private Vector getYVector(int segmentIndex) {
        return new Vector(
                points.get(segmentIndex - 1).getY(),
                points.get(segmentIndex).getY(),
                points.get(segmentIndex + 1).getY(),
                points.get(segmentIndex + 2).getY()
        );
    }

    private ParametricCurve calcSegment(int segmentIndex) throws MatrixDimensionException, VectorDimensionException {
        return new ParametricCurve(
                M.multiple(getXVector(segmentIndex)),
                M.multiple(getYVector(segmentIndex))
        );
    }

    private void reCalcCurves() throws VectorDimensionException, MatrixDimensionException {
        // Сделать пересчёт некоторых кривых, а не всех
        curves.clear();
        for (int i = 1; i < points.size() - 3 + 1; ++i) {
            curves.add(calcSegment(i));
        }
    }

    public Vector calc(double t) {
        int segmentIndex = (int) Math.floor(t);
        return curves.get(segmentIndex).calc(t - segmentIndex);
    }

    // UTILS

    public int pointsCount() {
        return points.size();
    }

    // ITERATORS

    public Iterator<Vector> getPointsIterator() {
        return points.iterator();
    }
}
