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

            Point3Space minErrorPoint = null;
            double minError = 0;

            for ( int i = 0; i < len; i++ ) {

                Circle leftCircle = leftImageCircles.get(i);
                Circle righCircle = rightImageCircles.get(i);

                final double leftX = leftCircle.getCenter().getX();
                final double leftY = leftCircle.getCenter().getY();
                final double rightX = righCircle.getCenter().getX();
                final double rightY = righCircle.getCenter().getY();

                if ( leftX == Double.POSITIVE_INFINITY || leftY == Double.POSITIVE_INFINITY ||
                        rightX == Double.POSITIVE_INFINITY || rightY == Double.POSITIVE_INFINITY ) {
                    System.out.println("Circles at index " + i + " aren't existent (positive inf)");
                    continue;
                }

                Point2Space leftCenter = new Point2Space(leftX, leftY);
                Point2Space rightCenter = new Point2Space(rightX, rightY);

                OptimalTriangulationMethod optimalTriangulationMethod = new OptimalTriangulationMethod(this.cameraPair, leftCenter, rightCenter);
                Point3Space point = optimalTriangulationMethod.getMLEPointIn3Space();

                final double error = optimalTriangulationMethod.getError();
                if ( minErrorPoint == null || error < minError ) {
                    minErrorPoint = point;
                    minError = error;
                }

            }

            if ( botController != null && minErrorPoint != null ) {
                botController.setTarget(minErrorPoint);
            }


            leftImage = ringDetectorLeft.getThinnedImage();
            rightImage = ringDetectorRight.getThinnedImage();

            lastRingDetectionTime = System.currentTimeMillis();

        } else {
            leftImage = null;
            rightImage = null;
        }

        return new BufferedImage[]{leftImage, rightImage};

    }
}
