package main;


import motion.ArmController;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import vision.datastructures.Point2Space;
import vision.datastructures.Point3Space;

public class BroBotController implements Runnable {
    
    // const
    private static final double cameraRotationAroundX = Math.toRadians(20);
    private static final double cameraTranslationZ = -0.0635d;
    private static final double cameraTranslationX = -0.0635d;

    private static final RealMatrix cameraToBotTransform;

    static {
         double [][] transformData = {
                 {1, 0, 0, cameraTranslationX},
                 {0, Math.cos(cameraRotationAroundX), -Math.sin(cameraRotationAroundX), 0},
                 {0, Math.sin(cameraRotationAroundX),  Math.cos(cameraRotationAroundX), cameraTranslationZ},
                 {0, 0, 0, 1}
         };
         cameraToBotTransform = new Array2DRowRealMatrix(transformData);
    }
    
    // args
    final protected BroBotControllerDelegate        delegate;
    protected Thread                                ballDetectionThread;
    protected Thread                                ringDetectionThread;

    private ArmController armController;
    
    protected boolean                               isExecuting;
    protected boolean                               endOfGame;

    private Object targetLock = new Object();
    protected Point3Space target;


    // CONSTRUCTOR METHOD
    public BroBotController(BroBotControllerDelegate delegate) {
        
        this.delegate = delegate;
        this.endOfGame = false;
        this.armController = new ArmController();
        this.target = new Point3Space(-0.0058793771250389565, 0.24317711959298435, 1.2732851679275128);
    }
    
    
    
    // RUNNABLE METHOD
    @Override
	public void run() {
		System.out.println("initializing new game in controller...");
		execute();
	}

    
    
    // EXECUTE METHOD
    public void execute() {
        
		synchronized (this) {
			if ( this.isExecuting ) return;
			this.isExecuting = true;
		}
        
        // BEGIN EXECUTION METHOD
        //  1. Bot State (possession)
        //      a. find circles
        //      b. send 9 center correspondences to stereo reconstruction
        //      c. send lowest error correspondence to lookup table
        //      d. send angles to servos and shoot
        //      e. goto intermediate step
        //  2. Intermediate State
        //      a. wait until a ball is detected in frame
        //      b. once ball is detected for at least "t1" seconds, goto opponent state
        //      c. wait until a ball is out of frame
        //      d. once ball leaves frame for at least "t2" seconds, goto bot state
        
        // start threads
        //this.ballDetectionThread = new Thread(this.ballDetectionController);
        //this.ringDetectionThread = new Thread(this.ringDetectionController);
        //this.ballDetectionThread.start();
        //this.ringDetectionThread.start();
        
        
        while (true) {
            
            this.gotoBotState();
            
            if (endOfGame) {
                System.out.println("no more cups in view. Game has ended");
                this.gotoEndState();
                break;
            }
            else {
                this.gotoIntermediateState();
            }
        }
        
        synchronized (this) {
            this.isExecuting = false;
        }
        
    }

    public Point3Space popTarget() {
        Point3Space target = null;
        synchronized (targetLock) {
            target = this.target;
            this.target = null;
        }
        return target;
    }

    public void setTarget(Point3Space target) {
        synchronized (targetLock) {
            this.target = target;
        }
    }

    protected Point3Space transformIntoBotCoordinates(Point3Space originalPoint) {
        RealMatrix point = originalPoint.getHomogeneousMatrixRepresentation();
        RealMatrix result = cameraToBotTransform.multiply(point);
        Point3Space transformed = Point3Space.getPointFromHomogeneousVector(result.getColumnVector(0));
        return transformed;
    }

    // STATE METHODS
    protected void gotoBotState() {
        // (1) acquire target
        // (2) destroy

        // first transform into bot coordinates
        Point3Space originalTarget = this.popTarget();
        if ( originalTarget == null ) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println("Target Original: " + originalTarget);
        Point3Space target = transformIntoBotCoordinates(originalTarget);
        System.out.println("Target Transformed: " + target);

        // find angle to turn to
        final double angle = Math.atan2(-target.getX(), target.getZ());
        System.out.println("calculated angle: " + angle);

        // find distance to throw
        System.out.println("Distance: " + target.getZ());

        armController.executeThrow(angle, target.getZ());
    }
    
    protected void gotoIntermediateState() {
        // run ball detection
        // wait until ball is detected
        // wait until ball leaves screen
        // set timer and wait for it to expire
        
    }
    
    protected void gotoEndState() {
        // turn everything off
        // reset bot to initial configuration (erect)
        
    }


}
