package Away.Vision.util;

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
	int width;
	int height;

	int r, newPixel;

	public binarize(BufferedImage col, double thresh) {
		this.color = col;
		this.thresh = thresh;
		this.width = col.getWidth();
		this.height = col.getHeight();
		binarized = col;
		binarizeImage();
	}

	protected void binarizeImage() {
        int[] data_stream = (int[]) color.getData().getDataElements(0, 0, width, height, null);
		int[] out_stream = new int[data_stream.length];
		out_stream = data_stream;
        for (int i = 0; i < data_stream.length; i++) {
			int p = data_stream[i];
			int pR = (p >> 16) & 0xff;
			int pG = (p >> 8) & 0xff;
			int pB = (p) & 0xff;
            float[] hsv = Color.RGBtoHSB(pR, pG, pB, null);
            if (hsv[0] >= MIN_BLUE_HUE && hsv[0] <= MAX_BLUE_HUE) {
                out_stream[i] = 0xff0000ff;    // white
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
