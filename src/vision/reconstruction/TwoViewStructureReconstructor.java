package vision.reconstruction;

import vision.datastructures.Point2Space;
import vision.datastructures.Point3Space;
import vision.datastructures.StereoCameraPair;

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
     * @param stereoCameraPair the information about the stereo
     * @return the point in 3-space, NOT homogeneous (3 by 1 matrix)
     */
    public Point3Space getPointInThreeSpace(StereoCameraPair stereoCameraPair,
                                            Point2Space leftImagePoint,
                                            Point2Space rightImagePoint);

}
