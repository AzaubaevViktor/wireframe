package wireframe;

import wireframe.matrix.Matrix;
import wireframe.matrix.Vector;
import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.transitions.Homogeneous;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Figure3D {
    public BSpline bSpline;
    public Color color;
    public Vector[][] points3D;
    public Vector axisX, axisY, axisZ;
    private Matrix rotateMat;
    private Matrix positionMat;

    private int uMax, vMax;
    private int k, n, m;
    private double a, b, c, d;

    public Figure3D() {
        bSpline = new BSpline();
        color = new Color(0, 150, 150);

        rotateMat = new Matrix(4);
        rotateMat.setDiagonal(1);

        positionMat = new Matrix(4);
        positionMat.setDiagonal(1);
    }

    public Figure3D(Figure3D figure3D) {
        apply(figure3D);
    }

    public void apply(Figure3D figure3D) {
        bSpline = new BSpline(figure3D.bSpline);
        color = figure3D.color;
        rotateMat = figure3D.rotateMat.copy();
        positionMat = figure3D.positionMat.copy();
    }

    public void calcPoints(Model model) {
        int[] paramsInt = model.getIntParams();
        n = paramsInt[0];
        m = paramsInt[1];
        k = paramsInt[2];

        double[] paramsDouble = model.getParamsDouble();

        a = paramsDouble[0];
        b = paramsDouble[1];
        c = paramsDouble[2];
        d = paramsDouble[3];

        uMax = n * k;
        vMax = m;

        points3D = new Vector[m][n * k];

        for (int u = 0; u < n * k; ++u) {
            double curL = u * (b - a) / (double) (n * k) + a;
            curL *= bSpline.getLen();

            Vector point2D = bSpline.calcL(curL);
            for (int v = 0; v < m; ++v) {
                Vector point3D = new Vector(3);
                double phi = v * (d - c) / (double) m + c;

                point3D.setX(point2D.getY() * Math.cos(phi));
                point3D.setY(point2D.getY() * Math.sin(phi));
                point3D.setZ(point2D.getX());
                points3D[v][u] = point3D;
            }
        }

        axisX = new Vector(1, 0, 0);
        axisY = new Vector(0, 1, 0);
        axisZ = new Vector(0, 0, 1);
    }

    public Matrix getToWorldMatrix() {
        try {
            return positionMat.multiple(rotateMat);
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vector> getPoints() {
        List<Vector> points = new ArrayList<>();

        points.add(new Vector(0, 0, 0, 1)); // 0
        points.add(new Vector(40, 0, 0, 1)); // 1
        points.add(new Vector(0, 30, 0, 1)); // 2
        points.add(new Vector(0, 0, 20, 1)); // 3

        for (Vector[] aPoints3D : points3D) {
            for (Vector oldPoint : aPoints3D) {
                points.add(new Vector(oldPoint.getX(), oldPoint.getY(), oldPoint.getZ(), 1));
            }
        }

        return points;
    }

    public List<int[]> getLinks() {

        List<int[]> links = new ArrayList<>();
        links.add(new int[]{0, 1});
        links.add(new int[]{0, 2});
        links.add(new int[]{0, 3});


        for (int v = 0; v < vMax; ++v) {
            for (int u = 0; u < uMax; ++u) {
                int curPointI = 4 + v * uMax + u;
                if (u < uMax - 1) {
                    links.add(new int[]{
                            curPointI,
                            curPointI + 1
                    });
                }
                if ((u % k == 0) || (u == uMax - 1)) {
                    if (v < vMax - 1) {
                        links.add(new int[]{
                                curPointI,
                                curPointI + uMax
                        });
                    } else {
                        links.add(new int[]{
                                curPointI,
                                curPointI - uMax * (vMax - 1)
                        });
                    }
                }
            }
        }


        return links;
    }

    public void setRotateMat(Matrix rotateMat) {
        this.rotateMat = rotateMat.copy();
    }

    public void setPositionMat(Matrix positionMat) {
        this.positionMat = positionMat;
    }

    public void rotateX(double phiX) {
        try {
            rotateMat = Homogeneous.RotateX(phiX).multiple(rotateMat);
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
    }

    public void rotateY(double phiY) {
        try {
            rotateMat = Homogeneous.RotateY(phiY).multiple(rotateMat);
        } catch (MatrixDimensionException e) {
            e.printStackTrace();
        }
    }
}
