package cameracalib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import april.jcam.ImageSource;

public class AppController {
	
	
	private ImageSource selectedImageSource;
	private String selectedCameraUrl;
	private MainWindow frame;
	private Thread imageThread;
	private ImageProcessor imageProcessor;

	public AppController(MainWindow frame) {
		this.frame = frame;
		frame.setSize(1024, 768);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	this.imageThread = null;
    	this.selectedCameraUrl = null;
    	
    	// add some callbacks
    	frame.getChooseCameraSourceButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AppController.this.chooseCameraSourceAction();
			}
		});
    	
    	frame.getCenterImage().addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			System.out.println("Center image clicked");
    		}    	
    	});
    	
	}
	
	protected static void imageToFile(BufferedImage image) {
		try {
		    // retrieve image
		    File outputfile = new File("saved.png");
		    ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	protected void chooseCameraSourceAction() {
		List<String> urls = ImageSource.getCameraURLs();
		
		if ( urls.size() == 0 ) {
			JOptionPane.showMessageDialog(
					this.getFrame(),
					"There are no available camera urls!",
					"Uh, oh!",
					JOptionPane.OK_OPTION
			);
			return;
		}
		
		final String initial = (this.selectedCameraUrl == null) ?
				urls.get(0) : this.selectedCameraUrl;
				
		final String option = (String)JOptionPane.showInputDialog(
				this.getFrame(),
				"Select a camera source from the available URLs below:",
				"Select Source",
				JOptionPane.PLAIN_MESSAGE,
				null,
				urls.toArray(),
				initial
		);
		
		if ( option != null ) {
			// the selected URL
			this.selectedCameraUrl = option;
			this.startCamera();
		}
	}
	
	protected void startCamera() {
		
		if ( this.imageThread != null ) {
			JOptionPane.showMessageDialog(
					this.getFrame(), 
					"Cannot start camera since a thread already exists!\n" +
					"See System.err for details about existing thread.",
					"Uh, oh!",
					JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		
		try {
			this.selectedImageSource = ImageSource.make(this.selectedCameraUrl);
		} catch(IOException e) {
			e.printStackTrace(System.err);
			
			JOptionPane.showMessageDialog(
					this.getFrame(), 
					"The selected camera URL could not be used\n" +
					"to create an image source:\n" + 
					e.getLocalizedMessage() + "\n\n" +
					"Check System.err for more details.",
					"Uh, oh!",
					JOptionPane.ERROR_MESSAGE
			);
			this.selectedImageSource = null;
			this.selectedCameraUrl = null;
			return;
		}
		
		
		this.selectedImageSource.start();
		this.imageProcessor = new ImageProcessor(this.frame, this.selectedImageSource);
		this.imageThread = new Thread(this.imageProcessor);
		this.imageThread.start();
		
		JOptionPane.showMessageDialog(
				this.getFrame(),
				"When you want to select the template image, click on the\n"
				+ "image to pause and further instructions will be given.",
				"Instructions",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	public MainWindow getFrame() {
		return frame;
	}

}
