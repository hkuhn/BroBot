package main;

import april.jcam.ImageConvert;
import april.jcam.ImageSource;
import april.jcam.ImageSourceFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class BroBotApplicationController implements BroBotControllerDelegate {

    // const
    // args
    protected BroBotFrame         frame;
    protected StereoVisionFrame   visionFrame;
    protected Thread              mainThread;
    protected BroBotController    botController;
    protected Thread              visionThread;

    protected final BroBotAppControllerDelegate delegate;

    
    // CONSTRUCTOR METHOD
    public BroBotApplicationController(BroBotFrame frame, StereoVisionFrame visionFrame, BroBotAppControllerDelegate delegate) {

        this.visionFrame = visionFrame;
        visionFrame.setSize(1024, 768);
        visionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        visionFrame.setVisible(true);

        this.frame = frame;
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        this.delegate = delegate;
        

        this.frame.disableAllButtons();
        frame.getChooseCameraSourceButton().setEnabled(true);

        frame.getChooseCameraSourceButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startComputerVision();
            }
        });



        // action event listeners
        // START GAME BUTTON LISTENER
        frame.getStartGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // initialize game
                System.out.println("Start Game Button Pressed");
                startGame();
            }
        });
        
        // END GAME BUTTON LISTENER
        frame.getEndGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // end game
                System.out.println("End Game Button Pressed");
                endGame();
            }
        });
    }


    protected ImageSource getImageSource(String description) {

        ArrayList<String> urls = ImageSource.getCameraURLs();
        Object [] urlOptions = urls.toArray();

        if ( urlOptions.length == 0 ) {
            System.out.println("NO URL OPTIONS");
            return null;
        }


        int optionIdx = JOptionPane.showOptionDialog(
                this.getFrame(),
                "Select Image Source",
                "Select " + description + " Image Source",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                urlOptions,
                urlOptions[0]);

        if ( JOptionPane.CLOSED_OPTION == optionIdx ) {
            return null;
        }

        String option = (String)urlOptions[optionIdx];

        ImageSource is = null;
        try {
            is = ImageSource.make(option);
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }

        return is;
    }

    protected void startComputerVision() {

        if ( this.visionThread != null ) return;

        this.getFrame().disableAllButtons();

        final ImageSource leftImageSource = getImageSource("left");
        if ( leftImageSource == null ) {
            this.getFrame().getChooseCameraSourceButton().setEnabled(true);
            return;
        }

        final ImageSource rightImageSource = getImageSource("right");;
        if ( leftImageSource == null ) {
            this.getFrame().getChooseCameraSourceButton().setEnabled(true);
            return;
        }


        this.visionThread = new Thread(new Runnable() {

            @Override
            public void run() {
                leftImageSource.start();
                rightImageSource.start();

                ImageSourceFormat leftFormat = leftImageSource.getCurrentFormat();
                ImageSourceFormat rightFormat = rightImageSource.getCurrentFormat();

                System.out.println("About to run computer vision thread");

                while (true) {
                    byte [] leftBuffer = leftImageSource.getFrame().data;
                    byte [] rightBuffer = rightImageSource.getFrame().data;

                    BufferedImage leftImageOriginal = ImageConvert.convertToImage(leftFormat.format, leftFormat.width, leftFormat.height, leftBuffer);
                    BufferedImage rightImageOriginal = ImageConvert.convertToImage(rightFormat.format, rightFormat.width, rightFormat.height, rightBuffer);

                    BufferedImage[] images = delegate.processImages(leftImageOriginal, rightImageOriginal);

                    final BufferedImage leftImage = images[0];
                    final BufferedImage rightImage = images[1];

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setLeftImage(leftImage);
                            setRightImage(rightImage);
                        }
                    });
                }
            }
        });

        this.visionThread.start();
    }

    protected void setLeftImage(BufferedImage image) {
        this.visionFrame.getLeftImageView().setImage(image);
    }

    protected void setRightImage(BufferedImage image) {
        this.visionFrame.getRightImageView().setImage(image);
    }

    public BroBotFrame getFrame() {
        return frame;
    }

    // PROTECTED METHODS
    protected void startGame() {
        if (this.mainThread != null) {
            System.out.println("Game Already Running!");
            return;
        }
        this.botController = new BroBotController(this);
        this.mainThread = new Thread(this.botController);
        this.mainThread.start();
    }
    
    protected void endGame() {
        if (this.mainThread == null) {
            System.out.println("Game has already stopped");
            return;
        }
        
    }
}