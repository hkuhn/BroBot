package src.vision.balldetection;


import javax.swing.*;
import java.awt.*;

import april.util.JImage;



public class BallDetectionFrame extends JFrame {

	// args
    private JButton         chooseCameraSourceButton;
    private JButton         chooseImageButton;
    private JImage          centerImage;
    private JSlider         blueThresholdSlider;
	private JLabel			sliderLabel;
	private JLabel			blueThresholdLabel;



	// CONSTRUCTOR METHOD
	public BallDetectionFrame() {
		super("Ball Detection");
        this.setLayout(new BorderLayout());
        
        // add center image from JCam
        centerImage = new JImage();
        this.add(centerImage, BorderLayout.CENTER);
        
        // add camera source button
        // add image source button
        // add slider
        chooseCameraSourceButton = new JButton("Camera Source");
        chooseImageButton = new JButton("Choose Image");
        blueThresholdSlider = new JSlider(0,255,255);  //range = 0-255
		blueThresholdLabel = new JLabel("255");

		sliderLabel = new JLabel("Blue Threshold: ");
        
        // build GUI
        buildGUI();

	}

    // BUILD GUI
    private void buildGUI() {
        // build panel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        this.add(northPanel, BorderLayout.NORTH);
        northPanel.add(chooseCameraSourceButton);
        northPanel.add(chooseImageButton);
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        this.add(southPanel, BorderLayout.SOUTH);
		southPanel.add(sliderLabel);
        southPanel.add(blueThresholdSlider);
		southPanel.add(blueThresholdLabel);
        
    }

    // PUBLIC CLASS METHODS
    public JButton getChooseCameraSourceButton() {
        return chooseCameraSourceButton;
    }
    
    
    public JImage getCenterImage() {
        return centerImage;
    }
    
    public synchronized JButton getChooseImageButton() {
        return chooseImageButton;
    }
    
    public JSlider getSlider() {
        return blueThresholdSlider;
    }

	public JLabel getThresholdLabel() {
		return blueThresholdLabel;
	}

}
