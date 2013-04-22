package vision.datastructures;

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * User: slessans
 * Date: 4/21/13
 * Time: 3:55 PM
 */
public class Point3Space extends Point {

    public Point3Space(final double x, final double y, final double z) {
        super(new double[]{x,y,z});
    }

    public double getX() {
        return this.getValueInDimension(0);
    }

    public double getY() {
        return this.getValueInDimension(1);
    }

    public double getZ() {
        return this.getValueInDimension(2);
    }

    public static Point3Space getPointFromHomogeneousVector(RealVector vector) {
        final double x = vector.getEntry(0);
        final double y = vector.getEntry(1);
        final double z = vector.getEntry(2);
        final double s = vector.getEntry(3);
        return new Point3Space(x / s, y / s, z / s);
    }

    @Override
    public String toString() {
        return "{Point3Space: x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + "}";
    }

}
