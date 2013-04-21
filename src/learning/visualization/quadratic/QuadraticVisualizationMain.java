package learning.visualization.quadratic;

import javax.swing.*;

public class QuadraticVisualizationMain {

    public static void main(String [] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                QuadraticVisualizationFrame mainWindow = new QuadraticVisualizationFrame();
                QuadraticVisualizationController appController = new QuadraticVisualizationController(mainWindow);
                appController.show();
            }
        });

    }

}
