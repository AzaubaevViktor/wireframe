package wireframe.matrix;

import wireframe.matrix.errors.VectorDimensionException;

import java.util.ArrayList;
import java.util.List;

public class ParametricCurve {
    static final int N = 100; // На сколько частей делится кривая при подсчёте длинны
    private final Vector y;
    private final Vector x;
    private List<Double> t2l;
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
        t2l = new ArrayList<>();
        t2l.add(0.);
        Vector prev = calcT(0);
        Vector cur;
        for (int i = 1; i <= N; i += 1) {
            cur = calcT((double) i / N);
            try {
                l += cur.distance(prev);
                t2l.add(l);
            } catch (VectorDimensionException e) {
                e.printStackTrace();
            }
            prev = cur;
        }
        len = l;
    }

    public double getLen() {
        if (t2l == null) {
            calculateT2L();
        }
        return len;
    }

    private double getTByL(double l) {
        if (l < 0) { return 0; }
        if (l > getLen()) { return 1; }
        int leftTmulN = 0;
        int rightTmulN = N;

        while (rightTmulN - leftTmulN > 1) {
            int middleI = (leftTmulN + rightTmulN) / 2;
            if (l < t2l.get(middleI)) {
                rightTmulN = middleI;
            } else {
                leftTmulN = middleI;
            }
        }
        double ll = t2l.get(leftTmulN);
        double lr = t2l.get(rightTmulN);
        return ((l - ll) / (lr - ll) + leftTmulN) / (double) N;
    }

    public Vector calcL(double l) {
        if (t2l == null) {
            calculateT2L();
        }
        return calcT(getTByL(l));
    }
}
