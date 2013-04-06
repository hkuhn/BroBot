package cameracalib;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.*;

import april.util.ParameterGUI;
import april.util.JImage;

/**
 * @author slessans
 *
 */
public class MainWindow extends JFrame implements ImageProcessorView {
	
	private static final long serialVersionUID = 1L;
	private JImage centerImage;
	private JButton chooseCameraSourceButton;
	private BufferedImage selectedImage;
	private ParameterGUI parameterGUI;

	public MainWindow() {
		super("Template Image Matcher");
		this.setLayout(new BorderLayout());
		this.selectedImage = null;
		
		// add center image
		centerImage = new JImage();
		this.add(centerImage, BorderLayout.CENTER);
		
		// bottom panel holds some buttons
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		// add camera source button
		chooseCameraSourceButton = new JButton("Camera Source");
		bottomPanel.add(chooseCameraSourceButton);
	}
	
	
	public double getThresholdValue() {
		return parameterGUI.gd("thresh");
	}
	
	public void setSelectedImage(BufferedImage image) {
		this.selectedImage = image;
		if ( this.selectedImage != null ) {
			this.getCenterImage().setImage(this.selectedImage);
		}
	}
	
	public BufferedImage getSelectedImage() {
		return this.selectedImage;
	}
	
	public void setVideoFrame(BufferedImage image) {
		if ( this.selectedImage == null ) {
			this.getCenterImage().setImage(image);
		}
	}

	public JButton getChooseCameraSourceButton() {
		return chooseCameraSourceButton;
	}

	public JImage getCenterImage() {
		return centerImage;
	}

}
