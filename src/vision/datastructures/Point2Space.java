package vision.datastructures;

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

    @Override
    public String toString() {
        return "{Point2Space: x=" + this.getX() + ", y=" + this.getY() + "}";
    }


}
