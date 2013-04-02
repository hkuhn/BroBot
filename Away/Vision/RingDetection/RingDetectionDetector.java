package Away.Vision.RingDetection;

import java.awt.image.*;


public class RingDetectionDetector {


    // args
    private BufferedImage im;
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
                    im.setRGB(x,y, 0xff0000ff);   //fill white
                }
                
            }
        }

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
        
    }
    
    // ACCESS METHODS
    public BufferedImage getBinarizedImage() {
		// return full color image
		return im;
		// return binarized array
    }
    

}
