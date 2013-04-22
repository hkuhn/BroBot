package vision.datastructures;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * User: slessans
 * Date: 4/21/13
 * Time: 9:44 PM
 */
public final class OurStereoCameraPairFactory {

    public static StereoCameraPair getOurStereoCameraPair() {
        return StereoCameraPair.stereoCameraPairWithCameraMatrices(
                leftCameraInternalMatrix(),
                rightCameraInternalMatrix(),
                rotationMatrix(),
                translationX(),
                translationY(),
                translationZ()
        );
    }

    private static double translationX() {
        return -132.3906;
    }

    private static double translationY() {
        return -5.7399;
    }

    private static double translationZ() {
        return -3.9191;
    }

    private static RealMatrix rotationMatrix() {
        // 1.0000   -0.0016   -0.0056
        // 0.0016    1.0000   -0.0061
        // 0.0057    0.0061    1.0000
        double [][] data = {
                {1.0000,    -0.0016,    -0.0056},
                {0.0016,    1.0000,     -0.0061},
                {0.0057,    0.0061,     1.0000}
        };
        return new Array2DRowRealMatrix(data, false);
    }

    private static RealMatrix leftCameraInternalMatrix() {

        // 1.0e+03 *
        //
        // 1.8893         0    0.6342
        // 0    1.8916    0.4634
        // 0         0    0.0010

        double [][] data = {
                {1889.3,    0,      0634.2},
                {0,         1891.6, 0463.4},
                {0,         0,      1}
        };
        return new Array2DRowRealMatrix(data, false);
    }

    private static RealMatrix rightCameraInternalMatrix() {

        // 1.0e+03 *
        //
        //  1.7226  0       0.6375
        //  0       1.7255  0.4680
        //  0       0       0.0010

        double [][] data = {
                {1722.6,    0,      0637.5},
                {0,         1725.5, 0468.0},
                {0,         0,      1}
        };
        return new Array2DRowRealMatrix(data, false);
    }

}
