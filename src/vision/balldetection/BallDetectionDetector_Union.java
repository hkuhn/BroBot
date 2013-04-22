package vision.balldetection;

import datatypes.*;

import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Point;
import static java.lang.Math.*;




public class BallDetectionDetector_Union {

    // NOTE: No threshold is needed
    //          test R = 0, G = 0, B = 255
    //
    
    // args
    private BufferedImage im;
    private int width;
    private int height;
    
    
    // CONSTRUCTOR METHOD
    public BallDetectionDetector_Union() {
        
    }
    
 /*   
    // PUBLIC METHODS
    public Point runDetection() {
        // test image setting
        if (width == 0) {
            System.err.println("Image not set! Can not run detection");
        }
        
        // initialize union find
        int x_center = (int)Double.POSITIVE_INFINITY;
        int y_center = (int)Double.POSITIVE_INFINITY;
        
        int data[] = ((DataBufferInt) (im.getRaster().getDataBuffer())).getData();
        int[] id_parent = new int[height * width];
        HashMap id_stats = new HashMap();
        
        
        //init id_parent -> id parent = self
        for (int i = 0; i < height * width; i++) {
            id_parent[i] = i;
        }
        
        // FOR LOOP TRAVERSAL
        //		setup id_parent table
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // retrieve id
                int id = y*width + x;
                // retrieve RGB & average
                int rgb = data[id];
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                
                // compare rgb to thresh
                if (r == 0 && g == 0 && b == 255) {
                    // IDENTIFY ID:PARENT RELATIONSHIP
                    // Test all directions (right, down) and set a flag
                    int rightID = y*width + x + 1;
                    int downID = (y + 1)*width + x;
                    boolean rightPixel = false;
                    boolean downPixel = false;
                    // watch for bounds of image
                    if (rightID < (y*width + width)) {
                        int rR = (data[rightID] >> 16) & 0xff;
                        int rG = (data[rightID] >> 8) & 0xff;
                        int rB = (data[rightID]) & 0xff;
                        if (rR == 0 && rG == 0 && rB == 255) {
                            rightPixel = true;
                        }
                    }
                    if (downID < (height * width)) {
                        int dR = (data[downID] >> 16) & 0xff;
                        int dG = (data[downID] >> 8) & 0xff;
                        int dB = (data[downID]) & 0xff;
                        if (dR == 0 && dG == 0 && dB == 255) {
                            downPixel = true;
                        }
                    }
                    
                    // test threshold for right and down directions
                    if (rightPixel) {
                        // check parent of right node
                        // if parent id = id, then update parent to cur node
                        if (id_parent[rightID] == rightID) {
                            id_parent[rightID] = id;
                        }
                        // if parent id is the same as curID, do nothing b/c already in tree
                        // if parent id is different, find roots from both trees
                        //		then link the roots together
                        else if (id_parent[rightID] != id) {
                            // FIND NEW NODE ROOT
                            int curID = rightID;
                            int curParent = id_parent[curID];
                            int newNodeRoot = -1;
                            boolean rootFlag = true;
                            while (rootFlag) {
                                // if curID = curParent, we found last in old tree
                                // retrieve root
                                if (curID == curParent) {
                                    newNodeRoot = curID;
                                    rootFlag = false;
                                }
                                else {
                                    curID = id_parent[curID];
                                    curParent = id_parent[curID];
                                }
                            }
                            
                            // FIND CUR NODE ROOT
                            curID = id;
                            curParent = id_parent[id];
                            rootFlag = true;
                            while (rootFlag) {
                                // if curID = curParent, we found last in old tree
                                // assign newNodeRoot to parent of this root
                                if (curID == curParent) {
                                    id_parent[curID] = newNodeRoot;
                                    rootFlag = false;
                                }
                                else {
                                    curID = id_parent[curID];
                                    curParent = id_parent[curID];
                                }
                            }
                            
                        }
                    } // end right_pixel comparison
                    if (downPixel) {
                        // check parent of down node
                        // if parent id = id, then update parent to cur node's root
                        if (id_parent[downID] == downID) {
                            id_parent[downID] = id;
                        }
                        // if parent id is the same as curID, do nothing b/c already in tree
                        // if parent id is different, trace curID back and convert parent to
                        //		downID root
                        else if (id_parent[downID] != id) {
                            // FIND NEW NODE ROOT
                            int curID = downID;
                            int curParent = id_parent[curID];
                            int newNodeRoot = -1;
                            boolean rootFlag = true;
                            while (rootFlag) {
                                // if curID = curParent, we found last in old tree
                                // retrieve root
                                if (curID == curParent) {
                                    newNodeRoot = curID;
                                    rootFlag = false;
                                }
                                else {
                                    curID = id_parent[curID];
                                    curParent = id_parent[curID];
                                }
                            }
                            
                            // FIND CUR NODE ROOT
                            curID = id;
                            curParent = id_parent[id];
                            rootFlag = true;
                            while (rootFlag) {
                                // if curID = curParent, we found last in old tree
                                // assign newNodeRoot to parent of this root
                                if (curID == curParent) {
                                    id_parent[curID] = newNodeRoot;
                                    rootFlag = false;
                                }
                                else {
                                    curID = id_parent[curID];
                                    curParent = id_parent[curID];
                                }
                            }
                            
                        }
                        
                    } // end down_pixel comparison
                    
                } // end cur_pixel comparison
            }
        } // end traversal
        
        
        // FOR LOOP HASH MAP
        //		setup id_stats hash map
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // retrieve id
                int id = y*width + x;
                int rgb = data[id];
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                
                // compare rgb to threshold
                if (r == 0 && g == 0 && b == 255) {
                    // RETRIEVE ROOT ID
                    int rootID = -1;
                    int curID = id;
                    boolean rootFlag = true;
                    while (rootFlag) {
                        // if curID = parent, we have reached the root
                        if (curID == id_parent[curID]) {
                            rootID = curID;
                            rootFlag = false;
                        }
                        else {
                            curID = id_parent[curID];
                        }
                    }
                    
                    
                    // retrieve stats at this id location
                    // 		if stats is null, create a new instance in table
                    //		else, add this id information to the stats at this location
                    Stats current_stats = (Stats)id_stats.get(rootID);
                    if (current_stats == null) {
						Stats insert = new Stats(x, y, 1);
                        id_stats.put(rootID, insert);
                    }
                    else {
                        int sum_x = x + current_stats.getX();
                        int sum_y = y + current_stats.getY();
                        int n = 1 + current_stats.getN();
						Stats insert = new Stats(sum_x, sum_y, n);
                        id_stats.put(rootID, insert);
                    }
                }
            }
        } // end hash map setup
        
        // FOR LOOP DEVIATION COMPUTATION
        //		computes deviations, finds center
        // initializations
        //		best a keeps track of a closest to 0
        //		best difference keeps track of best difference between sigma_x2 and sigma_y2
        //			should be closest to 0
        double best_a = Double.POSITIVE_INFINITY;
        double best_difference = Double.POSITIVE_INFINITY;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // retrieve id
                int id = y*width + x;
                // RETRIEVE ROOT ID
                int rootID = -1;
                int curID = id;
                boolean rootFlag = true;
                while (rootFlag) {
                    // if curID = parent, we have reached the root
                    if (curID == id_parent[curID]) {
                        rootID = curID;
                        rootFlag = false;
                    }
                    else {
                        curID = id_parent[curID];
                    }
                }
                
                Stats current_stats = (Stats)id_stats.get(rootID);
                if (current_stats != null && current_stats.getN() != 0) {
                    // retrieve stats object
                    // calculate distribution values
                    double sigma_x = x - (current_stats.getX() / current_stats.getN());
                    double sigma_y = y - (current_stats.getY() / current_stats.getN());
                    double sigma_x2 = pow(sigma_x, 2);
                    double sigma_y2 = pow(sigma_y, 2);
                    double a = sigma_x * sigma_y;
                    double difference = abs(sigma_x2 - sigma_y2);
                    
                    
                    // find center
                    if ((a <= best_a) && (difference <= best_difference)) {
                        best_a = a;
                        best_difference = difference;
                        x_center = (current_stats.getX() / current_stats.getN());
                        y_center = (current_stats.getY() / current_stats.getN());
						//System.out.println("Center: " + x_center + " " + y_center);
                   }
                    
                }
            }
        } // end variance computation
        
        // return point
        Point result = new Point(x_center, y_center);
        return result;
    }
    
    
    public void setImage(BufferedImage im) {
        this.im = im;
        this.width = im.getWidth();
        this.height = im.getHeight();
    }
 */   
    
}
