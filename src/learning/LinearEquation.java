package learning;

/**
 * Created with IntelliJ IDEA.
 * User: slessans
 * Date: 4/13/13
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinearEquation {

    protected double [] coefficients;
    protected double constant;

    public LinearEquation(double [] coefficients, double constant) {
        this.coefficients = coefficients;
        this.constant = constant;
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

}
