package wireframe;

import wireframe.matrix.Matrix;
import wireframe.matrix.ParametricCurve;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BSpline {
    private List<Vector> points = new ArrayList<>();
    private List<ParametricCurve> curves = new ArrayList<>();

    private Vector leftUp, rightDown;

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
    }

    public BSpline(){
        generateStart6Points();
        reCalc();
    }

    public BSpline(BSpline bSpline) {
        this.points.addAll(bSpline.points.stream().map(Vector::copy).collect(Collectors.toList()));
        reCalc();
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

    private void reCalc() {
        try {
            reCalcCurves();
        } catch (VectorDimensionException | MatrixDimensionException e) {
            e.printStackTrace();
        }
        recalcSize();
    }

    private void recalcSize() {
        leftUp = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        rightDown = new Vector(Double.MIN_VALUE, Double.MIN_VALUE);

        for (Vector point: points) {
            if (point.getX() < leftUp.getX()) {
                leftUp.setX(point.getX());
            }

            if (point.getY() < leftUp.getY()) {
                leftUp.setY(point.getX());
            }

            if (point.getX() > rightDown.getX()) {
                rightDown.setX(point.getX());
            }

            if (point.getY() > rightDown.getY()) {
                rightDown.setY(point.getY());
            }
        }
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

    // GETTER + SETTER

    public Vector[] getSize() {
        return new Vector[]{leftUp, rightDown};
    }

    // UTILS

    public int pointsCount() {
        return points.size();
    }

    public void movePoint(int index, Vector value) {
        this.points.get(index).apply(value);

        reCalc();
    }

    public void addPoint(int index, Vector value) {
        points.add(index, value.copy());
        reCalc();
    }

    public void deletePoint(int index) {
        points.remove(index);
        reCalc();
    }

    // ITERATORS

    public Iterator<Vector> getPointsIterator() {
        return points.iterator();
    }
}
