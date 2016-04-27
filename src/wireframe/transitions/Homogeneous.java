package wireframe.transitions;

import wireframe.matrix.Matrix;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;


public class Homogeneous {
    static Matrix Translate3D(double tx, double ty, double tz) {
        Matrix m = new Matrix(4);
        m.setDiagonal(1);
        try {
            m.setColumn(3, new double[] {tx, ty, tz, 1});
            return m;
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Matrix Scale3D(double sx, double sy, double sz) {
        Matrix m = new Matrix(4);
        m.setDiagonal(new double[] {sx, sy, sz, 1});
        return m;
    }

    static Matrix RotateX(double phi) {
        Matrix m = new Matrix(4);
        m.setDiagonal(1);
        try {
            m.setRow(1, new double[] {0, Math.cos(phi), -Math.sin(phi), 0});
            m.setRow(2, new double[] {0, Math.sin(phi), Math.cos(phi), 0});
            return m;
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Matrix RotateY(double phi) {
        Matrix m = new Matrix(4);
        m.setDiagonal(1);
        try {
            m.setRow(0, new double[] {Math.cos(phi), 0, Math.sin(phi), 0});
            m.setRow(2, new double[] {-Math.sin(phi), 0, Math.cos(phi), 0});
            return m;
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Matrix RotateZ(double phi) {
        Matrix m = new Matrix(4);
        m.setDiagonal(1);
        try {
            m.setRow(0, new double[] {Math.cos(phi), -Math.sin(phi), 0, 0});
            m.setRow(1, new double[] {Math.sin(phi), Math.cos(phi), 0, 0});
            return m;
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
