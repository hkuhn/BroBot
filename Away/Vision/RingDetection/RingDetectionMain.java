package Away.Vision.RingDetection;

import Away.Vision.util;

import javax.swing.*;
import java.util.ArrayList;
import java.lang.*;


public class RingDetectionMain {
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // create app frame
                RingDetectionFrame frame = new RingDetectionFrame();
                // build controller
                RingDetectionController appController = new RingDetectionController(frame);
                
            }
        });
    }
}
