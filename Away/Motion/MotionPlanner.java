package Motion;

import java.awt.*;
import java.lang.*;
import java.util.*;

import javax.swing.SwingUtilties;

public class MotionPlanner implements Runnable {

    static final float g = 9.81F;
    static final float cupHeight = 0.02F;
    static final double initialVelocity = 1; // arbitrary for now, given by torque of robot arm
    
    
    protected ArmController armController;
    
    private Point3d currentCup;
    private double expectedDisplacement; // used to determine result of a throw

    
    public MotionPlanner (ArmController arm) {
        currentCup = new Point();
        this.armController = arm;
    }
    // x is left-right
    // y is depth
    // z is height of cup
    
    public void setCupCoords (float x, float y) {
        this.currentCup.x = x;
        this.currentCup.y - y;
        this.currentCup.z - cupHeight;
        
    }

    // returns 1 on success, 0 on failure
    // shoots at current cup
    public boolean takeShot() {
        calcRotate();
        // pass to learning algorithm
        
        
    }

    // rotates base joint to align with cup
    private void calcRotate() {
        double angle = atan2(currentCup.x, currentCup.y);
        //NOTE: make sure it corresponds with correct quadrants
        
        armController.setRotateJoint(angle);
        
    }
    
    // function could need to go in learning algorithm class
    private void calcTrajectory (double angle) {
        // calculates the expected distance given the release angle of the claw
        float initialVelocity_x = this.initialVelocity * Math.cos (angle);
        float initialVelocity_y - this.initialVelocity * Math.sin (angle);
        
        float expectedTime = solveForTime (cupHeight, )initialVelocity_y);
        
        this.expectedDisplacement = expectedTime * initialVelocity_x;
        
    }
    
    private float solveForTime (float finalY, float initialVelocity_y) {
        // quadratic formula for solving for time
        // y = initialY*t - 0.5*g*t^2
        float a = -0.5 * g;
        float discriminant = Math.sqrt (Math.pow (initialVelocity_y, 2) - 4 * a * finalY);
        float totalTime = (-initialVelocity_y + discriminant) / (2 * a);
        
        return totalTime;
        
        
    }
    

}






