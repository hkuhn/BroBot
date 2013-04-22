package learning.visualization;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;

public class VisualizationFrame extends JFrame {

    protected JComboBox dimensionChooserComboBox;
    protected ChartPanel centerChartPanel;

    public VisualizationFrame() {
        super("Data Visualization for Learning");
        this.setLayout(new BorderLayout());

        this.centerChartPanel = new ChartPanel(null);
        this.add(this.centerChartPanel, BorderLayout.CENTER);

        this.dimensionChooserComboBox = new JComboBox();
        this.add(this.dimensionChooserComboBox, BorderLayout.NORTH);
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
