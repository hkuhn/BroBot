package learning.math;

import april.jmat.Matrix;

public class LinearRegression {

    protected static final boolean DEBUG = false;


    protected Matrix result;

    /**
     * Create a linear regression with input data x and output data y. The matrix X should NOT be one padded,
     * this will be done automatically.
     * @param x n rows, d cols matrix of input data. n is number of sample points. d is number of variables to learn.
     * @param y n rows, 1 col matrix of outcomes, n is number of sample points. same as in x.
     */
    public LinearRegression(Matrix x, Matrix y) {

        if (x.getRowDimension() != y.getRowDimension()) {
            String xSize = getMatrixSizeString(x);
            String ySize = getMatrixSizeString(y);
            throw new IllegalArgumentException(
                    "Matrix x [" + xSize + "] should have same number " +
                            "of rows as matrix y [" + ySize + "].");
        }

        if (y.getColumnDimension() != 1) {
            String ySize = getMatrixSizeString(y);
            throw new IllegalArgumentException("Matrix y [" + ySize + "] should have exactly one column.");
        }

        if ( x.getRowDimension() == 0 ) {
            String xSize = getMatrixSizeString(x);
            throw new IllegalArgumentException("Matrix x [" + xSize + "] should have at least one row.");
        }

        if (x.getColumnDimension() == 0) {
            String xSize = getMatrixSizeString(x);
            throw new IllegalArgumentException("Matrix x [" + xSize + "] should have at least one column.");
        }

        Matrix newX = new Matrix(x.getRowDimension(), x.getColumnDimension() + 1);
        for (int row = 0; row < x.getRowDimension(); row++) {
            for (int col = 0; col < x.getColumnDimension(); col++) {
                newX.set(row, col + 1, x.get(row,col));
            }
        }
        for (int row = 0; row < x.getRowDimension(); row++) {
            newX.set(row, 0, 1);
        }


        if (DEBUG) printMatrix(x, "Original x");
        x = newX;
        if (DEBUG) printMatrix(x, "1-padded x");

        Matrix xTranspose = x.transpose();
        Matrix theta = xTranspose.times(x).inverse();
        Matrix b = xTranspose.times(y);
        this.result = theta.times(b);

        if (DEBUG) printMatrix(this.result, "Result");
    }

    public static String getMatrixSizeString(Matrix m) {
        return m.getRowDimension() + " by " + m.getColumnDimension();
    }

    public LinearEquation getResultantLinearEquation() {
        double [] resultCoefficients = new double[result.getRowDimension() - 1];
        for ( int i = 0; i < resultCoefficients.length; i++ ) {
            resultCoefficients[i] = result.get(i+1, 0);
        }
        double resultConstant = result.get(0, 0);
        return new LinearEquation(resultCoefficients, resultConstant);
    }

    public Matrix getResult() {
        return result;
    }

    public static void printMatrix(Matrix m) {
        printMatrix(m, "" + m);
    }

    public static void printMatrix(Matrix m, String message) {
        String sizeString =getMatrixSizeString(m);
        System.out.println("Matrix " + message + " [" + sizeString + "]");
        for (int row = 0; row < m.getRowDimension(); row++) {
            System.out.print("[\t");
            for (int col = 0; col < m.getColumnDimension(); col++) {
                System.out.print(m.get(row, col) + "\t");
            }
            System.out.println("]");
        }
    }

}
