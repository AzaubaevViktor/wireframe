package wireframe.vision;

import wireframe.matrix.Matrix;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;

public class Camera {
    // Camera, viewport and up-vector
    private Vector PCam = new Vector(0, 0, -10, 0);
    private Vector PView = new Vector(0, 0, 0, 0);
    private Vector Vn;
    private Vector Vup = new Vector(0, 1, 0, 0);
    private Matrix MCam;

    public Camera() {
        try {
            Vn = PCam.minus(PView).normalize();
        } catch (VectorDimensionException e) {
            e.printStackTrace();
        }
        initMCam();
    }

    private void initMCam() {
        MCam = new Matrix(4);
        MCam.setDiagonal(1);
        try {
            Vector Vx = Vn.vecMultiple3(Vup);
            Vx.addAxis(0);

            MCam.setRow(0, Vx);
            MCam.setRow(1, Vup);
            MCam.setRow(2, Vn);

            MCam.setColumn(3, new double[] {
                    - PCam.scalarMul(Vx),
                    - PCam.scalarMul(Vup),
                    - PCam.scalarMul(Vn),
                    0
            });
            System.out.println(MCam);
        } catch (VectorDimensionException | MatrixDimensionException e) {
            e.printStackTrace();
        }
    }

    public Matrix getWorldToViewPortMat(double zn) {
        Matrix Mpsp = new Matrix(4);
        Mpsp.setDiagonal(new double[] {1, 1, 1, 0});
        Mpsp.set(2, 3,  1. / zn);
        try {
            return Mpsp.multiple(MCam);
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
