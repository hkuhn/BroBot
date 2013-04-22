package main;

import vision.balldetection.*;
import vision.util.*;

import java.awt.image.BufferedImage;
import java.awt.Point;

public class BroBotBallDetection {

    // const
    private static final int blueThreshold = 255;
    
    // args
    private Point center;
    
    
    // CONSTRUCTOR METHOD
    public BroBotBallDetection(BufferedImage im) {
        // run image binarization with blue thresh
        binarize b = new binarize(im, blueThreshold);
        BufferedImage out = b.getBinarizedImage();
        
		
        // run ball detection
        BallDetectionDetector_Expand bdd = new BallDetectionDetector_Expand();
        bdd.setImage(out);
        
        
        bdd.runDetection();
        this.center = bdd.getCenter();

    }
    
    // ACCESS METHOD
    public Point getCenter() {
        return center;
    }



}