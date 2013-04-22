package vision.reconstruction;

import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import vision.datastructures.Point2Space;
import vision.datastructures.Point3Space;
import vision.datastructures.StereoCameraPair;
import vision.util.MatrixUtils;

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

    protected StereoCameraPair stereoCameraPair;
    protected RealMatrix fundamentalMatrix;
    protected RealMatrix leftImagePoint;
    protected RealMatrix rightImagePoint;
    protected RealMatrix t;
    protected RealMatrix tp;
    protected RealMatrix r;
    protected RealMatrix rp;
    protected RealMatrix e;
    protected RealMatrix ep;
    protected RealMatrix xHat;
    protected RealMatrix xHatPrime;

    /**
     * THIS IS IN millimeters
     */
    protected Point3Space mleX;


    protected void reset() {
        this.leftImagePoint = null;
        this.rightImagePoint = null;
        this.stereoCameraPair = null;
        this.t = null;
        this.tp = null;
        this.r = null;
        this.rp = null;
        this.e = null;
        this.ep = null;
        this.xHat = null;
        this.xHatPrime = null;
        this.mleX = null;
        this.fundamentalMatrix = null;
    }

    protected void calculateInitialTransformationMatrices() {
        // to move points to origin, simply translate.
        this.t = translationMatrix(-this.leftImagePoint.getEntry(0,0), -this.leftImagePoint.getEntry(1,0));
        this.tp = translationMatrix(-this.rightImagePoint.getEntry(0,0), -this.rightImagePoint.getEntry(1,0));
    }

    protected void translatefundamentalMatrix() {
        this.fundamentalMatrix = MatrixUtils.calculateInverse(this.tp).transpose().multiply(this.fundamentalMatrix.multiply(MatrixUtils.calculateInverse(this.t)));
    }

    protected void computeEpipoles() {

        // get the epipoles by taking the SVD to find right and left null-spaces of the fundamental RealMatrix        
        // create the singular value decomp
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(this.fundamentalMatrix);

        // epipole 1 (left) is the last (3rd) column vector in the V RealMatrix
        double [] leftEpipoleData = singularValueDecomposition.getV().getColumn(2);

        // epipole 2 (right) is the last (3rd) column vector in the U RealMatrix
        double [] rightEpipoleData = singularValueDecomposition.getU().getColumn(2);

        // normalize the left and right epipoles s.t. e1^2 + e2^2 = 1 (where e1 and e2 are the first and second
        // components of the data
        this.e = normalizeAndMakeMatrix(leftEpipoleData);
        this.ep = normalizeAndMakeMatrix(rightEpipoleData);
    }

    protected void computeRotationMatrices() {
        this.r = rotationRealMatrixFromEpipole(this.e);
        this.rp = rotationRealMatrixFromEpipole(this.ep);
    }

    protected void rotatefundamentalMatrix() {
        this.fundamentalMatrix = this.rp.multiply(this.fundamentalMatrix).multiply(this.r.transpose());
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
        double a = this.fundamentalMatrix.getEntry(1,1);
        double b = this.fundamentalMatrix.getEntry(1,2);
        double c = this.fundamentalMatrix.getEntry(2,1);
        double d = this.fundamentalMatrix.getEntry(2,2);
        double f = this.e.getEntry(2,0);
        double f_p = this.ep.getEntry(2,0);
        
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
        this.xHat = MatrixUtils.calculateInverse(this.t).multiply(this.r.transpose()).multiply(this.xHat);
        this.xHatPrime = MatrixUtils.calculateInverse(this.tp).multiply(this.rp.transpose()).multiply(this.xHatPrime);
    }

    /**
     * Find the 3-space point X, given the MLE estimate for xHat and xHatPrime.
     * This uses the homogeneous method outlined in section 12.2 of the book.
     *
     * (12.2) ultimately lead me to algorithm (A5.4) on page 593.
     */
    protected void findXInThreeSpace() {

        // Minimize ||AX|| subject to ||X|| = 1
        // where A is constructed using the linear equations given by the point
        // correspondences and camera matrices
        RealMatrix A = new Array2DRowRealMatrix(4, 4);

        // RealMatrix is of structure:
        // x * p_3^T - p_1^T
        // y * p_3^T - p_2^T
        // x' * p'_3^T - p'_1^T
        // y' * p'_3^T - p'_2^T
        //
        // where x,y and x', y' are the inhomogeneous scaled components of xHat and xHatPrime
        // ie if xHat = [a, b, c] --> x = a / c, y = b / c and similarly for xHatPrime
        // AND where p_n^T and p'_n^T are the nth rows of P and P' respectively where
        // P and P' are leftCameraMatrix and rightCameraMatrix respectively

        // A will be (4x4) -- that is each row of P is (1x4)
        RealVector [] topRows = makeVectorRowsInA(this.stereoCameraPair.getLeftCameraMatrix(), this.xHat);
        RealVector [] bottomRows = makeVectorRowsInA(this.stereoCameraPair.getRightCameraMatrix(), this.xHatPrime);
        A.setRowVector(0, topRows[0]);
        A.setRowVector(1, topRows[1]);
        A.setRowVector(2, bottomRows[0]);
        A.setRowVector(3, bottomRows[1]);


        // finally take the SVD of A
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(A);

        // X is the last column of V. where A = UDV^T (is SVD) [see page 593]
        RealMatrix v = singularValueDecomposition.getV();
        RealVector X = v.getColumnVector(v.getColumnDimension() - 1);
        this.mleX = Point3Space.getPointFromHomogeneousVector(X);
    }

    protected static RealVector[] makeVectorRowsInA(final RealMatrix cameraMatrix, RealMatrix imagePoint) {

        final double s = imagePoint.getEntry(2,0);
        final double x = imagePoint.getEntry(0,0) / s;
        final double y = imagePoint.getEntry(1,0) / s;

        // x * p_3^T - p_1^T
        // y * p_3^T - p_2^T
        RealVector p_1T = cameraMatrix.getRowVector(0);
        RealVector p_2T = cameraMatrix.getRowVector(1);
        RealVector p_3T = cameraMatrix.getRowVector(2);

        return new RealVector[]{
                p_3T.mapMultiply(x).subtract(p_1T),
                p_3T.mapMultiply(y).subtract(p_2T)
        };
    }



    protected void runAlgorithm() {

        // Step 1
        // Define rigid-body transformation matrices t, tp s.t. the points leftImagePoint and rightImagePoint are
        // transformed to their respective origins
        this.calculateInitialTransformationMatrices();

        // Step 2
        // Replace fundamentalMatrix by tp^(-T) * F * t^(-1) this new F is the fundamental RealMatrix in the
        // translated coordinate system
        this.translatefundamentalMatrix();

        // Step 3
        // Compute left and right epipoles using null spaces of new F, the normalize
        this.computeEpipoles();

        // Step 4
        // Form rotation matrices that will bring epipoles to x-axis
        this.computeRotationMatrices();

        // Step 5
        // Replace fundamentalMatrix by rp * fundamentalMatrix * r^T
        this.rotatefundamentalMatrix();

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

    // RESULT IS IN METERS
    @Override
    public Point3Space getPointInThreeSpace(StereoCameraPair stereoCameraPair,
                                            Point2Space leftImagePoint,
                                            Point2Space rightImagePoint) {


        // start be clearing state
        this.reset();

        // set left and right image point ivars, also transform into homogeneous coordinates
        this.leftImagePoint = leftImagePoint.getHomogeneousMatrixRepresentation();
        this.rightImagePoint = rightImagePoint.getHomogeneousMatrixRepresentation();
        this.stereoCameraPair = stereoCameraPair;
        this.fundamentalMatrix = stereoCameraPair.getFundamentalMatrix().copy();

        runAlgorithm();

        System.out.println("xHat:");
        System.out.println(Point2Space.getPointFromHomogeneousVector(this.xHat.getColumnVector(0)));
        System.out.println("xHatPrime:");
        System.out.println(Point2Space.getPointFromHomogeneousVector(this.xHatPrime.getColumnVector(0)));

        return convertMillimetersToMeters(this.mleX);
    }

    private static Point3Space convertMillimetersToMeters(Point3Space point) {
        return new Point3Space(
                point.getX() * 0.001,
                point.getY() * 0.001,
                point.getZ() * 0.001
        );
    }

    protected static RealMatrix rotationRealMatrixFromEpipole(RealMatrix epipole) {

        if (epipole.getColumnDimension() != 1 || epipole.getRowDimension() != 3) {
            throw new IllegalArgumentException("Epipole must be a 3 by 1 RealMatrix");
        }

        final double e1 = epipole.getEntry(0,0);
        final double e2 = epipole.getEntry(1,0);

        // forms rotation RealMatrix to bring epipole to x-axis
        // via formula in Book on page 318
        double [][] data = {
                {e1, e2, 0},
                {-e2, e1, 0},
                {0, 0, 1}
        };
        return new Array2DRowRealMatrix(data, false);
    }

    // makes 2-d homogenous point into normalized RealMatrix form s.t.
    // c0^2 + c1^2 = 1 (where c0 and c1 are first and second components)
    protected static RealMatrix normalizeAndMakeMatrix(double [] vec) {

        if ( vec.length != 3 ) {
            throw new IllegalArgumentException("vec must be of length 3");
        }

        final double scale = 1 / Math.sqrt(Math.pow(vec[0],2) + Math.pow(vec[1],2));
        final RealMatrix result = new Array2DRowRealMatrix(3, 1);
        for ( int row = 0; row < vec.length; row++ ) {
            result.setEntry(row, 0, vec[row] * scale);
        }
        return result;
    }

    protected static RealMatrix translationMatrix(final double x, final double y) {
        double [][] data = {
                {1,0,x},
                {0,1,y},
                {0,0,1}
        };
        return new Array2DRowRealMatrix(data, false);
    }

}
