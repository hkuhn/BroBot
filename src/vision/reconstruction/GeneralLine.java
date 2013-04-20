package vision.reconstruction;

import april.jmat.Matrix;

/**
 * Takes three params, as if line is vector format (lambda, mu, v)
 * User: slessans
 * Date: 4/20/13
 * Time: 6:54 PM
 */
public class GeneralLine {

    final private double lambda;
    final private double mu;
    final private double v;

    public GeneralLine(double lambda, double mu, double v) {
        this.lambda = lambda;
        this.mu = mu;
        this.v = v;
    }

    public double getLambda() {
        return lambda;
    }

    public double getMu() {
        return mu;
    }

    public double getV() {
        return v;
    }

    public Matrix getClosestPointToOrigin() {
        Matrix point = new Matrix(3,1);
        point.set(0, 0, -lambda * v);
        point.set(0, 1, -mu * v);
        point.set(0, 2, Math.pow(lambda, 2) + Math.pow(mu, 2));
        return point;
    }

}
