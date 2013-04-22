package main;


import vision.ringdetection.RingDetectionDetector;

import java.awt.image.BufferedImage;

public class BroBotAppControllerDelegateImpl implements BroBotAppControllerDelegate {

    private static final long RingDetectionInterval = 3000;
    private static final int WhiteThreshold = 230;  // vary this parameter to detect rings


    private RingDetectionDetector ringDetector;
    private long lastRingDetectionTime;

    public BroBotAppControllerDelegateImpl() {
        ringDetector = new RingDetectionDetector();
        lastRingDetectionTime = 0;
    }

    @Override
    public BufferedImage[] processImages(BufferedImage leftImage, BufferedImage rightImage) {

        long currentTime = System.currentTimeMillis();

        if ( (currentTime - lastRingDetectionTime) >= RingDetectionInterval ) {
            System.out.println("Running left image detection");
            ringDetector.runDetection(leftImage, WhiteThreshold);
            System.out.println("\tDone detection, found " + ringDetector.getCircles().size() + " circles.");

            System.out.println("Running right image detection");
            ringDetector.runDetection(rightImage, WhiteThreshold);
            System.out.println("\tDone detection, found " + ringDetector.getCircles().size() + " circles.");

            lastRingDetectionTime = System.currentTimeMillis();
        }



        return new BufferedImage[]{leftImage, rightImage};

    }
}
