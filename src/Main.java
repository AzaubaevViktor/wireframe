import matrix.Matrix;
import matrix.Vector;
import matrix.errors.MatrixDimensionException;
import matrix.errors.VectorDimensionException;


public class Main {
    public static void main(String[] args) {
        Matrix a = new Matrix(new double[][]{new double []{1,2,3}, new double[]{4,5,6}});
        System.out.println(a);
        Matrix b = new Matrix(new double[][]{new double[]{7, 8, 9}, new double[]{9, 10, 11}});
        System.out.println(b);
        Matrix bt = b.transpone();
        System.out.println(bt);

        Vector v1 = new Vector(new double[] {1,2,3,4});
        Vector v2 = new Vector(new double[] {5,6,7,8});

        try {
            Matrix c = a.multiple(bt);
            System.out.println(c);

            System.out.println(v1.plus(v2));

            System.out.println(v1.multipleSum(v2));

            Matrix mv1 = v1.getMatrix(true);
            Matrix mv2 = v2.getMatrix(false);
            System.out.println(mv1.multiple(mv2));
        } catch (MatrixDimensionException | VectorDimensionException e) {
            e.printStackTrace();
        }
    }
}
