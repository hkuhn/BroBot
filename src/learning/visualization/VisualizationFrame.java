package learning.visualization;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;

public class VisualizationFrame extends JFrame {

    protected JButton solveEquationForAnglesButton;
    protected JPanel topPanel;
    protected JButton chooseDataFileButton;
    protected JComboBox dimensionChooserComboBox;
    protected ChartPanel centerChartPanel;

    public VisualizationFrame() {
        super("Data Visualization for Learning");
        this.setLayout(new BorderLayout());

        this.topPanel = new JPanel(new FlowLayout());

        this.centerChartPanel = new ChartPanel(null);
        this.add(this.centerChartPanel, BorderLayout.CENTER);

        this.dimensionChooserComboBox = new JComboBox();
        this.dimensionChooserComboBox.setSize(
                300,
                (int)this.dimensionChooserComboBox.getSize().getHeight()
        );
        this.topPanel.add(this.dimensionChooserComboBox, BorderLayout.NORTH);

        this.chooseDataFileButton = new JButton("Load File");
        this.topPanel.add(this.chooseDataFileButton);

        this.solveEquationForAnglesButton = new JButton("Solve for Angles");
        this.topPanel.add(this.solveEquationForAnglesButton);

        this.add(this.topPanel, BorderLayout.NORTH);
    }

    public JButton getSolveEquationForAnglesButton() {
        return solveEquationForAnglesButton;
    }

    public JButton getChooseDataFileButton() {
        return chooseDataFileButton;
    }

    public JComboBox getDimensionChooserComboBox() {
        return dimensionChooserComboBox;
    }

    public ChartPanel getCenterChartPanel() {
        return centerChartPanel;
    }

    protected void setCenterChartPanel(ChartPanel panel) {
        if ( this.centerChartPanel == panel ) return;
        if ( this.centerChartPanel != null ) this.remove(this.centerChartPanel);
        this.centerChartPanel = panel;
        if ( this.centerChartPanel != null ) {
            this.add(this.centerChartPanel, BorderLayout.CENTER);
        }
    }

    public JFreeChart getChart() {
        return this.getCenterChartPanel().getChart();
    }

    public void setChart(JFreeChart chart) {
        this.getCenterChartPanel().setChart(chart);
    }

}
