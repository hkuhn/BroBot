package Away.Vision.BallDetection;

import javax.swing.*;
import java.util.ArrayList;
import java.lang.*;

public class BallDetectionMain {

	// main function
	public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // create app frame
                BallDetectionFrame frame = new BallDetectionFrame();
                // build controller
                //BallDetectionController appController = new BallDetectionController(frame);
                
            }
        });
    }




}
