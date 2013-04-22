package learning.math.optimization;

import april.jmat.Matrix;
import learning.math.LinearEquation;

/**
 * User: slessans
 * Date: 4/17/13
 * Time: 1:07 AM
 */

public class LinearEquationErrorGradientFunction implements GradientFunction {

    private LinearEquation linearEquation;
    private double targetValue;

    public LinearEquationErrorGradientFunction(LinearEquation linearEquation, double targetValue) {
        this.linearEquation = linearEquation;
        this.targetValue = targetValue;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public LinearEquation getLinearEquation() {
        return linearEquation;
    }

    public static double [] makeArrayFromVectorMatrix(Matrix m) {
        double [] array = new double[m.getRowDimension()];
        for (int i = 0; i < array.length; i++) {
            array[i] = m.get(i, 0);
        }
        return array;
    }

    @Override
    public int getNumberOfDimensions() {
        return this.getLinearEquation().getNumberOfDimensions();
    }

    @Override
    public Matrix getGradientAtPoint(Matrix point) {

        final int numberOfDimensions = linearEquation.getNumberOfDimensions();
        Matrix gradient = new Matrix(numberOfDimensions, 1);

        final double valueAtPointMinusTarget = this.getLinearEquation().getResult(makeArrayFromVectorMatrix(point)) - this.getTargetValue();

        for ( int dimension = 0; dimension < numberOfDimensions; dimension++ ) {
            final double theta = point.get(dimension, 0);
            final double a = linearEquation.getCoefficients()[dimension];
            final double z = valueAtPointMinusTarget - (theta * a);
            double result = (2 * theta * a * a) + 2 * z * a;
            gradient.set(dimension, 0, result);
        }

        return gradient;
    }
}
