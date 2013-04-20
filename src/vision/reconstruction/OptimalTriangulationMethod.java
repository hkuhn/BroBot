package vision.reconstruction;

import april.jmat.Matrix;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import static java.lang.Math.*;

/**
 * User: slessans
 * Date: 4/18/13
 * Time: 1:27 AM
 */

/*
 * This follows the 'Optimal Triangulation Method' outlined on page 318 in:
 * R. Hartley and A. Zisserman. Multiple View Geometry in Computer Vision. Academic Press, 2002
 */
public class OptimalTriangulationMethod implements TwoViewStructureReconstructor {

    protected Matrix leftImagePoint;
    protected Matrix rightImagePoint;
    protected Matrix fundamentalMatrix;
    protected Matrix t;
    protected Matrix tp;
    protected Matrix r;
    protected Matrix rp;
    protected Matrix e;
    protected Matrix ep;
    protected Matrix xHat;
    protected Matrix xHatPrime;
    protected Matrix mleX;


    protected void reset() {
        this.leftImagePoint = null;
        this.rightImagePoint = null;
        this.fundamentalMatrix = null;
        this.t = null;
        this.tp = null;
        this.r = null;
        this.rp = null;
        this.e = null;
        this.ep = null;
        this.xHat = null;
        this.xHatPrime = null;
        this.mleX = null;
    }

    protected void calculateInitialTransformationMatrices() {
        // to move points to origin, simply translate.
        this.t = translationMatrix(this.leftImagePoint.get(0,0), this.leftImagePoint.get(1,0));
        this.tp = translationMatrix(this.rightImagePoint.get(0,0), this.rightImagePoint.get(1,0));
    }

    protected void translateFundamentalMatrix() {
        this.fundamentalMatrix = this.tp.inverse().transpose().times(this.fundamentalMatrix.times(this.t.inverse()));
    }

    protected void computeEpipoles() {

        // get the epipoles by taking the SVD to find right and left null-spaces of the fundamental matrix

        // use the apache commons math library for this, so first create a copy of the fundamental matrix
        // in that form
        RealMatrix f = new Array2DRowRealMatrix(this.fundamentalMatrix.copyArray(), false);

        // create the singular value decomp
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(f);

        // epipole 1 (left) is the last (3rd) column vector in the V matrix
        double [] leftEpipoleData = singularValueDecomposition.getV().getColumn(2);

        // epipole 2 (right) is the last (3rd) column vector in the U matrix
        double [] rightEpipoleData = singularValueDecomposition.getU().getColumn(2);

        // normalize the left and right epipoles s.t. e1^2 + e2^2 = 1 (where e1 and e2 are the first and second
        // components of the data
        this.e = normalizeAndMakeMatrix(leftEpipoleData);
        this.ep = normalizeAndMakeMatrix(rightEpipoleData);
    }

    protected void computeRotationMatrices() {
        this.r = rotationMatrixFromEpipole(this.e);
        this.rp = rotationMatrixFromEpipole(this.ep);
    }

    protected void rotateFundamentalMatrix() {
        this.fundamentalMatrix = this.rp.times(this.fundamentalMatrix).times(this.r.transpose());
    }

