package learning;

import april.jmat.Matrix;

public class Test {

    public static void main(String [] args) {
        double [][] xData = {{1, 4}, {3, 7}, {10, -19}};
        double [][] yData = {{9}, {17}, {-28}};

        LinearRegression r = new LinearRegression(new Matrix(xData), new Matrix(yData));
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
