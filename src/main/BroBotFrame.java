package main;

import javax.swing.*;
import java.awt.*;

public class BroBotFrame extends JFrame {

    // args
    private JButton     startGameButton;
    private JButton     endGameButton;
    private JButton     chooseCameraSourceButton;
    private JButton     displayOffButton;
    private JButton     displayBallDetectionButton;
    private JButton     displayRingDetectionButton;


    private JButton [] allButtons;
    
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
        chooseCameraSourceButton = new JButton("Select Camera Sources");

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        this.add(southPanel, BorderLayout.SOUTH);
        southPanel.add(displayBallDetectionButton);
        southPanel.add(displayRingDetectionButton);
        southPanel.add(displayOffButton);
        southPanel.add(chooseCameraSourceButton);


        this.allButtons = new JButton[]{
                startGameButton,
                endGameButton,
                displayOffButton,
                displayBallDetectionButton,
                displayRingDetectionButton,
                chooseCameraSourceButton};
    }

    public JButton getChooseCameraSourceButton() {
        return chooseCameraSourceButton;
    }

    public void disableAllButtons() {
        for (JButton button : this.allButtons) {
            button.setEnabled(false);
        }
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

}