    /**
     * Will set costFunctionMinimumT to param that minimizes cost function. if
     * cost function is minimum at t --> infinity, will set to null.
     */
    protected void findGlobalMinimumOfCostFunctionAndMLEPointCorrespondencePair() {
        // generated via MATLAB -
        //
        // polynomial coefficient array:
        //  6: (a^2)cd(f^4) - ab(c^2)(f^4)
        //  5: (a^2)(d^2)(f^4) - 2(a^2)(c^2)(f'^2) - (c^4)(f'^4)-(a^4)
        //  4: 2(a^2)cd(f^2) - 4(a^2)cd(f'^2) + ab(d^2)(f^4) - 4ab(c^2)(f'^2) - 2ab(c^2)(f^2) -
        //          (b^2)cd(f^4) - 4(c^3)d(f'^4) - 4(a^3)b
        //  3: 2(a^2)(d^2)(f^2) - 8abcd(f'^2) - 6(c^2)(d^2)(f'^4) - 2(b^2)(c^2)(f'^2) -
        //          2(a^2)(d^2)(f'^2) - 2(b^2)(c^2)(f^2) - 6(a^2)(b^2)
        //  2: 2ab(d^2)(f^2) - 4(b^2)cd(f'^2) - 2(b^2)cd(f^2) - 4ab(d^2)(f'^2) + (a^2)cd -
        //          4c(d^3)(f'^4) - ab(c^2) - 4a(b^3)
        //  1: (a^2)(d^2) - 2(b^2)(d^2)(f'^2) - (d^4)(f'^4) - (b^2)(c^2) - (b^4)
        //  0: ab(d^2) - (b^2)cd
        //
        //
        // constants:
        //      F =     ff'd    -f'c    f'd
        //              -fb     a       b
        //              -fd     c       d
        double a = this.fundamentalMatrix.get(2,2);
        double b = this.fundamentalMatrix.get(2,3);
        double c = this.fundamentalMatrix.get(3,2);
        double d = this.fundamentalMatrix.get(3,3);
        double f = -1 * this.fundamentalMatrix.get(1,3) / d;
        double f_p = -1 * this.fundamentalMatrix.get(1,2) / c;
        
        double six_coeff = pow(a,2)*c*d*pow(f,4) - a*b*pow(c,2)*pow(f,4);
        double five_coeff = pow(a,2)*pow(d,2)*pow(f,4) - 2*pow(a,2)*pow(c,2)*pow(f_p,2)
                                - pow(c,4)*pow(f_p,4) - pow(a,4);
        double four_coeff = 2*pow(a,2)*c*d*pow(f,2) - 4*pow(a,2)*c*d*pow(f_p,2)
                                + a*b*pow(d,2)*pow(f,4) - 4*a*b*pow(c,2)*pow(f_p,2)
                                - 2*a*b*pow(c,2)*pow(f,2) - pow(b,2)*c*d*pow(f,4)
                                - 4*pow(c,3)*d*pow(f_p,4) - 4*pow(a,3)*b;
        double three_coeff = 2*pow(a,2)*pow(d,2)*pow(f,2) - 8*a*b*c*d*pow(f_p,2)
                                - 6*pow(c,2)*pow(d,2)*pow(f_p,4) - 2*pow(b,2)*pow(c,2)*pow(f_p,2)
                                - 2*pow(a,2)*pow(d,2)*pow(f_p,2) - 2*pow(b,2)*pow(c,2)*pow(f,2)
                                - 6*pow(a,2)*pow(b,2);
        double two_coeff = 2*a*b*pow(d,2)*pow(f,2) - 4*pow(b,2)*c*d*pow(f_p,2)
                                - 2*pow(b,2)*c*d*pow(f,2) - 4*a*b*pow(d,2)*pow(f_p,2)
                                + pow(a,2)*c*d - 4*c*pow(d,3)*pow(f_p,4) - a*b*pow(c,2)
                                - 4*a*pow(b,3);
        double one_coeff = pow(a,2)*pow(d,2) - 2*pow(b,2)*pow(d,2)*pow(f_p,2)
                                - pow(d,4)*pow(f_p,4) - pow(b,2)*pow(c,2) - pow(b,4);
        double zero_coeff = a*b*pow(d,2) - pow(b,2)*c*d;

        // first term should be the consants, then firt order, second order, etc
        double [] coefficients = {zero_coeff, one_coeff, two_coeff, three_coeff, four_coeff, five_coeff, six_coeff};
        //double[] coeff = {six_coeff, five_coeff, four_coeff, three_coeff, two_coeff, one_coeff, zero_coeff};
        
        
        // now solve for roots
        LaguerreSolver polynomialSolver = new LaguerreSolver();
        Complex[] roots = polynomialSolver.solveAllComplex(coefficients, 0);
        OptimalTriangulationCostFunction costFunction = new OptimalTriangulationCostFunction(a, b, c, d, f, f_p);


        // now evaluate the cost function at the real values of each of the roots. find t that corresopnds
        // to the global min.
        double minimumValue = 0;
        Double minT = null;
        for (Complex complexRoot : roots ) {
            final double realRoot = complexRoot.getReal();
            final double value = costFunction.evaluate(realRoot);
            if ( minT == null || value < minimumValue ) {
                minimumValue = value;
                minT = realRoot;
            }
        }

        // now evaluate the cost function as t --> infinity and check if that is actually the minimum
        final double asymptoticValue = costFunction.evaluateAsymtotically();

        if (minT == null || asymptoticValue < minimumValue) {
            // min value is at infinity
            minT = null;
        }

        this.findMLEPointCorrespondencePair(minT, costFunction);
    }


    protected void findMLEPointCorrespondencePair(final Double minT, OptimalTriangulationCostFunction costFunction) {
        // use the tMin to evaluate the lines. if tMin is null, min is at infinity
        this.xHat = null;
        this.xHatPrime = null;

        if ( minT == null ) {
            throw new RuntimeException("minT is at asymtote");
        } else {
            GeneralLine l = new GeneralLine(
                    minT * costFunction.getF(),
                    1,
                    - minT
            );
            GeneralLine lPrime = new GeneralLine(
                    - costFunction.getfPrime() * (costFunction.getC() * minT + costFunction.getD()),
                    costFunction.getA() * minT + costFunction.getB(),
                    costFunction.getC() * minT + costFunction.getD()
            );
            this.xHat = l.getClosestPointToOrigin();
            this.xHatPrime = l.getClosestPointToOrigin();
        }

        // transform back to original system
        this.xHat = this.t.inverse().times(this.r.transpose()).times(this.xHat);
        this.xHatPrime = this.tp.inverse().times(this.rp.transpose()).times(this.xHatPrime);
    }

