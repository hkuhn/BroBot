package main;


public class BroBotController {

    // const
    // args
    
    
    
    // CONSTRUCTOR METHOD
    public BroBotController(BroBotFrame frame) {
        
        this.frame = frame;
        frame.sizeSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // action event listeners
        // START GAME BUTTON LISTENER
        frame.getStartGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // initialize game
            }
        });
        
        // END GAME BUTTON LISTENER
        frame.getEndGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // end game
            }
        });
        
    }
    
    




}