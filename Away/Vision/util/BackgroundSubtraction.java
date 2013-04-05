package Away.Vision.util;

import java.awt.image.BufferedImage;

public class BackgroundSubtraction {

	// args
	private static BufferedImage ref;
	private static byte[] ref_stream;
	private static int width;
	private static int height;

	// CONSTRUCTOR METHOD
	public BackgroundSubtraction(BufferedImage ref) {
		this.width = ref.getWidth();
		this.height = ref.getHeight();
		this.ref = ref;
		this.ref_stream = (byte[]) ref.getRaster().getDataElements(0, 0, width, height, null);

	}


	// PUBLIC METHODS
	public BufferedImage runSubtraction(BufferedImage im) {
		byte[] data_stream = (byte[]) im.getRaster().getDataElements(0, 0, width, height, null);
		byte[] out_stream = new byte[data_stream.length];
		for (int i = 0; i < data_stream.length; i++) {
			byte p = (byte)(data_stream[i] - ref_stream[i]);
			if (p < 0) p = 0;
			else p =1;
			out_stream[i] = p;
		}

		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		out.getRaster().setDataElements(0, 0, width, height, out_stream);
		return out;
	}




}
