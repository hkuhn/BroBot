package Motion;

public class RobotMeasurements {

    final public static double L1, L2, L3, L4, L5, L6;
	final public static double D1, D2, D3, D4, D5, D6;
	final public static double W1, W2, W3, W4, W5, W6;

	static {
		/*measurements in meters
		 *	L1 = base to first rotating joint
		 *	L2 = first rotating joint to first bending joint
		 *	L3 = first bending joint to second bending joint
		 *	L4 = second bending joint to wrist
		 *	L5 = wrist to rotating claw joint
		 *	L6 = rotating claw joint to finger
		 *
		 */
		L1 = .075;
		L2 = .044;
		L3 = .108;
		L4 = .100;
		L5 = .070;
		L6 = .098;
		D1 = .045;
		W1 = .040;
		D2 = .033;
		W2 = .034;
		D3 = .023;
		W3 = .043;
		D4 = .023;
		W4 = .045;
		D5 = .032;
		W5 = .062;
		D6 = .050;
		W6 = .050;
	}
}
