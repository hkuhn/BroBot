package cameracalib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.SwingUtilities;

import april.jcam.ImageConvert;
import april.jcam.ImageSource;
import april.jcam.ImageSourceFormat;

public class ImageProcessor implements Runnable {

	final protected ImageProcessorView view;
	final protected ImageSource imageSource;
	private volatile Rectangle2D cropArea;
	
	public ImageProcessor(
			ImageProcessorView view, 
			ImageSource imageSource) {
		this.view = view;
		this.imageSource = imageSource;
	}

	public synchronized Rectangle2D getCropArea() {
		return cropArea;
	}

	public synchronized void setCropArea(Rectangle2D cropArea) {
		this.cropArea = cropArea;
	}

	static protected void drawRectangles(BufferedImage image, List<Rectangle2D> rectangles)
	{
		Graphics g = image.getGraphics();
		g.setColor(Color.red);
		for ( Rectangle2D rectangle : rectangles ) {
			g.drawRect(
					(int)rectangle.getX(), 
					(int)rectangle.getY(),
					(int)rectangle.getWidth(),
					(int)rectangle.getHeight()
				);
		}
	}
	
	@Override
	public void run() {
		ImageSourceFormat fmt = this.imageSource.getCurrentFormat();
		while ( true ) {
			
			// get buffer with image data from next frame
			byte buf[] = this.imageSource.getFrame().data;
			
			// if next frame is not ready, buffer will be null
			// continue and keep trying
            if (buf == null) {
                System.err.println("Buffer is null");
            	continue;
            }

            // created buffered image from image data
            BufferedImage image = ImageConvert.convertToImage(
            		fmt.format, 
            		fmt.width, 
            		fmt.height, 
            		buf
            );
            
            Rectangle2D cropArea = this.getCropArea();
            if ( cropArea != null ) {
            	image = image.getSubimage(
            			(int)cropArea.getX(),
            			(int)cropArea.getY(),
            			(int)cropArea.getWidth(),
            			(int)cropArea.getHeight()
            	);
            }
            
            // set image on main window
            final BufferedImage processedImage = image;
            SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ImageProcessor.this.view.setVideoFrame(processedImage);
				}
			});
		}
	}

}
