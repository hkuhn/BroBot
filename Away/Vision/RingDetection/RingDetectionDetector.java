package Away.Vision.RingDetection;

import java.awt.image.BufferedImage;


public class RingDetectionDetector {


    // args
	private int width;
	private int height;
	private int[][] binarizedImageArray;	// C-ORDERED MATRIX (ROW MAJOR)

	private int t;		// white threshold (avg. of RGB values > t for a match)

    
    // CONSTRUCTOR METHOD
    public RingDetectionDetector() {
		this.width = 0;
		this.height = 0;
        
    }


	// PRIVATE METHODS
	private void binarizeImage() {
		// scan image, test threshold		

	}
    
    
    // RUN DETECTION METHOD
    public void runDetection(BufferedImage im, int thresh) {
        // retrieve image properties & initialize
		this.width = im.getWidth();
		this.height = im.getHeight();
		this.binarizedImageArray = new int[height][width];
		
		// run thresholding, signal matches on binarized image
		binarizeImage();
        
    }
    

}
