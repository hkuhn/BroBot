package learning.math;

import april.jmat.Matrix;
import april.jmat.Vec;

import java.util.Arrays;

public class Quadratic3DEquation {

    private final double xC;
    private final double yC;
    private final double zC;
    private final double xSquaredC;
    private final double ySquaredC;
    private final double zSquaredC;
    private final double xyC;
    private final double xzC;
    private final double yzC;
    private final double constant;

    public Quadratic3DEquation(double xC, double yC, double zC, double xSquaredC, double ySquaredC, double zSquaredC, double xyC, double xzC, double yzC, double constant) {
        this.xC = xC;
        this.yC = yC;
        this.zC = zC;
        this.xSquaredC = xSquaredC;
        this.ySquaredC = ySquaredC;
        this.zSquaredC = zSquaredC;
        this.xyC = xyC;
        this.xzC = xzC;
        this.yzC = yzC;
        this.constant = constant;
    }

    /**
     * Assuming that each input row is of the form (x,y,z)
     * @param input n rows, 3 cols each row is sample of form (x,y,z)
     * @return
     */
    public static Matrix getLinearRegressionInputFromSampleData(Matrix input) {

        // change to new matrix where each row is of form (x, y, z, xy, xz, yz, x^2, y^2, z^2)
        // the linear regression will add 1 to turn into 9 cols at beginning
        final int numRows = input.getRowDimension();

        double [][] linRegInputData = new double[numRows][8];
        for (int row = 0; row < numRows; row++) {
            Vec inputRow = input.getRow(row);
            final double x = inputRow.get(0);
            final double y = inputRow.get(1);
            final double z = inputRow.get(2);
            linRegInputData[row] = new double[]{x, y, z, x*y, x*z, y*z, x * x, y * y, z * z};
        }

        return new Matrix(linRegInputData);
    }

    /**
     * Assumes regression input was formed with getLinearRegressionInputFromSampleData
     * @param regression
     * @return
     */
    public static Quadratic3DEquation getQuadraticEquationFromLinearRegression(LinearRegression regression) {

        Vec result = regression.getResult().getColumn(0);

        double constant = result.get(0);
        double xC = result.get(1);
        double yC = result.get(2);
        double zC = result.get(3);
        double xyC = result.get(4);
        double xzC = result.get(5);
        double yzC = result.get(6);
        double xSquaredC = result.get(7);
        double ySquaredC = result.get(8);
        double zSquaredC = result.get(9);

        return new Quadratic3DEquation(xC, yC, zC, xSquaredC, ySquaredC, zSquaredC, xyC, xzC, yzC, constant);
    }

    public double getConstant() {
        return constant;
    }

    public double getxC() {
        return xC;
    }

    public double getyC() {
        return yC;
    }

    public double getzC() {
        return zC;
    }

    public double getxSquaredC() {
        return xSquaredC;
    }

    public double getySquaredC() {
        return ySquaredC;
    }

    public double getzSquaredC() {
        return zSquaredC;
    }

    public double getXyC() {
        return xyC;
    }

    public double getXzC() {
        return xzC;
    }

    public double getYzC() {
        return yzC;
    }

    public double getResult(double x, double y, double z) {

        double sum = this.constant +
                this.xSquaredC * x * x +
                this.ySquaredC * y * y +
                this.zSquaredC * z * z +
                this.xyC * x * y +
                this.xzC * x * z +
                this.yzC * y * z +
                this.xC * x +
                this.yC * y +
                this.zC * z;

        return sum;
    }

    @Override
    public String toString() {
        return "Quadratic3DEquation{" +
                "xC=" + xC +
                ", yC=" + yC +
                ", zC=" + zC +
                ", xSquaredC=" + xSquaredC +
                ", ySquaredC=" + ySquaredC +
                ", zSquaredC=" + zSquaredC +
                ", xyC=" + xyC +
                ", xzC=" + xzC +
                ", yzC=" + yzC +
                ", constant=" + constant +
                '}';
    }
}
