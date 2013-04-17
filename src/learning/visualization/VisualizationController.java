package learning.visualization;

import april.jmat.Matrix;
import learning.math.optimization.GradientDescent;
import learning.math.LinearEquation;
import learning.math.optimization.LinearEquationErrorGradientFunction;
import learning.math.OneDimensionalLinearEquation;
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
import java.text.DecimalFormat;

public class VisualizationController {

    private VisualizationFrame frame;
    private LinearEquation equation;
    private double [][] xData;
    private double [] yData;

    public VisualizationController(VisualizationFrame frame) {
        this.frame = frame;
        this.equation = null;
        this.frame.getDimensionChooserComboBox().setEnabled(false);

        this.frame.getDimensionChooserComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( getEquation() == null ) return;
                final int equationIndex = getFrame().getDimensionChooserComboBox().getSelectedIndex();
                OneDimensionalLinearEquation equationToDisplay = getEquation().getEquationForDimension(equationIndex);
                displayOneDimensionalEquation(equationToDisplay, equationIndex);
            }
        });

        this.frame.getSolveEquationForAnglesButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                calculateAnglesForUserSuppliedDistance();
            }
        });


    }

    protected void calculateAnglesForUserSuppliedDistance() {

        LinearEquation equation = getEquation();
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
                LinearEquationErrorGradientFunction gradientFunction = new LinearEquationErrorGradientFunction(equation, distance);
                GradientDescent gradientDescent = new GradientDescent(gradientFunction);
                final Matrix estimate = gradientDescent.estimateLocalMinimum();
                String estimateString = "[";
                for ( int i = 0; i < estimate.getRowDimension(); i++ ) {
                    if ( i > 0 ) estimateString += ", ";
                    estimateString += estimate.get(i, 0);
                }
                estimateString += "]";

                final double estimatedParamsResult = equation.getResult(LinearEquationErrorGradientFunction.makeArrayFromVectorMatrix(estimate));
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

    protected void displayOneDimensionalEquation(OneDimensionalLinearEquation equation, int dimension) {

        DefaultXYDataset dataSet = new DefaultXYDataset();
        dataSet.addSeries("Data", new double[][]{this.xData[dimension], this.yData});

        final String chartTitle = "Joint " + (dimension + 1) + " Angle Versus Throw Distance";
        final String xAxisLabel = "Joint " + (dimension + 1) + " Final Position";
        final String yAxisLabel = "Distance";

        // Create a single plot containing both the scatter and line
        XYPlot plot = new XYPlot();

        /* SETUP SCATTER */
        // Create the scatter data, renderer, and axis
        XYDataset collection1 = dataSet;
        XYItemRenderer renderer1 = new XYLineAndShapeRenderer(false, true);   // Shapes only
        ValueAxis domain1 = new NumberAxis(xAxisLabel);
        ValueAxis range1 = new NumberAxis(yAxisLabel);

        // Set the scatter data, renderer, and axis into plot
        plot.setDataset(0, collection1);
        plot.setRenderer(0, renderer1);
        plot.setDomainAxis(0, domain1);
        plot.setRangeAxis(0, range1);

        // Map the scatter to the first Domain and first Range
        plot.mapDatasetToDomainAxis(0, 0);
        plot.mapDatasetToRangeAxis(0, 0);

        /* SETUP LINE */

        // Create the line data, renderer, and axis
        XYDataset collection2 = DatasetUtilities.sampleFunction2D(
                new OneDLinearEquationFunctionAdapter(equation),
                domain1.getLowerBound(),
                domain1.getUpperBound(),
                1000,
                "data"
        );
        XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);   // Lines only
        ValueAxis domain2 = new NumberAxis(xAxisLabel);
        ValueAxis range2 = new NumberAxis(yAxisLabel);

        // Set the line data, renderer, and axis into plot
        plot.setDataset(1, collection2);
        plot.setRenderer(1, renderer2);
        plot.setDomainAxis(1, domain2);
        plot.setRangeAxis(1, range2);

        // Map the line to the second Domain and second Range
        plot.mapDatasetToDomainAxis(1, 1);
        plot.mapDatasetToRangeAxis(1, 1);

        // Create the chart with the plot and a legend
        JFreeChart chart = new JFreeChart(
                chartTitle,
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true
        );
        this.getFrame().getCenterChartPanel().setChart(chart);
    }

    public void setEquation(final LinearEquation equation, double [][] xData, double [] yData) {
        this.equation = equation;
        if ( this.equation == null ) {
            this.getFrame().getDimensionChooserComboBox().setEnabled(false);
            return;
        } else {
            this.getFrame().getDimensionChooserComboBox().setEnabled(true);
        }
        String [] options = new String[equation.getNumberOfDimensions()];
        for ( int i = 0; i < equation.getNumberOfDimensions(); i++ ) {
            options[i] = "Joint " + (i + 1) + " Angle";
        }
        this.xData = xData;
        this.yData = yData;
        this.getFrame().getDimensionChooserComboBox().setModel(new DefaultComboBoxModel(options));
        this.getFrame().getDimensionChooserComboBox().setSelectedIndex(0);
    }

    public LinearEquation getEquation() {
        return equation;
    }

    public void show() {
        if ( this.frame.isVisible() ) return;
        this.frame.setSize(1024, 768);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    public VisualizationFrame getFrame() {
        return frame;
    }

    protected class OneDLinearEquationFunctionAdapter implements Function2D {

        protected OneDimensionalLinearEquation equation;

        public OneDLinearEquationFunctionAdapter(OneDimensionalLinearEquation equation) {
            this.equation = equation;
        }

        public OneDimensionalLinearEquation getEquation() {
            return equation;
        }

        @Override
        public double getValue(double v) {
            return this.getEquation().getYValue(v);
        }
    }

}
