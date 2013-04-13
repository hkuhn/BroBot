package cameracalib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import april.jcam.ImageConvert;
import april.jcam.ImageSource;
import april.jcam.ImageSourceFormat;
import april.util.JImage;

public class AppController {
	
	private MainWindow frame;
	private BufferedImage leftImage;
	private BufferedImage rightImage;
    private ImageSource leftImageSource;
    private ImageSource rightImageSource;
    private boolean running;
    private int pictureNumber;

	public AppController(MainWindow frame) {
		this.frame = frame;
		frame.setSize(1024, 768);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);

        running = false;
        pictureNumber = 0;

        frame.getStartButton().setEnabled(false);
        frame.getTakePictureButton().setEnabled(false);
    	
    	frame.getSelectLeftImageSourceButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.this.selectImageSource(true);
			}
		});
    	
    	frame.getSelectRightImageSourceButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.this.selectImageSource(false);
			}
		});

        frame.getTakePictureButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                takePicture();
            }
        });
    	
	}

    protected void takePicture() {

        if ( this.leftImage != null && this.rightImage != null ) {
            imageToFile(this.leftImage, "ImagePair." + pictureNumber + ".Left");
            imageToFile(this.leftImage, "ImagePair." + pictureNumber + ".Right");
            pictureNumber++;
        }  else {
            System.err.println("Null image or images");
        }

    }

    protected void startCameraAction() {
        if ( running || (leftImageSource == null || rightImageSource == null) ) return;
        running = true;

        new Thread(new Runnable() {

            @Override
            public void run() {

                leftImageSource.start();
                rightImageSource.start();

                ImageSourceFormat leftFormat = leftImageSource.getCurrentFormat();
                ImageSourceFormat rightFormat = rightImageSource.getCurrentFormat();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        getFrame().getTakePictureButton().setEnabled(true);
                    }
                });

                while (true) {
                    byte [] leftBuffer = leftImageSource.getFrame().data;
                    byte [] rightBuffer = rightImageSource.getFrame().data;

                    final BufferedImage leftImage = ImageConvert.convertToImage(leftFormat.format, leftFormat.width, leftFormat.height, leftBuffer);
                    final BufferedImage rightImage = ImageConvert.convertToImage(rightFormat.format, rightFormat.width, rightFormat.height, rightBuffer);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setLeftImage(leftImage);
                            setRightImage(rightImage);
                        }
                    });
                }
            }
        }).start();

    }

	
	protected void selectImageSource(boolean left) {

        final String side = left ? "Left" : "Right";
        ArrayList<String> urls = ImageSource.getCameraURLs();
        Object [] urlOptions = urls.toArray();

        if ( urlOptions.length == 0 ) {
            System.out.println("NO URL OPTIONS");
            return;
        }


        int optionIdx = JOptionPane.showOptionDialog(
                this.getFrame(),
                "Select Image Source",
                "Select " + side + " Image Source",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                urlOptions,
                urlOptions[0]);

        if ( JOptionPane.CLOSED_OPTION == optionIdx ) {
            return;
        }

        String option = (String)urlOptions[optionIdx];

        try {
            ImageSource is = ImageSource.make(option);
            if ( left ) {
                leftImageSource = is;
            } else {
                rightImageSource = is;
            }
            if ( leftImageSource != null && rightImageSource != null ) {
                this.getFrame().getStartButton().setEnabled(true);
            }
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
	}
	
	protected static void imageToFile(BufferedImage image, String name) {
		try {
		    // retrieve image
		    File outputFile = new File(name + ".png");
		    ImageIO.write(image, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public MainWindow getFrame() {
		return frame;
	}

	public BufferedImage getLeftImage() {
		return leftImage;
	}

	public void setLeftImage(BufferedImage leftImage) {
		this.leftImage = leftImage;
		this.getFrame().getLeftImageView().setImage(this.leftImage);
	}

	public BufferedImage getRightImage() {
		return rightImage;
	}

	public void setRightImage(BufferedImage rightImage) {
		this.rightImage = rightImage;
		this.getFrame().getRightImageView().setImage(this.rightImage);
	}

}
