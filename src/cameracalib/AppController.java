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
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import april.jcam.ImageSource;
import april.util.JImage;

public class AppController {
	
	private MainWindow frame;
	private BufferedImage leftImage;
	private BufferedImage rightImage;
	private List<Point2D> leftImagePoints;
	private List<Point2D> rightImagePoints;

	public AppController(MainWindow frame) {
		this.frame = frame;
		frame.setSize(1024, 768);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
    	frame.getSelectLeftImagePointsButton().setEnabled(false);
		frame.getSelectRightImagePointsButton().setEnabled(false);
    	
    	frame.getSelectLeftImageButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.this.selectImage(true);
			}
		});
    	
    	frame.getSelectRightImageButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.this.selectImage(false);
			}
		});
    	
    	frame.getSelectLeftImagePointsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.this.selectPoints(AppController.this.getLeftImage());
			}
		});
    	
    	frame.getSelectRightImagePointsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.this.selectPoints(AppController.this.getRightImage());
			}
		});
    	
	}
	
	protected void selectImage(boolean left) {
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(false);
		fc.addChoosableFileFilter(new FileFilter() {
			public final static String jpeg = "jpeg";
		    public final static String jpg = "jpg";
		    public final static String gif = "gif";
		    public final static String tiff = "tiff";
		    public final static String tif = "tif";
		    public final static String png = "png";

		    /*
		     * Get the extension of a file.
		     */  
		    public String getExtension(File f) {
		        String ext = null;
		        String s = f.getName();
		        int i = s.lastIndexOf('.');

		        if (i > 0 &&  i < s.length() - 1) {
		            ext = s.substring(i+1).toLowerCase();
		        }
		        return ext;
		    }
			
			@Override
			public String getDescription() {
				return "Just Images";
			}
			
			@Override
		    public boolean accept(File f) {
		        if (f.isDirectory()) {
		            return true;
		        }

		        String extension = getExtension(f);
		        if (extension != null) {
		            if (extension.equals(tiff) ||
		                extension.equals(tif) ||
		                extension.equals(gif) ||
		                extension.equals(jpeg) ||
		                extension.equals(jpg) ||
		                extension.equals(png)) {
		                    return true;
		            } else {
		                return false;
		            }
		        }

		        return false;
		    }
		});
		
		final String side = left ? "Left" : "Right";
		int returnVal = fc.showDialog(this.getFrame(), "Select " + side + " Image");
		File file = fc.getSelectedFile();
		if ( returnVal == JFileChooser.APPROVE_OPTION && file != null ) {
			try {
				BufferedImage image = ImageIO.read(file);
				if ( left ) {
					this.setLeftImage(image);
				} else {
					this.setRightImage(image);
				}
				
				if ( this.leftImage != null && this.rightImage != null ) {
					this.getFrame().getSelectLeftImagePointsButton().setEnabled(true);
					this.getFrame().getSelectRightImagePointsButton().setEnabled(true);
				} else {
					this.getFrame().getSelectLeftImagePointsButton().setEnabled(false);
					this.getFrame().getSelectRightImagePointsButton().setEnabled(false);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						this.getFrame(), 
						e.getLocalizedMessage(), 
						"Uh, Oh! Seems an error ocurred", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected static void imageToFile(BufferedImage image) {
		try {
		    // retrieve image
		    File outputfile = new File("saved.png");
		    ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	protected void selectPoints(BufferedImage image) {
		if ( image != this.leftImage && image != this.rightImage ) return;
		final boolean left = (image == this.leftImage);
		
		this.getFrame().getSelectLeftImagePointsButton().setEnabled(false);
		this.getFrame().getSelectRightImagePointsButton().setEnabled(false);
		
		final JImage imageView = left ? this.getFrame().getLeftImageView() : this.getFrame().getRightImageView();
		imageView.addMouseListener(new MouseAdapter() {
			
			private int numClicks = 0;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				numClicks++;
				System.out.println("Point number " + numClicks + ": ");
				
				final Point2D clickPointInComponent = new Point2D.Double(e.getX(), e.getY());
				System.out.println("\tPosition in component: " + clickPointInComponent);
				
				AffineTransform t = null;
				try {
					t = imageView.getAffine().createInverse();
				} catch (NoninvertibleTransformException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if ( t != null ) {
					final Point2D clickPositionInImage = t.transform(clickPointInComponent, null);
					System.out.println("\tPosition in image: " + clickPositionInImage);
				} else {
					System.out.println("Somehow could not create inverse!");
					numClicks--;
				}
				
				
				if ( numClicks == 8 ) {
					AppController.this.getFrame().getSelectLeftImagePointsButton().setEnabled(true);
					AppController.this.getFrame().getSelectRightImagePointsButton().setEnabled(true);
					imageView.removeMouseListener(this);
				}
			}
		});
		
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
