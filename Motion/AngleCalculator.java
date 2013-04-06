package Motion;

import java.lang.*;
import java.util.*;
import java.awt.*;

import lcm.lcm.*;
import lcmtypes.*;

import april.jmat.MathUtil;
import april.util.TimeUtil;
// controls commands to the arm
public class AngleCalculator {

    final static private double angleCompensation = Math.toRadians(7);
    final static private double MAX_ARM_EXTEND = RobotMeasurements.L3 + RobotMeasurements.L4;

    public AngleCalculator() {}


	private static double calcj0 (double ball_x, double ball_y) {
		double next_angle = Math.atan(ball_y/ball_x);
		if (next_angle >= 0 && ball_x >= 0) { // in first quadrant
			next_angle -= Math.toRadians (179);
		}
		else if (next_angle < 0 && ball_y <= 0) { // in fourth quadrant
			next_angle += Math.toRadians (179);
		}

		// System.out.printf ("j0 angle is %f\n", next_angle);
		return next_angle + angleCompensation;
	}

	// calculates the distance between joint 1 and joint 3, then finds destination angle of joint 1
	// r is distance from base to ball
	private static double calcj1Vert (double r, double m, double delta_h) {
		double theta1 = law_of_cosines (RobotMeasurements.L3, m, RobotMeasurements.L4);

		double angle_to_90 = Math.asin(delta_h / m);
		double next_angle = Math.PI/2 - angle_to_90 - theta1;
		// CLOSE BALL
		if (r <= 0.12) {
			next_angle = -(theta1 + angle_to_90 - Math.PI/2);
		}
		// System.out.printf ("j1 angle is %f\n", next_angle);
		return next_angle;
	}

	private static double law_of_cosines (double x, double y, double z) {

		double arccos_arg = (Math.pow (x, 2) + Math.pow (y, 2) - Math.pow(z, 2)) /
				(2 * x * y);

		return Math.acos (arccos_arg);

	}

	private static double calcj1Strt (double r, double m, double delta_h) {

		double theta1 = law_of_cosines (RobotMeasurements.L3 + RobotMeasurements.L4, m, RobotMeasurements.L5 + RobotMeasurements.L6);
		double theta2 = Math.atan (r/delta_h);

		double next_angle = Math.PI - theta1 - theta2;
		// System.out.printf ("j1 angle is %f\n", next_angle);

		return next_angle;
	}

	// calculates the angle of joint 2 using law of cosines
	// assumes claw is hanging vertically

	private static double calcj2 (double r, double m) {

		double theta1 = law_of_cosines (RobotMeasurements.L3, RobotMeasurements.L4, m);
		double next_angle = Math.PI - theta1;

		// System.out.printf ("j2 angle is %f\n", next_angle);

		return next_angle;

	}


	private static double calcj3Vert (double r, double m) {
		//find last angle using law of cosines
		double theta1 = law_of_cosines (RobotMeasurements.L4, m, RobotMeasurements.L3);

		double theta2 = Math.asin (r / m);

		double next_angle = Math.PI - theta1 - theta2;
		//System.out.printf ("j3 angle is %f\n", next_angle);

		return next_angle;

	}

	private static double calcj3Strt (double r, double m) {

		double theta1 = law_of_cosines (RobotMeasurements.L3 + RobotMeasurements.L4, RobotMeasurements.L5 + RobotMeasurements.L6, m);
		double next_angle = Math.PI - theta1;
		// System.out.printf ("j3 angle is %f\n", next_angle);

		return next_angle;

	}


	public double[] calcNextAngles(double dest_x, double dest_y, double dest_z) {

		double delta_h, m;
		// height of where the wrist joint is
		double wrist_height = dest_z + RobotMeasurements.L5 + RobotMeasurements.L6;
		// delta_h is difference between first joint and wrist
		delta_h = Math.abs (wrist_height - RobotMeasurements.L1 - RobotMeasurements.L2);

		double radius = Math.sqrt (Math.pow(dest_x, 2) + Math.pow(dest_y, 2));

		double j0, j1, j2, j3, j4;

		m = Math.sqrt (Math.pow (radius, 2) + Math.pow (delta_h, 2));
		//System.out.printf ("radius %f\n", radius);

		j0 = calcj0 (dest_x, dest_y);

		// max_x is maximum that arm can reach whilewhat are  wrist is pointed down
		double max_x = Math.sqrt (Math.abs (Math.pow (MAX_ARM_EXTEND, 2) - Math.pow (delta_h, 2)));

		//System.out.printf ("max_x %f\n", max_x);
		if (radius <= max_x) {

			//System.out.printf ("delta_h %f, m %f\n", delta_h, m);
			j1 = calcj1Vert (radius, m, delta_h);
			j2 = calcj2 (radius, m);
			j3 = calcj3Vert (radius, m);
			if (radius <= 0.12) {
				j4 = -Math.PI/2;
			}
			else {
				j4 = 0;
			}

		}

		// ball is too far to grab with wrist straight down
		else {
			//System.out.printf ("Straightening out wrist\n");

			delta_h = Math.abs (RobotMeasurements.L1 + RobotMeasurements.L2 - dest_z);
			m = Math.sqrt (Math.pow (radius, 2) + Math.pow (delta_h, 2));

			//System.out.printf ("delta_h %f, m %f\n", delta_h, m);
			j1 = calcj1Strt (radius, m, delta_h);

			// straighten out second angle
			j2 = 0;
			j3 = calcj3Strt (radius, m);

			// align claw to be perpendicular to ground
			j4 = 0;

		}

		j0 = j0 % (2 * Math.PI);

		return new double[] {j0, j1, j2 ,j3, j4, 0};

	}



}
