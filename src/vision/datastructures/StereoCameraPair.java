package vision.datastructures;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import vision.util.MatrixUtils;

/**
 * User: slessans
 * Date: 4/21/13
 * Time: 3:08 PM
 */
public class StereoCameraPair {

    private static final String InvalidCameraMatrixMessage = "camera matrix must be a non-null, 3 by 4 real matrix.";
    private static final String InvalidInternalCameraMatrixMessage = "internal camera matrix must be a non-null, 3 by 3 real matrix.";
    private static final String InvalidFundamentalMatrixMessage = "fundamental matrix must be a non-null, 3 by 3 real matrix.";

    private final RealMatrix leftCameraMatrix;
    private final RealMatrix rightCameraMatrix;
    private final RealMatrix fundamentalMatrix;

    public StereoCameraPair(
            final RealMatrix leftCameraMatrix,
            final RealMatrix rightCameraMatrix,
            final RealMatrix fundamentalMatrix) {

        if ( ! checkThatCameraMatrixIsValid(leftCameraMatrix) ) {
            throw new IllegalArgumentException("Error with left camera matrix: " + InvalidCameraMatrixMessage);
        }

        if ( ! checkThatCameraMatrixIsValid(rightCameraMatrix) ) {
            throw new IllegalArgumentException("Error with right camera matrix: " + InvalidCameraMatrixMessage);
        }

        if ( ! checkThatfundamentalMatrixIsValid(fundamentalMatrix) ) {
            throw new IllegalArgumentException("Error with fundamental matrix: " + InvalidFundamentalMatrixMessage);
        }

        this.leftCameraMatrix = leftCameraMatrix;
        this.rightCameraMatrix = rightCameraMatrix;
        this.fundamentalMatrix = fundamentalMatrix;

    }

    /**
     * Will compute the fundamental matrix given cameras not at infinity property on page 246 of
     * R. Hartley and A. Zisserman. Multiple View Geometry in Computer Vision. Academic Press, 2002
     *
     * @param leftCameraInternalMatrix
     * @param rightCameraInternalMatrix
     * @param rotationTransformation from left camera to right camera, assuming left at origin
     * @param translationX from left camera to right camera, assuming left at origin
     * @param translationY from left camera to right camera, assuming left at origin
     * @return
     */
    public static StereoCameraPair stereoCameraPairWithCameraMatrices(
            RealMatrix leftCameraInternalMatrix,
            RealMatrix rightCameraInternalMatrix,
            RealMatrix rotationTransformation,
            double translationX,
            double translationY,
            double translationZ
    ) {

        if ( ! checkThatCameraInternalMatrixIsValid(leftCameraInternalMatrix) ) {
            throw new IllegalArgumentException("Error with left internal camera matrix: " + InvalidInternalCameraMatrixMessage);
        }

        if ( ! checkThatCameraInternalMatrixIsValid(rightCameraInternalMatrix) ) {
            throw new IllegalArgumentException("Error with right internal camera matrix: " + InvalidInternalCameraMatrixMessage);
        }

        if ( rotationTransformation.getRowDimension() != 3 || rotationTransformation.getColumnDimension() != 3 ) {
            throw new IllegalArgumentException("Error with rotation transformation, must be 3 by 3");
        }

        RealMatrix translationMatrix = new Array2DRowRealMatrix(3, 1);
        translationMatrix.setEntry(0, 0, translationX);
        translationMatrix.setEntry(1, 0, translationY);
        translationMatrix.setEntry(2, 0, translationZ);

        RealMatrix fundamentalMatrix =
                MatrixUtils.calculateInverse(rightCameraInternalMatrix).transpose()
                .multiply(rotationTransformation)
                        .multiply(leftCameraInternalMatrix.transpose());


        RealMatrix KRTt = MatrixUtils.skewSymmetricMatrix(leftCameraInternalMatrix.multiply(rotationTransformation.transpose()).multiply(translationMatrix));

        fundamentalMatrix = fundamentalMatrix.multiply(KRTt);

        RealMatrix leftCameraMatrix = makeCameraMatrix(leftCameraInternalMatrix, null, null);
        RealMatrix rightCameraMatrix = makeCameraMatrix(rightCameraInternalMatrix, rotationTransformation, translationMatrix);

        return new StereoCameraPair(leftCameraMatrix, rightCameraMatrix, fundamentalMatrix);
    }

    private static RealMatrix makeCameraMatrix(RealMatrix internalMatrix, RealMatrix rotationMatrix33, RealMatrix translationCol) {

        // P = K[R | I]
        RealVector translationVector = null;
        if ( translationCol == null ) {
            translationVector = new ArrayRealVector(new double[]{0, 0, 0}, false);
        } else {
            translationVector = translationCol.getColumnVector(0);
        }

        if ( rotationMatrix33 == null ) {
            rotationMatrix33 = MatrixUtils.createIdentityMatrix(3);
        }

        // [R | I]
        RealMatrix RI = new Array2DRowRealMatrix(3, 4);
        RI.setSubMatrix(rotationMatrix33.getData(), 0, 0);
        RI.setColumnVector(3, translationVector);

        //
        return internalMatrix.multiply(RI);
    }

    public RealMatrix getLeftCameraMatrix() {
        return leftCameraMatrix;
    }

    public RealMatrix getRightCameraMatrix() {
        return rightCameraMatrix;
    }

    public RealMatrix getFundamentalMatrix() {
        return fundamentalMatrix;
    }


    private static boolean checkThatCameraInternalMatrixIsValid(final RealMatrix cameraRealMatrix) {

        if ( cameraRealMatrix == null ) return false;
        if ( cameraRealMatrix.getRowDimension() != 3 ) return false;
        if ( cameraRealMatrix.getColumnDimension() != 3 ) return false;

        return true;
    }

    private static boolean checkThatCameraMatrixIsValid(final RealMatrix cameraRealMatrix) {

        if ( cameraRealMatrix == null ) return false;
        if ( cameraRealMatrix.getRowDimension() != 3 ) return false;
        if ( cameraRealMatrix.getColumnDimension() != 4 ) return false;

        return true;
    }

    private static boolean checkThatfundamentalMatrixIsValid(final RealMatrix fundamentalMatrix) {
        if ( fundamentalMatrix == null ) return false;
        if ( fundamentalMatrix.getRowDimension() != 3 ) return false;
        if ( fundamentalMatrix.getColumnDimension() != 3 ) return false;
        return true;
    }

}
