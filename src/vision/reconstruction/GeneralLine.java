package vision.reconstruction;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

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

    public RealMatrix getClosestPointToOrigin() {
        double [][] data = {
                {-this.getLambda() * this.getV()},
                {-this.getMu() * this.getV()},
                {Math.pow(this.getLambda(), 2) + Math.pow(this.getMu(), 2)}
        };
        return new Array2DRowRealMatrix(data, false);
    }

}
