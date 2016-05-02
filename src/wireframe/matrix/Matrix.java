package wireframe.matrix;


import wireframe.matrix.errors.MatrixDimensionException;
import wireframe.matrix.errors.VectorDimensionException;

public class Matrix {
    private double [][] values; // [Y][X]
    private int width;
    private int height;

    public Matrix(int width, int height) {
        this.width = width;
        this.height = height;
        this.values = new double[height][width];
    }

    public Matrix(int sqSize) {
        this.width = this.height = sqSize;
        this.values = new double[sqSize][sqSize];
    }

    public void matrixInitValuesMultiptication(double[][] values, double multiplication) {
        this.values = values.clone();
        this.width = values[0].length;
        this.height = values.length;
        this.multiple(multiplication);
    }

    public Matrix(double[][] values, double multiplication) {
        matrixInitValuesMultiptication(values, multiplication);
    }

    public Matrix(double [][] values) {
        matrixInitValuesMultiptication(values, 1);
    }


    private void createMatrixFromOther(Matrix m, boolean copyValues) {
        this.width = m.width;
        this.height = m.height;
        if (copyValues) {
            this.values = m.values.clone();
        } else {
            this.values = new double[this.height][this.width];
        }
    }

    public Matrix(Matrix m) {
        createMatrixFromOther(m, true);
    }

    public Matrix(Matrix m, boolean copyValues) {
        createMatrixFromOther(m, copyValues);
    }

    // GETTER+SETTER

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean checkXY(int x, int y) {
        if ((0 > x) || (x > width)) {
            throw new IndexOutOfBoundsException("X(" + x + ") не принадлежит [0;" + width + "]");
        }
        if ((0 > y) || (y > height)) {
            throw new IndexOutOfBoundsException("Y(" + y + ") не принадлежит [0;" + height + "]");
        }
        return true;
    }

    public double get(int x, int y) {
        checkXY(x, y);
        return values[y][x];
    }

    public Vector getRow(int y) {
        checkXY(0, y);
        return new Vector(values[y]);
    }

    public Vector getColumn(int x) {
        checkXY(x, 0);
        double [] tmp = new double[height];
        for (int y = 0; y < height; y++) {
            tmp[y] = values[y][x];
        }
        return new Vector(tmp);
    }

    public void set(int x, int y, double v) {
        checkXY(x, y);
        values[y][x] = v;
    }

    public void setRow(int y, double [] row) throws MatrixDimensionException {
        if (row.length != width) {
            throw new MatrixDimensionException("Ширина заполняемого массива не соответствует длинне массиве. " +
                    width + "x" + height + "; len(row) == " + row.length);
        }
        values[y] = row.clone();
    }

    public void setRow(int y, Vector row) throws MatrixDimensionException {
        setRow(y, row.getValues());
    }

    public void setColumn(int x, double [] column) throws MatrixDimensionException {
        if (column.length != height) {
            throw new MatrixDimensionException("Высота заполняемого массива не соответствует длинне массива. " +
                    width + "x" + height + "; len(column) == " + column.length);
        }
        for (int y = 0; y < height; y++) {
            values[y][x] = column[y];
        }
    }

    public void setColumn(int x, Vector column) throws MatrixDimensionException {
        setColumn(x, column.getValues());
    }

    public void setDiagonal(double [] values) {
        int size = Math.min(width, height);
        for (int i = 0; i < size; i++) {
            this.values[i][i] = values[i % values.length];
        }
    }

    public void setDiagonal(double value) {
        setDiagonal(new double[]{value});
    }

    // UTILS

    public Matrix copy() {
        return new Matrix(this);
    }

    public String toString() {
        String s = "";
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                s += values[h][w] + " ";
            }
            s += "\n";
        }
        return s;
    }

    // SCALAR

    public void nulled() {
        fillAll(0);
    }

    public void fillAll(double value) {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                values[h][w] = value;
            }
        }
    }

    public void multiple(double k) {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                values[h][w] *= k;
            }
        }
    }

    // MATRIX

    public Matrix plus(Matrix m) throws MatrixDimensionException {
        if ((width != m.width) || (height != m.height)) {
            throw new MatrixDimensionException(this, m, "plus");
        }
        Matrix result = new Matrix(this);

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                result.values[h][w] = this.values[h][w] + m.values[h][w];
            }
        }

        return result;
    }

    public Matrix multiple(Matrix m) throws MatrixDimensionException {
        if (width != m.height) {
            throw new MatrixDimensionException(this, m, "multiple");
        }
        Matrix result = new Matrix(m.width, this.height);

        for (int h = 0; h < this.height; h++) {
            for (int w = 0; w < m.width; w++) {
                try {
                    result.values[h][w] = this.getRow(h).scalarMul(m.getColumn(w));
                } catch (VectorDimensionException ve) {
                    System.out.println("Ну нахуй, не может такого быть Оо\n" + ve);
                }
            }
        }
        return result;
    }

    public Vector multiple(Vector v) throws MatrixDimensionException {
        return this.multiple(v.getMatrix()).getColumn(0);
    }

    public Matrix transpone() {
        //noinspection SuspiciousNameCombination
        Matrix result = new Matrix(this.height, this.width);
        for (int h = 0; h < this.height; h++) {
            for (int w = 0; w < this.width; w++) {
                result.values[w][h] = values[h][w];
            }
        }
        return result;
    }

    // VECTORS

}
