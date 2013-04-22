package vision.datastructures;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * User: slessans
 * Date: 4/21/13
 * Time: 3:08 PM
 */
public class StereoCameraPair {

    private static final String InvalidCameraMatrixMessage = "camera matrix must be a non-null, 3 by 4 real matrix.";
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

    public RealMatrix getLeftCameraMatrix() {
        return leftCameraMatrix;
    }

    public RealMatrix getRightCameraMatrix() {
        return rightCameraMatrix;
    }

    public RealMatrix getFundamentalMatrix() {
        return fundamentalMatrix;
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
