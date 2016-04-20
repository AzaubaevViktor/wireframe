package wireframe.matrix;

import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;
import wireframe.pixel.Point2DI;

public class Vector {
    private double [] values;

    public Vector(int lenght) {
        values = new double[lenght];
    }

    public Vector(double[] values) {
        this.values = values.clone();
    }

    public Vector(Vector v) {
        apply(v);
    }

    public Vector(Point2DI p) {
        values = new double[] {p.getX(), p.getY()};
    }

    public void apply(Vector v) {
        values = v.values.clone();
    }

    public Vector(double x, double y) {
        values = new double[] {x, y};
    }

    public Vector(double x, double y, double z) {
        values = new double[] {x, y, z};
    }

    public Vector(double x, double y, double z, double a) {
        values = new double[] {x, y, z, a};
    }

    // UTILS

    public int length() {
        return values.length;
    }

    public Vector copy() {
        return new Vector(this);
    }

    public String toString() {
        String s = "";
        for (double value : values) {
            s += value + " ";
        }
        return s;
    }

    // GETTER + SETTER

    public Matrix getMatrix(boolean isRow) {
        Matrix m;
        try {
            if (isRow) {
                m = new Matrix(values.length, 1);
                m.setRow(0, values);
            } else {
                m = new Matrix(1, values.length);
                m.setColumn(0, values);
            }
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
            return null;
        }
        return m;
    }

    public Matrix getMatrix() {
        return getMatrix(false);
    }

    private boolean checkPos(int pos) {
        if ((0 > pos) || (pos > values.length)) {
            throw new IndexOutOfBoundsException("Pos(" + pos + ") не принадлежит [0;" + values.length + "]");
        }
        return true;
    }

    public double get(int pos) {
        checkPos(pos);
        return values[pos];
    }

    public double getX() { return get(0); }
    public double getY() { return get(1); }
    public double getZ() { return get(2); }
    public double getX2() { return get(2); }
    public double getY2() { return get(3); }

    public void set(int pos, double value) {
        checkPos(pos);
        values[pos] = value;
    }

    public void setX(double value) { set(0, value); }
    public void setY(double value) { set(1, value); }
    public void setZ(double value) { set(2, value); }
    public void setX2(double value) { set(0, value); }
    public void setY2(double value) { set(1, value); }

    public Point2DI getPoint2DI() {
        return new Point2DI((int) getX(), (int) getY());
    }

    // SCALAR

    public void multiple(double k) {
        for (int i = 0; i < values.length; ++i) {
            values[i] *= k;
        }
    }

    // VECTOR

    public Vector plus(Vector v, double k) throws VectorDimensionException {
        if (values.length != v.values.length) {
            throw new VectorDimensionException(this, v, "plus");
        }
        Vector result = new Vector(this);
        for (int i = 0; i < values.length; ++i) {
            result.values[i] += v.values[i] * k;
        }
        return result;
    }

    public Vector plus(Vector v) throws VectorDimensionException {
        return plus(v, 1);
    }

    public Vector move(Vector v) throws VectorDimensionException {
        return plus(v);
    }

    public Vector move(Vector v, double k) throws VectorDimensionException {
        return plus(v, k);
    }

    public double multipleSum(Vector v) throws VectorDimensionException {
        if (values.length != v.values.length) {
            throw new VectorDimensionException(this, v, "plus");
        }
        double sum = 0.;
        for (int i = 0; i < values.length; ++i) {
            sum += values[i] * v.values[i];
        }
        return sum;
    }

    public Matrix multiple(Matrix m) throws MatrixDimensionException {
        return this.getMatrix(true).multiple(m);
    }

    public double distance2(Vector v) throws VectorDimensionException {
        if (values.length != v.values.length) {
            throw new VectorDimensionException(this, v, "plus");
        }
        double d2 = 0;
        for (int i = 0; i < values.length; ++i) {
            d2 += (values[i] - v.values[i]) * (values[i] - v.values[i]);
        }
        return d2;
    }

    public double distance(Vector v) throws VectorDimensionException {
        return Math.sqrt(distance2(v));
    }

    public Vector divided(Vector v, double a) throws VectorDimensionException {
        // this -----a----- result ---1--- p
        if (values.length != v.values.length) {
            throw new VectorDimensionException(this, v, "plus");
        }

        Vector result = this.plus(v);
        result.multiple(a / (a + 1));

        return result;
    }

    public double calcPolynomial(double t) {
        double result = 0;
        for (int i = 0; i < values.length; ++i) {
            result += Math.pow(t, values.length - i - 1) * values[i];
        }
        return result;
    }
}
