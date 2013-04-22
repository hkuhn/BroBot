package vision.coordinateprojection;

import javax.swing.*;
import java.awt.*;

import april.util.JImage;

public class CoordinateProjectionFrame extends JFrame {
    
    // args
    private JButton         chooseLeftImageButton;
    private JButton         chooseRightImageButton;
    private JButton         projectButton;
    private JImage          LeftImage;
    private JImage          RightImage;
    private JLabel          leftPointLabel;
    private JLabel          rightPointLabel;
    
    // CONSTRUCTOR METHOD
    public CoordinateProjectionFrame() {
        super("Coordinate Projection");
        this.setLayout(new BorderLayout());
        
        // add center image from JCam
        LeftImage = new JImage();
        RightImage = new JImage();
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 2, 10, 10));
        centerPanel.add(LeftImage);
        centerPanel.add(RightImage);
		this.add(centerPanel, BorderLayout.CENTER);
        
        // add camera source button
		chooseLeftImageButton = new JButton("Choose Left Image");
        chooseRightImageButton = new JButton("Choose Right Image");
        projectButton = new JButton("Project Coordinates");
        leftPointLabel = new JLabel("No Point Selected");
        rightPointLabel = new JLabel("No Point Selected");

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        this.add(northPanel, BorderLayout.NORTH);
        northPanel.add(chooseLeftImageButton);
        northPanel.add(chooseRightImageButton);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        this.add(southPanel, BorderLayout.SOUTH);
        southPanel.add(leftPointLabel);
        southPanel.add(projectButton);
        southPanel.add(rightPointLabel);
    }

    public JLabel getLeftPointLabel() {
        return leftPointLabel;
    }

    public JLabel getRightPointLabel() {
        return rightPointLabel;
    }

    // PUBLIC CLASS METHODS
    public JImage getLeftImage() {
        return LeftImage;
    }
    
    public JImage getRightImage() {
        return RightImage;
    }
    
    public synchronized JButton getChooseLeftImageButton() {
        return chooseLeftImageButton;
    }
    
    public synchronized JButton getChooseRightImageButton() {
        return chooseRightImageButton;
    }
    
    public synchronized JButton getProjectButton() {
        return projectButton;
    }
    
}
