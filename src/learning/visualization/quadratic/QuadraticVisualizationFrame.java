package learning.visualization.quadratic;

import javax.swing.*;
import java.awt.*;

public class QuadraticVisualizationFrame extends JFrame {

    protected JButton solveEquationForAnglesButton;
    protected JButton chooseDataFileButton;

    public QuadraticVisualizationFrame() {
        super("Data Visualization for Quadratic Learning");
        this.setLayout(new FlowLayout());

        this.solveEquationForAnglesButton = new JButton("Solve for Angles");
        this.add(this.solveEquationForAnglesButton);

        this.chooseDataFileButton = new JButton("Load File");
        this.add(this.chooseDataFileButton);
    }

    public JButton getSolveEquationForAnglesButton() {
        return solveEquationForAnglesButton;
    }

    public JButton getChooseDataFileButton() {
        return chooseDataFileButton;
    }

}
