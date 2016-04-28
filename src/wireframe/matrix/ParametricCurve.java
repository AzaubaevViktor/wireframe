package wireframe.matrix;

import wireframe.matrix.errors.VectorDimensionException;

import java.util.ArrayList;
import java.util.List;

public class ParametricCurve {
    static final int N = 100; // На сколько частей делится кривая при подсчёте длинны
    private final Vector y;
    private final Vector x;
    private List<Double> tByL;
    private List<Vector> pointByIndex;
    private double len = 0;

    public ParametricCurve(Vector x, Vector y) throws VectorDimensionException {
        if (x.length() != y.length()) {
            throw new VectorDimensionException(x, y, "Подсчёт параметрической кривой");
        }
        this.x = x;
        this.y = y;
    }

    public Vector calcT(double t) {
        return new Vector(
                x.calcPolynomial(t),
                y.calcPolynomial(t)
        );
    }

    public void calculateT2L() {
        double l = 0.;
        tByL = new ArrayList<>();
        pointByIndex = new ArrayList<>();

        Vector prev = calcT(0);
        tByL.add(0.);
        pointByIndex.add(prev);

        Vector cur;
        for (int i = 1; i <= N; i += 1) {
            cur = calcT((double) i / N);
            try {
                l += cur.distance(prev);
                tByL.add(l);
                pointByIndex.add(cur);
            } catch (VectorDimensionException e) {
                e.printStackTrace();
            }
            prev = cur;
        }
        len = l;
    }

    public double getLen() {
        if (tByL == null) {
            calculateT2L();
        }
        return len;
    }

    private int getLeftIndexByL(double l) {
        if (l < 0) { return 0; }
        if (l > getLen()) { return 1; }
        int leftTmulN = 0;
        int rightTmulN = N;

        while (rightTmulN - leftTmulN > 1) {
            int middleI = (leftTmulN + rightTmulN) / 2;
            if (l < tByL.get(middleI)) {
                rightTmulN = middleI;
            } else {
                leftTmulN = middleI;
            }
        }
        return leftTmulN;
    }

    public Vector calcL(double l) {
        if (tByL == null) {
            calculateT2L();
        }
        int leftTmulN = getLeftIndexByL(l);
        double ll = tByL.get(leftTmulN);
        double lr = tByL.get(leftTmulN + 1);
        try {
            return pointByIndex.get(leftTmulN).divided(
                    pointByIndex.get(leftTmulN + 1),
                    (l - ll) / (lr - l)
            );
        } catch (VectorDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
