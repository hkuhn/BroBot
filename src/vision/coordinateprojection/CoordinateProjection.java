package src.vision.coordinateprojection;

import java.awt.Point;
import java.awt.geom.*;

import april.jmat.Matrix;

public class CoordinateProjection {

    // const
    private static final double[][] identity = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 1}};
    
    // args
    private Matrix K_left;
    private Matrix K_right;
    private Matrix RT_left;     // [I | 0] -> transform wrt left camera
    private Matrix RT_right;
    private Matrix P_left;
    private Matrix P_right;
    
    // CONSTRUCTOR METHOD
    public CoordinateProjection(Matrix left_intrinsics, Matrix right_intrinsics, Matrix extrinsics) {
        // intrinsics matrix form (3x3):
        //  fx      skew        cx
        //  0       fy          cy
        //  0       0           1
        //
        // extrinsics matrix wrt left camera (3x4):
        //  cos(t)  -sin(t)     0   tx
        //  sin(t)  cos(t)      0   ty
        //  0       0           0   1
        //
        
        this.K_left = left_intrinsics;
        this.K_right = right_intrinsics;
        this.RT_left = new Matrix(identity);
        this.RT_right = extrinsics;
        this.P_left = K_left.times(RT_left);
        this.P_right = K_right.times(RT_right);
        
        
    }
    
    
    // ACCESS METHODS
    public double[] LinearTriangulation(Point2D pixel_coord_left, Point2D pixel_coord_right) {
        
        // init vars cleanly
        double x_left = pixel_coord_left.getX();
        double y_left = pixel_coord_left.getY();
        double x_right = pixel_coord_right.getX();
        double y_right = pixel_coord_right.getY();
        Matrix P_left_one_vec = P_left.getRow(1);
        Matrix P_left_two_vec = P_left.getRow(2);
        Matrix P_left_three_vec = P_left.getRow(3);
        Matrix P_right_one_vec = P_right.getRow(1);
        Matrix P_right_two_vec = P_right.getRow(2);
        Matrix P_right_three_vec = P_right.getRow(3);
        
        // run linear triangulation
        Matrix A_one_vec = P_left_three_vec.transpose()
                            .times(x_left)
                            .minus(P_left_one_vec.transpose());
        Matrix A_two_vec = P_left_three_vec.transpose()
                            .times(y_left)
                            .minus(P_left_two_vec.transpose());
        Matrix A_three_vec = P_right_three_vec.transpose()
                            .times(x_right)
                            .minus(P_right_one_vec.transpose());
        Matrix A_four_vec = P_right_three_vec.transpose()
                            .times(y_right)
                            .minus(P_right_two_vec.transpose());
        
        
        
        
        
        
        
    }
    
    public double[] LinearTriangulation(int[] pixel_coord_left, int[] pixel_coord_right) {
        
        
    }
	*/




}
