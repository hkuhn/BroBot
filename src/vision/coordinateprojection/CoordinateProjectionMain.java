package vision.coordinateprojection;

import javax.swing.*;
import java.util.ArrayList;
import java.lang.*;


public class CoordinateProjectionMain {
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // create app frame
                CoordinateProjectionFrame frame = new CoordinateProjectionFrame();
                // build controller
                CoordinateProjectionController appController = new CoordinateProjectionController(frame);
            }
        });
    }
}
