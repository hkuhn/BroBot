package learning.math;

public class OneDimensionalLinearEquation extends LinearEquation {

    public OneDimensionalLinearEquation(final double slope, final double yIntercept) {
        super(new double[]{slope}, yIntercept);
    }

    public double getYValue(final double xVal) {
        return this.getResult(new double[]{xVal});
    }

    public double getSlope() {
        return this.getCoefficients()[0];
    }

    public double getYIntercept() {
        return this.getConstant();
    }

}
