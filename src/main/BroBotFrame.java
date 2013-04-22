package main;

import javax.swing.*;
import java.awt.*;

public class BroBotFrame extends JFrame {

    // args
    private JButton     startGameButton;
    private JButton     endGameButton;
    private JButton     displayOffButton;
    private JButton     displayBallDetectionButton;
    private JButton     displayRingDetectionButton;
    private JButton     displayStereoVisionButton;
    
    
    
    // CONSTRUCTOR METHOD
    public BroBotFrame() {
        super("Pong Bot");
        this.setLayout(new BorderLayout());
        
        // add start button
        // add end button
        startGameButton = new JButton("Start Game");
        endGameButton = new JButton("End Game");
        
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        this.add(northPanel, BorderLayout.NORTH);
        northPanel.add(startGameButton);
        northPanel.add(endGameButton);
        
        // add view buttons
        displayOffButton = new JButton("Displays Off");
        displayBallDetectionButton = new JButton("Display Ball Detection");
        displayRingDetectionButton = new JButton("Display Ring Detection Button");
        displayStereoVisionButton = new JButton("Display Stereo Vision Button");
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        this.add(southPanel, BorderLayout.SOUTH);
        southPanel.add(displayStereoVisionButton);
        southPanel.add(displayBallDetectionButton);
        southPanel.add(displayRingDetectionButton);
        southPanel.add(displayOffButton);
        
    }
    

    // ACCESS METHODS
    public JButton getStartGameButton() {
        return startGameButton;
    }
    
    public JButton getEndGameButton() {
        return endGameButton;
    }
    
    public JButton getDisplayOffButton() {
        return displayOffButton;
    }
    
    public JButton getDisplayBallDetectionButton() {
        return displayBallDetectionButton;
    }
    
    public JButton getDisplayRingDetectionButton() {
        return displayRingDetectionButton;
    }
    
    public JButton getDisplayStereoVisionButton() {
        return displayStereoVisionButton;
    }
    
    
    
    

}