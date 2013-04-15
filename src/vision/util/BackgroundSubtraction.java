package src.vision.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.Math;

public class BackgroundSubtraction {

	// args
	private static BufferedImage ref;
	private static int[] ref_stream;
	private static int width;
	private static int height;

	private static final int mid_thresh = 20;
	private static final int high_thresh = 100;

	// CONSTRUCTOR METHOD
	public BackgroundSubtraction() {

	}


	// PUBLIC METHODS
	public BufferedImage runSubtraction(BufferedImage im) {
		int[] data_stream = (int[]) im.getData().getDataElements(0, 0, width, height, null);
		int[] out_stream = new int[data_stream.length];

		// TEST BAYER PATTERN CAMERA TYPE
		//if (data_stream.length > width * height) {
			//System.out.println("Bayer Pattern");
			for (int i = 0; i < data_stream.length - 3; i = i + 3) {
				int pR = data_stream[i] - ref_stream[i];
				int pG = data_stream[i+1] - ref_stream[i+1];
				int pB = data_stream[i+2] - ref_stream[i+2];

				out_stream[i] = pR;
				out_stream[i+1] = pG;
				out_stream[i+2] = pB;
			}
		//}
		//else {
			//System.out.println("RGB Pattern");

			/*
        	for (int i = 0; i < data_stream.length; i++) {
				int p = data_stream[i];
				int r = ref_stream[i];
				int pR = (p >> 16) & 0xff;
				int pG = (p >> 8) & 0xff;
				int pB = (p) & 0xff;
				int rR = (r >> 16) & 0xff;
				int rG = (r >> 8) & 0XFF;
				int rB = (r) & 0xff;

				
				int dR = pR - rR;
				int dG = pG - rG;
				int dB = pB - rB;
	
				int out = (dB) & (dG >> 8) & (dR >> 16);
				
				out_stream[i] = out;
			}
		}
		*/

		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		out.getRaster().setDataElements(0, 0, width, height, out_stream);
		return out;
	}

/*
	public BufferedImage runSubtraction(BufferedImage im) {
		int[] data_stream = (int[]) im.getData().getDataElements(0, 0, width, height, null);
		byte[] out_stream = new byte[data_stream.length];
		for (int i = 0; i < data_stream.length - 3; i = i + 3) {
			int pR = (data_stream[i] - ref_stream[i]);
			int pG = (data_stream[i+1] - ref_stream[i+1]);
			int pB = (data_stream[i+2] - ref_stream[i+2]);
			if (pR < 0) pR = 0;
			if ( pG < 0 ) pG = 0;
			if ( pB < 0 ) pB = 0;
			out_stream[i] = (byte)pR;
			out_stream[i+1] = (byte)pG;
			out_stream[i+2] = (byte)pB;
		}

		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		out.getRaster().setDataElements(0, 0, width, height, out_stream);
		return out;
	}
*/
	public void setRef(BufferedImage ref) {
		this.width = ref.getWidth();
		this.height = ref.getHeight();
		this.ref = ref;
		this.ref_stream = (int[]) ref.getData().getDataElements(0, 0, width, height, null);
	}

	public BufferedImage getRef() {
		return ref;
	}




}
