package Vision.util;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class binarize {

	// const
	private static final float MIN_BLUE_HUE = 0.5f;
	private static final float MAX_BLUE_HUE = 0.83333f;

	// args
	BufferedImage binarized;
	BufferedImage color;
	double thresh;

	int r, newPixel;

	public binarize(BufferedImage col, double thresh) {
		this.color = col;
		this.thresh = thresh;
		binarized = col;
		binarizeImage();
	}

	protected void binarizeImage() {
        int[] data = (int[]) color.getData().getDataElements(0, 0, width, height, null);
		int[] out_stream = new int[data_stream.length];
        for (int i = 0; i < data_stream.length - 3; i = i + 3) {
			int pR = data_stream[i] - ref_stream[i];
			int pG = data_stream[i+1] - ref_stream[i+1];
			int pB = data_stream[i+2] - ref_stream[i+2];
            float[] hsv = Color.RGBtoHSB(pR, pG, pB, null);
            if (hsv[0] >= MIN_BLUE_HUE && hsv[0] <= MAX_BLUE_HUE) {
                out_stream[i] = 255;    // white
                out_stream[i+1] = 255;  // white
                out_stream[i+2] = 255;  // white
            }
		}
        this.binarized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		binarized.getRaster().setDataElements(0, 0, width, height, out_stream);
	}

	// PUBLIC METHOD
	public BufferedImage getBinarizedImage() {
		return binarized;
	}

}
