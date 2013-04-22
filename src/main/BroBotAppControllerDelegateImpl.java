package main;


import datatypes.Circle;
import vision.datastructures.OurStereoCameraPairFactory;
import vision.datastructures.Point2Space;
import vision.datastructures.Point3Space;
import vision.datastructures.StereoCameraPair;
import vision.reconstruction.OptimalTriangulationMethod;
import vision.ringdetection.RingDetectionDetector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BroBotAppControllerDelegateImpl implements BroBotAppControllerDelegate {

    private static final long RingDetectionInterval = 10000;
    private static final int WhiteThresholdRight = 218;  // vary this parameter to detect rings
    private static final int WhiteThresholdLeft = 213;  // vary this parameter to detect rings

    private static final int DotDim = 2;

    private StereoCameraPair cameraPair;
    private long lastRingDetectionTime;

    private BroBotApplicationController appController;
    private BroBotController botController;

    public BroBotAppControllerDelegateImpl() {
        lastRingDetectionTime = 0;
        cameraPair = OurStereoCameraPairFactory.getOurStereoCameraPair();
        appController = null;
        botController = null;
    }

    @Override
    public void setAppController(BroBotApplicationController controller) {
        appController = controller;
    }

    @Override
    public void setBotController(BroBotController controller) {
        botController = controller;
    }

    @Override
    public BufferedImage[] processImages(BufferedImage leftImage, BufferedImage rightImage) {

        long currentTime = System.currentTimeMillis();

        if ( (currentTime - lastRingDetectionTime) >= RingDetectionInterval ) {

            RingDetectionDetector ringDetectorLeft = new RingDetectionDetector();
            System.out.println("Running left image detection");
            ringDetectorLeft.runDetection(leftImage, WhiteThresholdLeft);
            ArrayList<Circle> leftImageCircles = ringDetectorLeft.getCircles();
            System.out.println("\tDone detection, found " + leftImageCircles.size() + " circles.");

            RingDetectionDetector ringDetectorRight = new RingDetectionDetector();
            System.out.println("Running right image detection");
            ringDetectorRight.runDetection(rightImage, WhiteThresholdRight);
            ArrayList<Circle> rightImageCircles = ringDetectorRight.getCircles();
            System.out.println("\tDone detection, found " + rightImageCircles.size() + " circles.");

            Comparator<Circle> circleComparator = new Comparator<Circle>() {
                @Override
                public int compare(Circle circle, Circle circle2) {
                    if ( circle.getCenter().getX() < circle2.getCenter().getX() ) {
                        return -1;
                    } else if ( circle.getCenter().getX() == circle2.getCenter().getX() ) {
                        return 0;
                    }
                    return 1;
                }
            };

            System.out.println("Sorting circles");
            Collections.sort(leftImageCircles, circleComparator);
            Collections.sort(rightImageCircles, circleComparator);

            System.out.println("Finding 3d correspondences");

            final int len = Math.min(leftImageCircles.size(), rightImageCircles.size());

            double rightAvgX = 0.0;
            double rightAvgY = 0.0;
            int numRight = 0;

            double leftAvgX = 0.0;
            double leftAvgY = 0.0;
            int numLeft = 0;

            for ( int i = 0; i < len; i++ ) {

                Circle leftCircle = leftImageCircles.get(i);
                Circle rightCircle = rightImageCircles.get(i);

                final double leftX = leftCircle.getCenter().getX();
                final double leftY = leftCircle.getCenter().getY();
                final double rightX = rightCircle.getCenter().getX();
                final double rightY = rightCircle.getCenter().getY();

                if ( leftX != Double.POSITIVE_INFINITY && leftY != Double.POSITIVE_INFINITY ) {
                    leftAvgX += leftX;
                    leftAvgY += leftY;
                    numLeft++;
                }

                if ( rightX != Double.POSITIVE_INFINITY && rightY != Double.POSITIVE_INFINITY ) {
                    rightAvgX += rightX;
                    rightAvgY += rightY;
                    numRight++;
                }
            }

            if ( numRight > 0 && numLeft > 0 ) {

                System.out.println("RARARARA ----------------------------------------------------");
                leftImage = ringDetectorLeft.getThinnedImage();
                rightImage = ringDetectorRight.getThinnedImage();

                Point2Space leftAvgPoint = new Point2Space(leftAvgX / numLeft, leftAvgY / numLeft);
                Point2Space rightAvgPoint = new Point2Space(rightAvgX / numLeft, rightAvgY / numLeft);

                System.out.println("\tleftPoint: " + leftAvgPoint);
                System.out.println("\trightPoint: " + rightAvgPoint);

                drawCircle(leftImage, leftAvgPoint);
                drawCircle(rightImage, rightAvgPoint);

                OptimalTriangulationMethod optimalTriangulationMethod = new OptimalTriangulationMethod(this.cameraPair, leftAvgPoint, rightAvgPoint);
                if ( botController != null ) {
                    botController.setTarget(optimalTriangulationMethod.getMLEPointIn3Space());
                }
            } else {
                leftImage = null;
                rightImage = null;
            }

            lastRingDetectionTime = System.currentTimeMillis();

        } else {
            leftImage = null;
            rightImage = null;
        }

        return new BufferedImage[]{leftImage, rightImage};

    }

    public static void drawCircle(BufferedImage image, Point2Space point) {

        int x = (int)point.getX();
        int y = (int)point.getY();

        if ( (x-DotDim) < 0 ) x = DotDim;
        if ( x + DotDim >= image.getWidth() ) x = image.getWidth() - DotDim -1;

        if ( (y-DotDim) < 0 ) y = DotDim;
        if ( y + DotDim >= image.getHeight() ) y = image.getHeight() - DotDim -1;

        for ( int cx = (x - DotDim); cx < (x + DotDim); cx++ ) {
            for ( int cy = (y - DotDim); cy < (y + DotDim); cy++ ) {
                image.setRGB(cx, cy, 0xFF00FF00);
            }
        }
    }

}
