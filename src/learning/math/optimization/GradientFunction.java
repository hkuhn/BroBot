package learning.math.optimization;

import april.jmat.Matrix;

/**
 * User: slessans
 * Date: 4/16/13
 * Time: 11:54 PM
 */
public interface GradientFunction {

    /**
     * @param point a d by 1 vector of input values
     * @return a d by 1 vector representing the gradient at the given point
     */
    public Matrix getGradientAtPoint(Matrix point);

    public int getNumberOfDimensions();

}
