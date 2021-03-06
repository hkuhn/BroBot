package learning.math;

import april.jmat.Matrix;

import java.util.Arrays;

public class LinearEquation {

    private double [] coefficients;
    private double constant;

    public LinearEquation(double [] coefficients, double constant) {
        this.coefficients = coefficients;
        this.constant = constant;
    }

    public double[] getCoefficients() {
        return this.coefficients;
    }

    public OneDimensionalLinearEquation getEquationForDimension(final int d) {
        if ( d >= this.coefficients.length ) {
            throw new IllegalArgumentException(
                    "Dimension " + d + " exceeds number of " +
                    "dimensions in this linear equation (" + this.coefficients.length + ")."
            );
        }
        return new OneDimensionalLinearEquation(this.coefficients[d], this.constant);
    }


    public static LinearEquation getLinearEquationFromLinearRegression(LinearRegression regression) {
        Matrix result = regression.getResult();
        double [] resultCoefficients = new double[result.getRowDimension() - 1];
        for ( int i = 0; i < resultCoefficients.length; i++ ) {
            resultCoefficients[i] = result.get(i+1, 0);
        }
        double resultConstant = result.get(0, 0);
        return new LinearEquation(resultCoefficients, resultConstant);
    }

    public double getConstant() {
        return this.constant;
    }

    public int getNumberOfDimensions() {
        return this.coefficients.length;
    }

    public double getResult(double [] values) {
        if ( values.length != coefficients.length ) {
            throw new IllegalArgumentException("values [] should have same length as number " +
                    "of coefficients (" + this.coefficients.length + ")");
        }

        double sum = this.constant;
        for ( int i = 0; i < coefficients.length; i++ ) {
            sum += coefficients[i] * values[i];
        }

        return sum;
    }

    @Override
    public String toString() {
        return "LinearEquation{" +
                "coefficients=" + Arrays.toString(coefficients) +
                ", constant=" + constant +
                '}';
    }
}
