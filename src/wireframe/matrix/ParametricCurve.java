package wireframe.matrix;

import wireframe.matrix.errors.VectorDimensionException;

public class ParametricCurve {
    private final Vector y;
    private final Vector x;

    public ParametricCurve(Vector x, Vector y) throws VectorDimensionException {
        if (x.length() != y.length()) {
            throw new VectorDimensionException(x, y, "Подсчёт параметрической кривой");
        }
        this.x = x;
        this.y = y;
    }

    public Vector calc(double t) {
        return new Vector(
                x.calcPolynomial(t),
                y.calcPolynomial(t)
        );
    }
}
