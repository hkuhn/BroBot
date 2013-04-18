package learning;

import april.jmat.Matrix;
import learning.math.LinearEquation;
import learning.math.LinearRegression;
import learning.visualization.VisualizationController;
import learning.visualization.VisualizationFrame;

import javax.swing.*;
import java.util.Arrays;

public class Test {

    public static void main(String [] args) {
        final double [][] xData = {{1, 4}, {3, 7}, {10, -19}};
        final double [][] yData = {{9}, {17}, {-28}};

        final Matrix x = new Matrix(xData);
        final Matrix y = new Matrix(yData);

        LinearRegression r = new LinearRegression(x, y);
        final LinearEquation eq = r.getResultantLinearEquation();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VisualizationFrame mainWindow = new VisualizationFrame();
                VisualizationController controller = new VisualizationController(mainWindow);
                controller.setEquation(eq, x, y);
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
