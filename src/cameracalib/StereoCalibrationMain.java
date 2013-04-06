package cameracalib;

import javax.swing.SwingUtilities;

public class StereoCalibrationMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	// create app frame
		    	MainWindow frame = new MainWindow();
		    	
		    	// create controller, give it frame. controller
		    	// will automatically show and handle frame.
		    	// nothing else needs to be done here
		    	new AppController(frame);
		    }
		});
	}
}
