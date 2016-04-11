package matrix.errors;

import matrix.Matrix;

public class MatrixDimensionException extends VectorException {
    private final Matrix a;
    private final Matrix b;
    private final String operation;

    public MatrixDimensionException(Matrix a, Matrix b, String operation) {
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

    public MatrixDimensionException(String text) {
        this.operation = text;
        a = null;
        b = null;
    }

    public String  toString() {
        if ((a != null) && (b != null)) {
            return "Несовпадают размеры матриц при операции " + this.operation + ":" +
                    "" + a.getWidth() + "x" + a.getHeight() + " и " + b.getWidth() + "x" + b.getHeight();
        } else {
            return operation;
        }
    }
}
