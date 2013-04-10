package Away.Vision.BallDetection;

import Away.Vision.util.*;

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



public class BallDetectionController {
    
    
    // args
    private ImageSource		    	selectedImageSource;
    private BallDetectionFrame      frame;
    private String		    		selectedCameraURL;
    private Thread		    		imageThread;
    private BufferedImage 	    	selectedImage;

    
    // slider white threshold (dynamic)
    private volatile int blueThreshold;

	// timer vars
	private static final int interval = 30000;	// 5 sec
    
    
    // CONSTRUCTOR
    public BallDetectionController(BallDetectionFrame frame) {
        
        this.blueThreshold = 255;
        
        this.frame = frame;
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        
        // add action event listeners
        frame.getChooseCameraSourceButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BallDetectionController.this.chooseCameraSourceAction();
			}
		});
                
        
        // toggle click action
        frame.getCenterImage().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				BallDetectionController.this.didClickMouse(me);
			}
		});
        
        // toggle jslider action
        frame.getSlider().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                BallDetectionController.this.updateThreshold();
            }
        });
        
        frame.getChooseImageButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                                                             "Images", "jpg", "gif", "png");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(BallDetectionController.this.frame);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	selectedImage = imageFromFile(chooser.getSelectedFile());
			    	selectedImageSource = null;
			    	if ( selectedImage != null ) {
			    		startImage();
			    	}
			    }
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
        
	}
    
    protected void updateThreshold() {
        // update threshold value
        //System.out.println("Threshold Change from Slider: " + this.frame.getSlider().getValue());
        this.blueThreshold = this.frame.getSlider().getValue();
		this.frame.getThresholdLabel().setText(String.valueOf(this.blueThreshold));
        
        startImage();

    }
    
	protected void chooseCameraSourceAction() {
		// retrieve camera URLS
		List<String> URLS = ImageSource.getCameraURLs();
        
		// test for no cameras availablImageSourceFormate
		if (URLS.size() == 0) {
			JOptionPane.showMessageDialog(
                                          this.getFrame(),
                                          "There are no camera urls available",
                                          "Error encountered",
                                          JOptionPane.OK_OPTION
                                          );
			return;
		}
        
		final String initial = (this.selectedCameraURL == null) ?
        URLS.get(0) : this.selectedCameraURL;
        
		final String option = (String)JOptionPane.showInputDialog(
                                                                  this.getFrame(),
                                                                  "Select a camera source from the available URLs below:",
                                                                  "Select Source",
                                                                  JOptionPane.PLAIN_MESSAGE,
                                                                  null,
                                                                  URLS.toArray(),
                                                                  initial
                                                                  );
        
		if ( option != null ) {
			// the selected URL
			this.selectedCameraURL = option;
			this.startCamera();
		}
	}
    
    protected void startImage() {
		
		if ( this.imageThread != null ) {
			//System.err.println("Warning, camera already running");
			//return;
		}
		
        
		this.imageThread = new Thread(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
                        final BackgroundSubtraction backgroundSubtraction = new BackgroundSubtraction();
						BufferedImage out = BallDetectionController.this.processImage(selectedImage, backgroundSubtraction, false);
                        BallDetectionController.this.getFrame().getCenterImage().setImage(out);
					}
				});
			}
		});
		this.imageThread.start();
	}
    
    protected void startCamera() {
		
		if ( this.imageThread != null ) {
			System.err.println("Warning, camera already running");
			return;
		}
        
		try {
			this.selectedImageSource = ImageSource.make(this.selectedCameraURL);
		}
		catch(IOException e) {
			// do nothing
			System.err.println(e);
			this.selectedImageSource = null;
			return;
		}
        
		// BUILD NEW THREAD
		this.selectedImageSource.start();
		this.imageThread = new Thread(new Runnable() {
			@Override
            public void run() {
                ImageSourceFormat fmt = BallDetectionController.this.selectedImageSource.getCurrentFormat();
				long prev_time = 0;

                while (true) {
                    // get buffer with image data from next frame
                    byte buf[] = BallDetectionController.this.selectedImageSource.getFrame().data;
                    
                    // if next frame is not ready, buffer will be null
                    // continue and keep trying
                    if (buf == null) {
                        System.err.println("Buffer is null");
                        continue;
                    }
                    
                    // created buffered image from image data
                    final BufferedImage im = ImageConvert.convertToImage(
                                                                         fmt.format,
                                                                         fmt.width,
                                                                         fmt.height,
                                                                         buf
                                                                         );

					final BackgroundSubtraction backgroundSubtraction = new BackgroundSubtraction();
					// check timer
					long cur_time = System.currentTimeMillis();
					// reset reference frame
					if (cur_time > prev_time + interval) {
						prev_time = cur_time;
						backgroundSubtraction.setRef(im);
					}
                    
                    // set image on main window
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            BufferedImage out = BallDetectionController.this.processImage(im, backgroundSubtraction, true);
                            BallDetectionController.this.getFrame().getCenterImage().setImage(out);
                        }
                    });
                }
            }
        });
        this.imageThread.start();
    }
    
    // Image Processing
    protected BufferedImage processImage(BufferedImage im, BackgroundSubtraction backgroundSubtraction, boolean camera) {
		
        BufferedImage out;
        if (camera) {
            // run background subtraction
            out = backgroundSubtraction.runSubtraction(im);
        }
        else {
            out = im;
        }
        
        // run image binarization with blue thresh
        binarize b = new binarize(out, blueThreshold);
        out = b.getBinarizedImage();
        
		/*
        // run ball detection
        BallDetectionDetector bdd = new BallDetectionDetector();
        int[] bounds = bdd.runDetector(out, blueThreshold);
        
        
         // Display the detection, by drawing on the image
         // draw the horizontal lines
         for (int y = bounds[1]; y <= bounds[3]; y++) {
             for (int x = bounds[0]; x <=bounds[2]; x++) {
                    out.setRGB(x,y, 0xffff00ff);
             }
         }
         
         int center_x = (bounds[2] + bounds[0])/2;
         int center_y = (bounds[3] + bounds[1])/2;
         for (int y = -2; y < 3; y++) {
             for (int x = -2; x < 3; x++) {
                 if (bounds[0] > 0) {
                        im.setRGB(center_x + x, center_y + y, 0xff00ffff);
                 }
             }
         }
        */

        
        return out;
    }
    
    
    // PUBLIC CLASS METHODS
    public BallDetectionFrame getFrame() {
        return frame;
    }
    
}
