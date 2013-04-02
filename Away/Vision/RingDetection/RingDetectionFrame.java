package Vision.RingDetection;


import javax.swing.*;
import java.awt.*;

import april.util.JImage;

public class RingDetectionFrame extends JFrame {
    
    // args
    private JButton         chooseCameraSourceButton;
    private JButton         chooseImageButton;
    private JImage          centerImage;
    
    
    // CONSTRUCTOR METHOD
    public RingDetectionFrame() {
        super("Ring Shape Detection");
        this.setLayout(new BorderLayout());
        
        // add center image from JCam
        centerImage = new JImage();
        this.add(centerImage, BorderLayout.CENTER);
        
        // add camera source button
        // add image source button
        chooseCameraSourceButton = new JButton("Camera Source");
        chooseImageButton = new JButton("Choose Image");
        
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
    
    
}
