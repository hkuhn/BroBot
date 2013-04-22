package main;

import vision.ringdetection.*;
import datatypes.*;

import java.util.ArrayList;

public class BroBotRingDetection {

    // const
    private static final int whiteThreshold = 230;  // vary this parameter to detect rings
    
    // args
    private ArrayList<Circle> circles_list;
    
    
    // CONSTRUCTOR METHOD
    public BroBotRingDetection(BufferedImage im) {
        
        this.circles_list = new ArrayList<Circle>();
        
        RingDetectionDetector rdd = new RingDetectionDetector();
		rdd.runDetection(im, this.whiteThreshold);
        circles_list = rdd.getCircles().clone();   // returns 3 circles
		
    }
    
    // ACCESS METHODS
    public ArrayList<Circle> getCircles() {
        return circles_list;
    }

}