    protected void findXInThreeSpace() {

    }

    protected void runAlgorithm() {

        // Step 1
        // Define rigid-body transformation matrices t, tp s.t. the points leftImagePoint and rightImagePoint are
        // transformed to their respective origins
        this.calculateInitialTransformationMatrices();

        // Step 2
        // Replace fundamentalMatrix by tp^(-T) * F * t^(-1) this new F is the fundamental matrix in the
        // translated coordinate system
        this.translateFundamentalMatrix();

        // Step 3
        // Compute left and right epipoles using null spaces of new F, the normalize
        this.computeEpipoles();

        // Step 4
        // Form rotation matrices that will bring epipoles to x-axis
        this.computeRotationMatrices();

        // Step 5
        // Replace fundamentalMatrix by rp * fundamentalMatrix * r^T
        this.rotateFundamentalMatrix();

        // Step 6/7/8
        // form the 6-degree polynomial g(t) representing the derivative of the cost function and then
        // solve for its 6 roots in terms of t to get extrema points. Then evaluate cost function at each
        // of the points and additionally find the asympotic value as t --> infinity. Of all these values
        // select t corresponding to the minimum value. that is our optimal parameter.
        // Step 9/10
        // using the parameter found in Step6/7/8, calculate the real world lines and then use them
        // to find the corresponding points xHat <--> xHatPrime. Finally transform the points back
        // to the original coordinate system using the translation and rotation matrices from the
        // beginning steps.
        this.findGlobalMinimumOfCostFunctionAndMLEPointCorrespondencePair();

        // Step 11 find the MLE of X in 3-space
        this.findXInThreeSpace();
    }

    @Override
    public Matrix getPointInThreeSpace(Matrix leftImagePoint, Matrix rightImagePoint, Matrix fundamentalMatrix) {

        // first do integrity constraints
        if ( leftImagePoint.getRowDimension() != 2 || leftImagePoint.getColumnDimension() != 1 ) {
            throw new IllegalArgumentException("leftImagePoint must be a 2 by 1 matrix.");
        }

        if ( rightImagePoint.getRowDimension() != 2 || rightImagePoint.getColumnDimension() != 1 ) {
            throw new IllegalArgumentException("rightImagePoint must be a 2 by 1 matrix.");
        }

        if ( fundamentalMatrix.getRowDimension() != 3 || fundamentalMatrix.getColumnDimension() != 3 ) {
            throw new IllegalArgumentException("fundamentalMatrix must be a 3 by 3 matrix.");
        }

        // start be clearing state
        this.reset();

        // set left and right image point ivars, also transform into homogeneous coordinates
        this.leftImagePoint = makeHomogeneous(leftImagePoint);
        this.rightImagePoint = makeHomogeneous(rightImagePoint);
        this.fundamentalMatrix = fundamentalMatrix;

        runAlgorithm();

        return this.mleX;
    }

    protected static Matrix rotationMatrixFromEpipole(Matrix epipole) {

        if (epipole.getColumnDimension() != 1 || epipole.getRowDimension() != 3) {
            throw new IllegalArgumentException("Epipole must be a 3 by 1 matrix");
        }

        final double e1 = epipole.get(0,0);
        final double e2 = epipole.get(1,0);

        // forms rotation matrix to bring epipole to x-axis
        // via formula in Book on page 318
        Matrix rotationMatrix = Matrix.identity(3,3);
        rotationMatrix.set(0, 0, e1);
        rotationMatrix.set(0, 1, e2);
        rotationMatrix.set(1, 0, -e2);
        rotationMatrix.set(1, 1, e1);
        return rotationMatrix;
    }

    // makes 2-d homogenous point into normalized matrix form s.t.
    // c0^2 + c1^2 = 1 (where c0 and c1 are first and second components)
    protected static Matrix normalizeAndMakeMatrix(double [] vec) {

        if ( vec.length != 3 ) {
            throw new IllegalArgumentException("vec must be of length 3");
        }

        final double scale = 1 / Math.sqrt(Math.pow(vec[0],2) + Math.pow(vec[1],2));
        final Matrix result = new Matrix(3, 1);
        for ( int row = 0; row < vec.length; row++ ) {
            result.set(row, 0, vec[row] * scale);
        }
        return result;
    }

    protected static Matrix translationMatrix(final double x, final double y) {
        Matrix t = Matrix.identity(3,3);
        t.set(0,2,x);
        t.set(1,2,y);
        return t;
    }

    protected static Matrix makeHomogeneous(Matrix m) {

        if ( m.getColumnDimension() != 1 ) {
            throw new IllegalArgumentException("m must have exactly one column.");
        }

        final int numRows = m.getRowDimension();

        Matrix h = new Matrix(numRows + 1, 1);

        for ( int row = 0; row < numRows; row++ ) {
            h.set(row, 0, m.get(row, 0));
        }

        h.set(numRows, 0, 1);

        return h;
    }

}
