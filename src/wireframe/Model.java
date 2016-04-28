package wireframe;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private int[] paramsInt = new int[] {10, 10, 5}; // n, m, k
    private double[] paramsDouble = new double[] {0, 1, 0, 6.28, 5, 15, 1, 1}; // k a b c d zn zf sw sh
    private List<Figure3D> figures = new ArrayList<Figure3D>();

    public Model() {
        figures.add(new Figure3D());
    }

    public int[] getIntParams() {
        return paramsInt.clone();
    }

    public double[] getParamsDouble() {
        return paramsDouble.clone();
    }

    public Figure3D getFigure(int i) {
        return figures.get(i);
    }

    public int getFiguresCount() {
        return figures.size();
    }

    public void setIntParams(int[] intValue) {
        this.paramsInt = intValue;
    }
}
