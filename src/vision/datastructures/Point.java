package vision.datastructures;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

/**
 * User: slessans
 * Date: 4/21/13
 * Time: 3:35 PM
 */
abstract public class Point {

    private final double [] values;

    protected Point(double [] values) {
        this.values = values;
    }

    /**
     * How many items in this n-tuple.
     * @return
     */
     public int getDimension() {
         return this.values.length;
     }

    /**
     * Gets the value at dimension d. For instance, (x,y) x is at dim 0.
     * @param d 0-based dimensions
     * @return
     */
    public double getValueInDimension(final int d) {
        return this.values[d];
    }

    /**
     * Gets vector representation of point
     * @return
     */
    public RealVector getVectorRepresentation() {
        return new ArrayRealVector(this.values);
    }

    /**
     * A column matrix representing this point
     * @return
     */
    public RealMatrix getMatrixRepresentation() {
        return new Array2DRowRealMatrix(this.values);
    }

    public RealVector getHomogeneousVectorRepresentation() {
        return new ArrayRealVector(this.values, new double[]{1});
    }

    /**
     * A column matrix representing this point in homogeneous coordinates
     * @return
     */
    public RealMatrix getHomogeneousMatrixRepresentation() {
        RealMatrix m = new Array2DRowRealMatrix(this.getDimension() + 1, 1);
        for ( int i = 0; i < this.getDimension(); i++ ) {
            m.setEntry(i, 0, this.values[i]);
        }
        m.setEntry(this.getDimension(), 0, 1);
        return m;
    }

    @Override
    public String toString() {
        return "Point{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
