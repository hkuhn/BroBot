package src.vision.balldetection;

import src.datatypes.*;

import java.awt.image.*;
import java.awt.Point;
import java.util.ArrayList;
import static java.lang.Math.*;




public class BallDetectionDetector_Expand {

    // args
    // image data
    private int width;
    private int height;
    private BufferedImage im;
    private int[] in_stream;  // RGB stream
    private int[][] binarizedImageMatrix;
    
    // stats data
    private static final int THRESH_COUNT = 500; // min number of pixels in group
    private static final int JUMP = 10;         // skip length during traversal
    private static final double SCORE_WEIGHT = 0.8;
    ArrayList<Stats> groups = new ArrayList<Stats>();
    private int group = 1;
    
    // return values
    private int x_center;
    private int y_center;
    
    
    // CONSTRUCTOR METHOD
    public BallDetectionDetector_Expand() {
        // null
    }
    
    
    // FLOOD FILL METHOD
    private void floodFill() {
        
        int[][] groupMatrix = new int[height][width];
        
        for (int y = 0; y < height; y+=JUMP) {
            for (int x = 0; x < width; x+=JUMP) {
                int cur_value = binarizedImageMatrix[y][x];
                // blue pixel test
                if (cur_value == 1) {
                    Stats temp = new Stats();
                    groups.add(temp);
                    expand(groupMatrix, x, y);
                    group++;
                }
            }
        }
        
		System.out.println(groups.size());

        double best_a = Double.POSITIVE_INFINITY;
        double best_difference = Double.POSITIVE_INFINITY;
        double best_score = Double.POSITIVE_INFINITY;
        Stats best_stats = new Stats();
        
        for (Stats s: groups) {
            if (s.getN() > THRESH_COUNT) {
                // retrieve stats object
                // calculate distribution values
                ArrayList<Point> coords = s.getCoords();
				for (int i = 0; i < coords.size(); i++) {
					Point test = coords.get(i);
                	double sigma_x = test.getX() - (s.getX() / s.getN());
                	double sigma_y = test.getY() - (s.getY() / s.getN());
                	double sigma_x2 = pow(sigma_x, 2);
                	double sigma_y2 = pow(sigma_y, 2);
                	double a = sigma_x * sigma_y;
                	double difference = abs(sigma_x2 - sigma_y2);
                    
                    double score = abs(SCORE_WEIGHT*a) + (1-SCORE_WEIGHT)*difference;
                
                
                	// find center
                	if (score < best_score) {
                    	best_a = a;
                    	best_difference = difference;
                        best_score = score;
                    	x_center = (s.getX() / s.getN());
                    	y_center = (s.getY() / s.getN());
						//im.setRGB(x_center, y_center, 0xffff0000);
					}
                }
            }
        }
    }
    
    
    // EXPAND METHOD
    private void expand(int[][] groupMatrix, int x, int y) {
        if (y < 2 || y > height - 2) return;
        if (x < 2 || x > width - 2) return;
        
        groupMatrix[y][x] = group;
        Stats s = groups.get(group - 1);
        s.addCoord(x,y);
        groups.set(group - 1, s);
        
        if (s.getN() > 1000) return;
        
        // test east pixel
        if (groupMatrix[y][x+1] == 0) {
            if (binarizedImageMatrix[y][x+1] == 1) {
            	expand(groupMatrix, x+1, y);
            }
        }
        
        // test west pixel
        if (groupMatrix[y][x-1] == 0) {
            if (binarizedImageMatrix[y][x-1] == 1) {
				expand(groupMatrix, x-1, y);
            }
        }
        
        // test south pixel
        if (groupMatrix[y+1][x] == 0) {
            if (binarizedImageMatrix[y+1][x] == 1) {
				expand(groupMatrix, x, y+1);
            }
        }
        
        // test north pixel
        if (groupMatrix[y-1][x] == 0) {
            if (binarizedImageMatrix[y-1][x] == 1) {
				expand(groupMatrix, x, y-1);
            }
        }
        
        
    }
    
    
    // SET & RUN METHODS
    public void runDetection() {
        // run binarization on blue pixels in image
        if (in_stream.length > width * height) {
            // RGB channels (Bayer Pattern)
            int index = 0;
        	for (int y = 0; y < height; y++) {
            	for (int x = 0; x < width; x++) {
                	int r = in_stream[index];
                	int g = in_stream[index + 1];
                	int b = in_stream[index + 2];
                	if (r == 0 && g == 0 && b == 255) {
                    	binarizedImageMatrix[y][x] = 1;
                	}
					index = index + 3;
        		}
        	}
        }
        else {
            // one channel
        	for (int y = 0; y < height; y++) {
            	for (int x = 0; x < width; x++) {
                    
                	int rgb = in_stream[x + y*width];
                	int r = (rgb >> 16) & 0xff;
                	int g = (rgb >> 8) & 0xff;
                	int b = (rgb) & 0xff;
                	if (r == 0 && g == 0 && b == 255) {
                    	binarizedImageMatrix[y][x] = 1;
                	}
        		}
        	}
        }
        
        // run expand/flood fill on binarized image
        floodFill();
        
    }
    
    
    
    
    public void setImage(BufferedImage im) {
        this.width = im.getWidth();
        this.height = im.getHeight();
        this.im = im;
        this.in_stream = ((DataBufferInt) im.getRaster().getDataBuffer()).getData();
        this.binarizedImageMatrix = new int[height][width];
        
        this.x_center = 0;
        this.y_center = 0;
        this.group = 1;
        groups.clear();
        
    }
    
    // ACCESS METHODS
    public BufferedImage getImage() {
        return im;
    }
    
    public Point getCenter() {
        Point center = new Point(x_center, y_center);
        return center;
    }

    


}
