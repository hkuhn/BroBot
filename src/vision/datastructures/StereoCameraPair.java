package vision.datastructures;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * User: slessans
 * Date: 4/21/13
 * Time: 3:08 PM
 */
public class StereoCameraPair {

    private final RealMatrix leftCameraMatrix;
    private final RealMatrix rightCameraMatrix;
    private final RealMatrix fundamentalMatrix;

    public StereoCameraPair(
            final RealMatrix leftCameraMatrix,
            final RealMatrix rightCameraMatrix,
            final RealMatrix fundamentalMatrix) {

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
}
