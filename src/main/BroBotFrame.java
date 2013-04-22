package main;

import javax.swing.*;
import java.awt.*;

public class BroBotFrame extends JFrame {

    // args
    private JButton     startGameButton;
    private JButton     endGameButton;
    
    
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
        
    }
    

    // ACCESS METHODS
    public JButton getStartGameButton() {
        return startGameButton;
    }
    
    public JButton getEndGameButton() {
        return endGameButton;
    }
    
    

}