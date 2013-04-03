package Away.Vision.RingDetection;

import Away.Vision.util.*;
import Away.DataTypes.*;

import java.awt.image.*;
import java.awt.Point;
import java.util.Random;


public class RingDetectionDetector {

	// const
	// EDGE DETECTION
	private final static float low_thresh = 15.0f;
	private final static float high_thresh = 20.0f;

    // args
    private BufferedImage im;
	private BufferedImage edges;
	private int width;
	private int height;
	private int[][] binarizedImageArray;	// C-ORDERED MATRIX (ROW MAJOR)

	private int t;		// white threshold (avg. of RGB values > t for a match)
    
    
    // RANSAC Parameters
    private static final int MIN_SIZE = 53; // min. number of data points to define a circle
    private static final int q = 10;        // number of total circles to be found
    private static final int n = 3;         // randomly selected n points
    private static final double inf = Double.POSITIVE_INFINITY;
    private static final int ERROR_THRESH = 1;  // base error threshold in pixels for pixel offset
                                                // actual error dependent on radius size
    private static final int DELTA_T = 5;       // search every DELTA_T degrees for a match
    


    
    // CONSTRUCTOR METHOD
    public RingDetectionDetector() {
		this.width = 0;
		this.height = 0;
        
    }


	// PRIVATE METHODS
	private void binarizeImage() {
		// scan image, test threshold
        int[] imageArray = ((DataBufferInt) im.getRaster().getDataBuffer()).getData();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                
                int rgb = imageArray[x + y*width];
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                int avg = (r + g + b) / 3;
                if (avg >= t) {
                    binarizedImageArray[y][x] = 1;
                    //im.setRGB(x,y, 0xff0000ff);   //fill blue
                }
            }
        }

	}

	private void RANSAC_CIRCLES(int[][] edgesMatrix) {
        // RANSAC ALGORITHM
        //  1. pick three random points from data array
        //  2. fit a circle for these three points
        //  3. run through all points in the array and test fit to line
        //      - if there is a high fit, add to hypothesis inliers
        //      - if there is a high count of inliers, add circle to best circle array
        //  4. Repeat until we find the best circle after many iterations and save the best circle
        //      - remove these points from data array and continue
        //  5. Repeat after we find n circles
        
        int trials = 0;
        
        while (trials < q) {
            // init & reset
            Circle best_model = new Circle();
            ArrayList<Point> best_consensusSet = new ArrayList<Point>();
            
            // begin traversal
            Random randomizer = new Random();
            int iterations = 0;
            while (iterations < k) {
                // coords
                int p1x = randomizer.nextInt(width); int p1y = randomizer.nextInt(height);
                int p2x = randomizer.nextInt(width); int p2y = randomizer.nextInt(height);
                int p3x = randomizer.nextInt(width); int p3y = randomizer.nextInt(height);
                // points
                Point p1 = new Point(p1x, p1y);
                Point p2 = new Point(p2x, p2y);
                Point p3 = new Point(p3x, p3y);
                // test for dissimilar points
                while (p1 == p2 || p2 == p3 || p1 == p3) {
                    // coords
                    p1x = randomizer.nextInt(width); int p1y = randomizer.nextInt(height);
                    p2x = randomizer.nextInt(width); int p2y = randomizer.nextInt(height);
                    p3x = randomizer.nextInt(width); int p3y = randomizer.nextInt(height);
                    // points
                    p1 = new Point(p1x, p1y);
                    p2 = new Point(p2x, p2y);
                    p3 = new Point(p3x, p3y);
                }
                // values
                int p1v = edgesMatrix[p1y][p1x];
                int p2v = edgesMatrix[p2y][p2x];
                int p3v = edgesMatrix[p3y][p3x];                
                // retrieve model
                Circle model = fitCircle(p1, p2, p3);
                
                // find consensus points
                ArrayList<Point> consensusSet = new ArrayList<Point>();
                consensusSet.add(p1); consensusSet.add(p2); consensusSet.add(p3);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        // test if pixel is an edge
                        if (edgesMatrix[y][x] != 1) continue;
                        // test if pixel is within range of radius
                        double error = getCirclePointError(model, x, y);
                        
                        /* CONTINUE HERE */
                    }
                }
                
            }

        }
        
		
	}
    
    // RANSAC HELPER METHODS
    private Circle fitCircle(Point p1, Point p2, Point p3) {
        // given three points, fit a circle
        // return the radius and center coordinate point
        // int array result = [Point c, int radius] (floored)
        // Point c Calculations:
        //      ~ Done by equating distance from all three points to xr and yr
        //      ~ Algebraic expression
        //
        // Radius Calculations:
        //      ~ Done by calculating distance from any input point to center point
        // 
        
        // CENTER POINT CALCULATIONS
        Point p1 = new Point(2,1);
        Point p2 = new Point(0,5);
        Point p3 = new Point(-1,2);
        
        double A = p1.getX() - p3.getX();
        double B = p1.getX() - p2.getX();
        double C = A*(p2.getX()*p2.getX() + p2.getY()*p2.getY()) + B*(p1.getX()*p1.getX() + p1.getY()*p1.getY()) - A*(p1.getX()*p1.getX() + p1.getY()*p1.getY()) - B*(p3.getX()*p3.getX() + p3.getY()*p3.getY());
        double D = 2*(A*(p2.getY()-p1.getY()) - B*(p3.getY() - p1.getY()));
        
        int yr = (int)Math.floor(C / D);
        
        int xr = (int)Math.floor(((p1.getX()*p1.getX() + p1.getY()*p1.getY()) - (p2.getX()*p2.getX() + p2.getY()*p2.getY()) + 2*yr*(p2.getY() - p1.getY())) / (2*(p1.getX() - p2.getX())));
        
        Point center = new Point(xr, yr);
        
        // RADIUS CALCULATIONS
        double r = Math.sqrt((p1.getX() - center.getX())*(p1.getX() - center.getX()) + (p1.getY() - center.getY())*(p1.getY() - center.getY()));
        
        // return
        Circle out = new Circle(center, r);
        return out;
    }
    
    private double getCirclePointError(Circle model, int x, int y) {
        // Assess validity of test point x and y on circle model
        
        
        /* WRITE FUNCTION HERE */
        
    }
    
    
    // RUN DETECTION METHOD
    public void runDetection(BufferedImage im, int thresh) {
        // retrieve image properties & initialize
		this.width = im.getWidth();
		this.height = im.getHeight();
		this.t = thresh;
		this.binarizedImageArray = new int[height][width];

		this.im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        this.im = im;
		
		// run thresholding, signal matches on binarized image
		binarizeImage();
        
        // run edge detection on binarized image
		CannyEdgeDetector detector = new CannyEdgeDetector();
		detector.setLowThreshold(low_thresh);
		detector.setHighThreshold(high_thresh);		
		detector.setSourceImage(this.getBinarizedImage());
		detector.process();
		this.edges = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.edges = detector.getEdgesImage();

		// store data points in matrix
		int[][] edgesMatrix = new int[height][width];
		int[] edge_data = ((DataBufferInt) this.edges.getRaster().getDataBuffer()).getData();
		int edge_count = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// edge pixel has value either 1 or 255
				if (((edge_data[x + y*width] >> 16) & 0xff) > 0) {
					edgesMatrix[y][x] = 1;
					edge_count++;
				}
			}
		}
		System.out.println("Edge Count: " + edge_count);

		// run RANSAC Circle Detection on data points
		RANSAC_CIRCLES(edgesMatrix);
        
    }
    
    // ACCESS METHODS
    public BufferedImage getBinarizedImage() {
		// return binarized array
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster raster = (WritableRaster) out.getRaster();
        int[] data = new int[width*height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[x + y*width] = binarizedImageArray[y][x];
            }
        }
        raster.setPixels(0, 0, width, height, data);        
        return out;
        
    }

	public BufferedImage getThinnedImage() {
		return this.edges;

	}

    

}
