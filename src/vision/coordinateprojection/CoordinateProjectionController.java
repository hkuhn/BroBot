package src.vision.coordinateprojection;


import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.awt.geom.*;

import april.jcam.ImageSource;
import april.jcam.ImageConvert;
import april.jcam.ImageSourceFormat;
import april.jcam.ImageSourceFile;
import april.jmat.Matrix;



public class CoordinateProjectionController {
    
    
    // args
    private ImageSource		    	selectedLeftImageSource;
    private ImageSource             selectedRightImageSource;
    private CoordinateProjectionFrame      frame;
    private Thread		    		leftImageThread;
    private Thread                  rightImageThread;
    private BufferedImage 	    	selectedLeftImage;
    private BufferedImage           selectedRightImage;

	protected Point					pixel_left_point;
	protected Point					pixel_right_point;
    
    
    // CONSTRUCTOR
    public CoordinateProjectionController(CoordinateProjectionFrame frame) {
        
        this.frame = frame;
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // add action event listeners
        // LEFT IMAGE BUTTON
        frame.getChooseLeftImageButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                                                             "Images", "jpg", "gif", "png");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(CoordinateProjectionController.this.frame);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	selectedLeftImage = imageFromFile(chooser.getSelectedFile());
			    	selectedLeftImageSource = null;
			    }
                
                if (selectedLeftImage != null) {
                    startLeftImage();
                }
			}
		});
        
        // RIGHT IMAGE BUTTON
        frame.getChooseRightImageButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                                                             "Images", "jpg", "gif", "png");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(CoordinateProjectionController.this.frame);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	selectedRightImage = imageFromFile(chooser.getSelectedFile());
			    	selectedRightImageSource = null;
			    }
                
                if (selectedRightImage != null) {
                    startRightImage();
                }
			}
		});
        
        // COMPUTE PROJECTION BUTTON
        frame.getProjectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				System.out.println("Projecting points into 3D");
                // run computation
            }
        });
        
        // CLICK ACTION ON LEFT IMAGE
        frame.getLeftImage().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				CoordinateProjectionController.this.didClickMouse(me);
			}
		});
                                                   
        // CLICK ACTION ON RIGHT IMAGE
        frame.getRightImage().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				CoordinateProjectionController.this.didClickMouse(me);
			}
		});
        
        
    }
    
    // PROTECTED CLASS METHODS
    protected static BufferedImage imageFromFile(File file) {
		BufferedImage in;
		try {
			in = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error Reading from File");
			return null;
		}
		BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(in, 0, 0, null);
		g.dispose();
		return newImage;
	}
    
    protected void didClickMouse(MouseEvent me) {
        // toggle click action
        System.out.println("Clicked on an image!");
        
	}
    
    // RUN LEFT IMAGE
    protected void startLeftImage() {
		
		if ( this.leftImageThread != null ) {
			System.err.println("Warning, camera already running");
			return;
		}
		
        
		this.leftImageThread = new Thread(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						//BufferedImage out = RingDetectionController.this.processImage(selectedImage);
                        CoordinateProjectionController.this.getFrame().getLeftImage().setImage(selectedLeftImage);
					}
				});
			}
		});
		this.leftImageThread.start();
	}
    
    // RUN LEFT IMAGE
    protected void startRightImage() {
		
		if ( this.rightImageThread != null ) {
			System.err.println("Warning, camera already running");
			return;
		}
		
        
		this.rightImageThread = new Thread(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						//BufferedImage out = RingDetectionController.this.processImage(selectedImage);
                        CoordinateProjectionController.this.getFrame().getRightImage().setImage(selectedRightImage);
					}
				});
			}
		});
		this.rightImageThread.start();
	}
    
    
    // Image Processing
    protected void processImage(BufferedImage im) {
        // do shit to image
    }
    
    
    // PUBLIC CLASS METHODS
    public CoordinateProjectionFrame getFrame() {
        return frame;
    }
    
}
