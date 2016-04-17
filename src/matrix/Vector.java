package matrix;

import matrix.errors.MatrixDimensionException;
import matrix.errors.VectorDimensionException;

public class Vector {
    private double [] values;

    public Vector(int lenght) {
        values = new double[lenght];
    }

    public Vector(double[] values) {
        this.values = values.clone();
    }

    public Vector(Vector v) {
        values = v.values.clone();
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

    public void set(int pos, double value) {
        checkPos(pos);
        values[pos] = value;
    }

    public void setX(double value) { set(0, value); }
    public void setY(double value) { set(1, value); }
    public void setZ(double value) { set(2, value); }

    // SCALAR

    public void multiple(double k) {
        for (int i = 0; i < values.length; i++) {
            values[i] *= k;
        }
    }

    // VECTOR

    public Vector plus(Vector v) throws VectorDimensionException {
        if (values.length != v.values.length) {
            throw new VectorDimensionException(this, v, "plus");
        }
        Vector result = new Vector(this);
        for (int i = 0; i < values.length; i++) {
            result.values[i] += v.values[i];
        }
        return result;
    }

    public double multipleSum(Vector v) throws VectorDimensionException {
        if (values.length != v.values.length) {
            throw new VectorDimensionException(this, v, "plus");
        }
        double sum = 0.;
        for (int i = 0; i < values.length; i++) {
            sum += values[i] * v.values[i];
        }
        return sum;
    }

    public Matrix multiple(Matrix m) throws MatrixDimensionException {
        return this.getMatrix(true).multiple(m);
    }
}
