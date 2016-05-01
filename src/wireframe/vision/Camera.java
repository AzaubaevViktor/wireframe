package wireframe.vision;

import wireframe.matrix.Matrix;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;

import java.util.List;

public class Camera {
    // Camera, viewport and up-vector
    private Vector PCam = new Vector(0, 0, 10, 0);
    private Vector PView = new Vector(0, 0, 0, 0);
    private Vector Vup = new Vector(0, 1, 0, 0);
    private Matrix MCam;

    public Camera() {
        MCam = new Matrix(4);
        MCam.setDiagonal(1);
        try {
            Vector Vn = PCam.minus(PView).normalize();
            Vector Vx = Vn.vecMultiple(Vup);
            Vx.addAxis(0);

            MCam.setColumn(0, Vx);
            MCam.setColumn(1, Vup);
            MCam.setColumn(2, Vn);

            MCam.setRow(3, new double[] {
                    - PCam.scalarMul(Vx),
                    - PCam.scalarMul(Vup),
                    - PCam.scalarMul(Vn),
                    1
            });
            MCam = MCam.transpone();
        } catch (VectorDimensionException | MatrixDimensionException e) {
            e.printStackTrace();
        }
    }

    public Matrix getWorldToViewPortMat(double zn) {
        Matrix Mpsp = new Matrix(4);
        Mpsp.setDiagonal(new double[] {1, 1, 1, 1});
        Mpsp.set(2, 3, - 1. / zn);
        try {
            return Mpsp.multiple(MCam);
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
