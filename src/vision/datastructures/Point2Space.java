package vision.datastructures;

import org.apache.commons.math3.linear.RealVector;

/**
 * User: slessans
 * Date: 4/21/13
 * Time: 3:53 PM
 */
public class Point2Space extends Point {

    public Point2Space(final double x, final double y) {
        super(new double[]{x,y});
    }

    public double getX() {
        return this.getValueInDimension(0);
    }

    public double getY() {
        return this.getValueInDimension(1);
    }

    public static Point2Space getPointFromHomogeneousVector(RealVector vector) {
        final double x = vector.getEntry(0);
        final double y = vector.getEntry(1);
        final double s = vector.getEntry(2);
        return new Point2Space(x / s, y / s);
    }

    @Override
    public String toString() {
        return "{Point2Space: x=" + this.getX() + ", y=" + this.getY() + "}";
    }


}
