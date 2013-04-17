package learning;

import april.jmat.Matrix;
import java.lang.Math;

public class Test {

    public static void main(String [] args) {
        double [][] xData = {{1, 4}, {3, 7}, {10, -19}};
        double [][] yData = {{9}, {17}, {-28}};

	double[][] angles = { {0, 0, 0},
							{ 0, 0, Math.PI/8},
							{ 0, 0, Math.PI/4},
							{ Math.PI/8, 0, 0},
							{ -Math.PI/4, 0, 0}};

	double[][] distances = { {79}, {60}, {33}, {38}, {67.5} };

        LinearRegression r = new LinearRegression(new Matrix(angles), new Matrix(distances));
        LinearEquation eq = r.getResultantLinearEquation();

        for ( int i = 0; i < xData.length; i++ ) {
            double [] input = xData[i];
            String inputParamString = "";
            for ( int j = 0; j < input.length; j++ ) {
                if (j > 0) inputParamString += ", ";
                inputParamString += input[j];
            }
            System.out.println(eq.getResult(input) + "\t\t(" + inputParamString + ")");
        }
    }

}
