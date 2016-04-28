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
    private List<Double> lBySegments;
    private double len;

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

    private void reCalc(int segmentIFrom, int segmentITo) {
        try {
            reCalcCurves(segmentIFrom, segmentITo);
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
        curves.clear();
        for (int segmentI = 1; segmentI < points.size() - 3 + 1; ++segmentI) {
            curves.add(calcSegment(segmentI));
        }
        reCalcLen();
    }

    private void reCalcCurves(int segmentIFrom, int segmentITo) throws VectorDimensionException, MatrixDimensionException {
        if (segmentITo > points.size() - 3 + 1) {
            segmentITo = points.size() - 3 + 1;
        }

        if (segmentIFrom < 1) {
            segmentIFrom = 1;
        }

        for (int i = segmentIFrom; i < segmentITo; ++i) {
            ParametricCurve curve = curves.get(i - 1);
            curve.apply(
                    M.multiple(getXVector(i)),
                    M.multiple(getYVector(i))
            );
        }

        reCalcLen();
    }

    private void reCalcLen() {
        len = 0;
        lBySegments = new ArrayList<>();
        lBySegments.add(0.);

        for (ParametricCurve curve : curves) {
            len += curve.getLen();
            lBySegments.add(len);
        }
    }

    public Vector calcT(double t) {
        int segmentIndex = (int) Math.floor(t);
        return curves.get(segmentIndex).calcT(t - segmentIndex);
    }

    private int getSegmentByL(double l) {
        if (l < 0) { return 0; }
        if (l > getLen()) { return curves.size(); }

        int leftI = 0;
        int rightI = curves.size();

        while (rightI - leftI > 1) {
            int middleI = (leftI + rightI) / 2;
            if (l < lBySegments.get(middleI)) {
                rightI = middleI;
            } else {
                leftI = middleI;
            }
        }
        return leftI;
    }

    public Vector calcL(double l) {
        int segmentI = getSegmentByL(l);
        return curves.get(segmentI).calcL(l - lBySegments.get(segmentI));
    }

    // GETTER + SETTER

    public Vector[] getEnvRect() {
        return new Vector[]{leftUp, rightDown};
    }

    public double getLen() {
        return len;
    }

    // UTILS

    public int pointsCount() {
        return points.size();
    }

    public void movePoint(int index, Vector value) {
        this.points.get(index).apply(value);

        reCalc(index - 2, index + 2);
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
