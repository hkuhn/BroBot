package learning.math.optimization;

import april.jmat.Matrix;
import learning.math.Quadratic3DEquation;

/**
 * User: slessans
 * Date: 4/21/13
 * Time: 5:44 PM
 */
public class Quadratic3DEquationErrorGradientFunction implements GradientFunction {

    final private Quadratic3DEquation equation;
    final private double targetValue;

    public Quadratic3DEquationErrorGradientFunction(final Quadratic3DEquation equation, final double targetValue) {
        this.equation = equation;
        this.targetValue = targetValue;
    }


    @Override
    public Matrix getGradientAtPoint(Matrix point) {

        final double x = point.get(0,0);
        final double y = point.get(1,0);
        final double z = point.get(2,0);

        final double twoTimesValue = 2 * (this.getEquation().getResult(x, y, z) - this.getTargetValue());

        final double a = this.equation.getxSquaredC();
        final double b = this.equation.getySquaredC();
        final double c = this.equation.getzSquaredC();
        final double d = this.equation.getXyC();
        final double e = this.equation.getXzC();
        final double f = this.equation.getYzC();
        final double g = this.equation.getxC();
        final double h = this.equation.getyC();
        final double i = this.equation.getzC();

        // dx, dy, dz
        double [][] data = {
                {twoTimesValue * (2*a*x + d*y + e*z + g)},
                {twoTimesValue * (2*b*y + d*x + f*z + h)},
                {twoTimesValue * (2*c*z + e*x + f*y + i)}
        };

        return new Matrix(data);
    }

    @Override
    public int getNumberOfDimensions() {
        return 3;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public Quadratic3DEquation getEquation() {
        return equation;
    }
}
