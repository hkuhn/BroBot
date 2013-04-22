package main;


import datatypes.Circle;
import vision.datastructures.OurStereoCameraPairFactory;
import vision.datastructures.Point2Space;
import vision.datastructures.Point3Space;
import vision.datastructures.StereoCameraPair;
import vision.reconstruction.OptimalTriangulationMethod;
import vision.reconstruction.TwoViewStructureReconstructor;
import vision.ringdetection.RingDetectionDetector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BroBotAppControllerDelegateImpl implements BroBotAppControllerDelegate {

    private static final long RingDetectionInterval = 3000;
    private static final int WhiteThreshold = 230;  // vary this parameter to detect rings


    private RingDetectionDetector ringDetector;
    private long lastRingDetectionTime;
    private TwoViewStructureReconstructor structureReconstructor;
    private StereoCameraPair cameraPair;

    public BroBotAppControllerDelegateImpl() {
        ringDetector = new RingDetectionDetector();
        lastRingDetectionTime = 0;
        structureReconstructor = new OptimalTriangulationMethod();
        cameraPair = OurStereoCameraPairFactory.getOurStereoCameraPair();
    }

    @Override
    public BufferedImage[] processImages(BufferedImage leftImage, BufferedImage rightImage) {

        long currentTime = System.currentTimeMillis();

        if ( (currentTime - lastRingDetectionTime) >= RingDetectionInterval ) {
            System.out.println("Running left image detection");
            ringDetector.runDetection(leftImage, WhiteThreshold);
            leftImage = ringDetector.getThinnedImage();
            ArrayList<Circle> leftImageCircles = ringDetector.getCircles();
            System.out.println("\tDone detection, found " + leftImageCircles.size() + " circles.");

            System.out.println("Running right image detection");
            ringDetector.runDetection(rightImage, WhiteThreshold);
            rightImage = ringDetector.getThinnedImage();
            ArrayList<Circle> rightImageCircles = ringDetector.getCircles();
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
            for ( int i = 0; i < len; i++ ) {

                Circle leftCircle = leftImageCircles.get(0);
                Circle righCircle = rightImageCircles.get(0);

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

                Point3Space point = structureReconstructor.getPointInThreeSpace(this.cameraPair, leftCenter, rightCenter);
                System.out.println("Cup " + i + ": " + point);
            }

            lastRingDetectionTime = System.currentTimeMillis();
        }



        return new BufferedImage[]{leftImage, rightImage};

    }
}
