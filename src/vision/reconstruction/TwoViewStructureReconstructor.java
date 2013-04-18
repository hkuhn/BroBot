package vision.reconstruction;

import april.jmat.Matrix;

/**
 * User: slessans
 * Date: 4/18/13
 * Time: 1:27 AM
 */
public interface TwoViewStructureReconstructor {

    /**
     * For two corresponding points x<==>x' from left and right images respectively,
     * finds the point X s.t. X is the point in 3-space where the image points x and x'
     * originated from.
     *
     * @param leftImagePoint image point in left image, NOT homogeneous (2 by 1 matrix)
     * @param rightImagePoint image point in right image, NOT homogeneous (2 by 1 matrix)
     * @param fundamentalMatrix the fundamental matrix between left and right camera
     * @return the point in 3-space, NOT homogeneous (3 by 1 matrix)
     */
    public Matrix getPointInThreeSpace(Matrix leftImagePoint, Matrix rightImagePoint, Matrix fundamentalMatrix);

}
