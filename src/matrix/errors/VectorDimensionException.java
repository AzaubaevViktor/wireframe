package matrix.errors;

import matrix.Vector;

public class VectorDimensionException extends Throwable {
    private final Vector a;
    private final Vector b;
    private final String operation;

    public VectorDimensionException(Vector a, Vector b, String operation) {
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

    public VectorDimensionException(String text) {
        this.operation = text;
        a = null;
        b = null;
    }

    public String  toString() {
        if ((a != null) && (b != null)) {
            return "Несовпадают размеры матриц при операции " + this.operation + ":" +
                    "" + a.length() + " и " + b.length();
        } else {
            return operation;
        }
    }

}
