package vision.util;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class MatrixUtils {

    public static RealMatrix createIdentityMatrix(final int dimension) {
        double [] diags = new double[dimension];
        for ( int i = 0; i < dimension; i++ ) diags[i] = 1.0d;
        return new DiagonalMatrix(diags, false);
    }

    /**
     *
     * @param matrix 3 by 1 matrix
     * @return
     */
    public static RealMatrix skewSymmetricMatrix(RealMatrix matrix) {
        if ( matrix.getRowDimension() != 3 || matrix.getColumnDimension() != 1 ) {
            throw new IllegalArgumentException("matrix must by 3 by 1, matrix is " + matrix.getRowDimension() + " by " + matrix.getColumnDimension());
        }

        final double a1 = matrix.getEntry(0,0);
        final double a2 = matrix.getEntry(1,0);
        final double a3 = matrix.getEntry(2,0);

        double [][] data = {
                {0, -a3, a2},
                {a3, 0, -a1},
                {-a2, a1, 0}
        };
        return new Array2DRowRealMatrix(data, false);
    }

    public static void printMatrix(final RealMatrix matrix) {
        final int numRows = matrix.getRowDimension();
        final int numCols = matrix.getColumnDimension();

        System.out.println("Real matrix (" + numRows + "x" + numCols + "):");
        for (int row = 0; row < numRows; row++) {
            System.out.print("[\t");
            for (int col = 0; col < numCols; col++) {
                System.out.print(matrix.getEntry(row, col) + "\t");
            }
            System.out.println("]");
        }
    }

    public static RealMatrix calculateInverse(final RealMatrix matrix) {

        // the 2 by 2 ad 3 by 3 case MAY optimize algorithm in the case of those matrices
        // this idea and implement borrowed from April

        final int rows = matrix.getRowDimension();
        final int cols = matrix.getColumnDimension();

        if ( rows == 2 && cols == 2 ) {

            final double a = matrix.getEntry(0,0);
            final double b = matrix.getEntry(0,1);
            final double c = matrix.getEntry(1,0);
            final double d = matrix.getEntry(1,1);

            final double det = 1/(a*d-b*c);

            double[][] data = {{det*d, -det*b}, {-det*c, det*a}};

            return new Array2DRowRealMatrix(data, false);
        }

        if ( rows == 3 && cols == 3 ) {
            final double a = matrix.getEntry(0, 0);
            final double b = matrix.getEntry(0, 1);
            final double c = matrix.getEntry(0, 2);
            final double d = matrix.getEntry(1, 0);
            final double e = matrix.getEntry(1, 1);
            final double f = matrix.getEntry(1, 2);
            final double g = matrix.getEntry(2, 0);
            final double h = matrix.getEntry(2, 1);
            final double i = matrix.getEntry(2, 2);

            final double det = 1/(a*e*i-a*f*h-d*b*i+d*c*h+g*b*f-g*c*e);

            double [][] data = {
                    {det*(e*i-f*h), det*(-b*i+c*h), det*(b*f-c*e)},
                    {det*(-d*i+f*g), det*(a*i-c*g), det*(-a*f+c*d)},
                    {det*(d*h-e*g), det*(-a*h+b*g), det*(a*e-b*d)}
            };

            return new Array2DRowRealMatrix(data, false);
        }


        return new LUDecomposition(matrix).getSolver().getInverse();
    }

}
