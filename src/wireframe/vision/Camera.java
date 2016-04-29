package wireframe.vision;

import wireframe.matrix.Matrix;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;

import java.util.List;

public class Camera {
    // Camera, viewport and up-vector
    private Vector PCam = new Vector(1, 0, 0, 1);
    private Vector PView = new Vector(0, 0, 0, 1);
    private Vector Vup = new Vector(0, 1, 0, 1);
    private Matrix MCam;

    public Camera() {
        MCam = new Matrix(4);
        MCam.setDiagonal(1);
        try {
            Vector Vz = PCam.minus(PView);
            Vector Vx = Vz.vecMultiple(Vup);
            Vx.addAxis(1);
            MCam.setColumn(0, Vx);
            MCam.setColumn(1, Vup);
            MCam.setColumn(2, Vz);
        } catch (VectorDimensionException | MatrixDimensionException e) {
            e.printStackTrace();
        }
    }

    public Matrix getWorldToViewPortMat(double zn) {
        // На входе -- точки из мировой СК,
        // На выходе -- точки в СК камеры с корректирующим W

        Matrix Mpsp = new Matrix(4);
        Mpsp.setDiagonal(new double[] {1, 1, 1, 0});
        Mpsp.set(2, 3, 1. / zn);
        Matrix Mtocam;
        try {
            return Mpsp.multiple(MCam);
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
