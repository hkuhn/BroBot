package cameracalib;

import java.awt.image.BufferedImage;

public interface ImageProcessorView {
	
	// processor calls this with each frame to display
	// this will be called on the swing thread
	public void setVideoFrame(BufferedImage image);

}
