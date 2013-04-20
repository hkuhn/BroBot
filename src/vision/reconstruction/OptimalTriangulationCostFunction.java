package vision.reconstruction;

/**
 * See 'R. Hartley and A. Zisserman. Multiple View Geometry in Computer Vision. Academic Press, 2002'
 * page 317 (equation 12.5) for details about what the parameters mean.
 * User: slessans
 * Date: 4/20/13
 * Time: 6:17 PM
 */
public final class OptimalTriangulationCostFunction {

    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double f;
    private final double fPrime;

    public OptimalTriangulationCostFunction(double a, double b, double c, double d, double f, double fPrime) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.f = f;
        this.fPrime = fPrime;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public double getF() {
        return f;
    }

    public double getfPrime() {
        return fPrime;
    }

    /**
     * Evaluates function at given t
     * @param t
     * @return
     */
    public double evaluate(final double t) {

        final double tSquared = Math.pow(t, 2);

        final double firstTerm = tSquared / (1 + tSquared * Math.pow(this.f, 2));
        final double secondTermNumerator = Math.pow(this.c * t + this.d, 2);
        final double secondTermDenominator = Math.pow(this.a * t + this.b, 2) +
                Math.pow(this.fPrime, 2) * Math.pow(this.c * t + this.d, 2);

        return firstTerm + (secondTermNumerator / secondTermDenominator);

    }

    /**
     * Evaluates function as t --> infinity
     * @return
     */
    protected double evaluateAsymtotically() {
        final double cSquared = Math.pow(this.c, 2);
        return (1 / Math.pow(this.f, 2)) + cSquared / (Math.pow(this.a, 2) + Math.pow(this.fPrime, 2) * cSquared);
    }

}
