package learning.visualization.linear;

import javax.swing.*;

public class VisualizationMain {

    public static void main(String [] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VisualizationFrame mainWindow = new VisualizationFrame();
                VisualizationController appController = new VisualizationController(mainWindow);
                appController.show();
            }
        });

    }

}
