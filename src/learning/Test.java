package learning;

import april.jmat.Matrix;
import learning.math.LinearEquation;
import learning.math.LinearRegression;
import learning.visualization.VisualizationController;
import learning.visualization.VisualizationFrame;

import javax.swing.*;

public class Test {

    public static void main(String [] args) {
        final double [][] xData = {{1, 4}, {3, 7}, {10, -19}};
        final double [][] yData = {{9}, {17}, {-28}};

        LinearRegression r = new LinearRegression(new Matrix(xData), new Matrix(yData));
        final LinearEquation eq = r.getResultantLinearEquation();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                double [] chartYData = new double[yData.length];
                for ( int i = 0; i < yData.length; i++ ) {
                    chartYData[i] = yData[i][0];
                }

                double [][] chartXData = new double[eq.getNumberOfDimensions()][xData.length];
                for ( int i = 0; i < xData.length; i++ ) {
                    for ( int d = 0; d < eq.getNumberOfDimensions(); d++ ) {
                        chartXData[d][i] = xData[i][d];
                    }
                }

                VisualizationFrame mainWindow = new VisualizationFrame();
                VisualizationController controller = new VisualizationController(mainWindow);
                controller.setEquation(eq, chartXData, chartYData);
                controller.show();
            }
        });

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
