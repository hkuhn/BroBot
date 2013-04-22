package learning.visualization.quadratic;

import april.jmat.Matrix;
import learning.math.LinearEquation;
import learning.math.LinearRegression;
import learning.math.OneDimensionalLinearEquation;
import learning.math.Quadratic3DEquation;
import learning.math.optimization.GradientDescent;
import learning.math.optimization.LinearEquationErrorGradientFunction;
import learning.math.optimization.Quadratic3DEquationErrorGradientFunction;
import learning.util.DataReader;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

public class QuadraticVisualizationController {

    private QuadraticVisualizationFrame frame;
    private Quadratic3DEquation equation;

    public QuadraticVisualizationController(QuadraticVisualizationFrame frame) {
        this.frame = frame;
        this.equation = null;

        this.frame.getSolveEquationForAnglesButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                calculateAnglesForUserSuppliedDistance();
            }
        });

        this.frame.getChooseDataFileButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadDataFromUserChosenFile();
            }
        });


    }

    protected void loadDataFromUserChosenFile() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showDialog(this.getFrame(), "Load");

        File selectedFile = fileChooser.getSelectedFile();
        if ( result != JFileChooser.APPROVE_OPTION || selectedFile == null ) return;

        DataReader parser = new DataReader(selectedFile);

        try {
            parser.parse();

            Matrix x = parser.getParsedInput();
            Matrix y = parser.getParsedOutput();


            LinearRegression r = new LinearRegression(Quadratic3DEquation.getLinearRegressionInputFromSampleData(x), y);
            Quadratic3DEquation eq = Quadratic3DEquation.getQuadraticEquationFromLinearRegression(r);

            System.out.println("Parsed new data, got linear equation: " + eq);


            this.setEquation(eq);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this.getFrame(),
                    e.getLocalizedMessage(),
                    "Uh, Oh!",
                    JOptionPane.ERROR_MESSAGE
            );
        }


    }

    protected void calculateAnglesForUserSuppliedDistance() {

        Quadratic3DEquation equation = getEquation();
        if ( equation == null ) return;

        // get distance from the user of the interface
        String input = JOptionPane.showInputDialog(
                this.getFrame(),
                "Enter the target distance",
                "Target Distance",
                JOptionPane.PLAIN_MESSAGE
        );

        if ( input != null ) {
            try {

                final double distance = Double.parseDouble(input);

                Quadratic3DEquationErrorGradientFunction gradientFunction = new Quadratic3DEquationErrorGradientFunction(equation, distance);
                GradientDescent gradientDescent = new GradientDescent(gradientFunction);

                final Matrix estimate = gradientDescent.estimateLocalMinimum();
                String estimateString = "[";
                for ( int i = 0; i < estimate.getRowDimension(); i++ ) {
                    if ( i > 0 ) estimateString += ", ";
                    estimateString += estimate.get(i, 0);
                }
                estimateString += "]";

                final double estimatedParamsResult = equation.getResult(
                        estimate.get(0,0),
                        estimate.get(1,0),
                        estimate.get(2,0)
                );
                final double finalError = (estimatedParamsResult - distance);
                final double percentageError = (distance == 0) ? 0 : (finalError / distance) * 100;

                DecimalFormat formatter = new DecimalFormat();
                formatter.setMaximumFractionDigits(4);

                JOptionPane.showMessageDialog(
                        this.getFrame(),
                        "The final computed parameter estimate for target distance " +
                                distance + " is:\n" + estimateString +
                                "\n\nThe estimated parameters give result: " + formatter.format(estimatedParamsResult) +
                                "\nFinal error: " + formatter.format(finalError) + " (" + formatter.format(percentageError) + "%)",
                        "Results",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (NumberFormatException e) {
                e.printStackTrace(System.out);
            }
        }

    }

    /**
     * @param equation
     */
    public void setEquation(final Quadratic3DEquation equation) {
        this.equation = equation;
        this.getFrame().getSolveEquationForAnglesButton().setEnabled(this.equation != null);
    }

    public Quadratic3DEquation getEquation() {
        return equation;
    }

    public void show() {
        if ( this.frame.isVisible() ) return;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public QuadraticVisualizationFrame getFrame() {
        return frame;
    }

}
