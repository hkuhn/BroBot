package learning;

import april.jmat.Matrix;

/**
 * User: slessans
 * Date: 4/16/13
 * Time: 11:47 PM
 */
public class GradientDescent {

    private final static double DefaultStepSize = 0.01d;
    private final static double DefaultPrecision = 0.00001d;

    private final double stepSize;
    private final double precision;
    private final GradientFunction gradientFunction;

    public GradientDescent(final GradientFunction gradientFunction, double stepSize, double precision) {
        this.gradientFunction = gradientFunction;
        this.stepSize = stepSize;
        this.precision = precision;
    }

    public GradientDescent(final GradientFunction gradientFunction, double stepSize) {
        this(gradientFunction, stepSize, DefaultPrecision);
    }

    public GradientDescent(final GradientFunction gradientFunction) {
        this(gradientFunction, DefaultStepSize);
    }

    protected double getStepSize() {
        return stepSize;
    }

    protected double getPrecision() {
        return precision;
    }

    protected GradientFunction getGradientFunction() {
        return gradientFunction;
    }

    /**
     * Takes the difference of two matrices, then checks if difference is
     * "precise enough." In this case, precise enough is defined as each element
     * being less than the specified precision. Subclasses can alter behavior by
     * overriding this function, or overriding the precision function. If either
     * matrix is null, false is returned.
     * @param m1
     * @param m2
     * @return
     */
    protected boolean differenceOfMatricesIsPreciseEnough(final Matrix m1, final Matrix m2) {

        if ( m1 == null || m2 == null ) return false;

        final Matrix diff = m1.minus(m2);
        final int numRows = diff.getRowDimension();
        final int numCols = diff.getColumnDimension();

        for (int row = 0; row < numRows; row++ ) {
            for ( int col = 0; col < numCols; col++ ) {
                final double absVal = Math.abs(diff.get(row, col));
                if ( absVal >= this.getPrecision() ) return false;
            }
        }

        return true;
    }

    public Matrix estimateLocalMinimum() {
        final int numberOfDimensions = this.getGradientFunction().getNumberOfDimensions();
        Matrix initialGuess = new Matrix(numberOfDimensions, 1);
        for ( int i = 0; i < numberOfDimensions; i++ ) {
            initialGuess.set(i, 0, 0);
        }
        return this.estimateLocalMinimum(initialGuess);
    }

    public Matrix estimateLocalMinimum(final Matrix initialGuess) {

        Matrix previousGuess = initialGuess.copy();

        while (true) {
            final Matrix gradient = this.getGradientFunction().getGradientAtPoint(previousGuess);
            final Matrix newGuess = previousGuess.minus(gradient.times(this.getStepSize()));

            if (this.differenceOfMatricesIsPreciseEnough(newGuess, previousGuess)) {
                return newGuess;
            }

            previousGuess = newGuess;
        }

    }

}
