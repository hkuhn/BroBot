package main;


public class BroBotController implements Runnable {
    
    // const
    
    // args
    final protected BroBotControllerDelegate delegate;
    
    protected boolean isExecuting;


    // CONSTRUCTOR METHOD
    public BroBotController(BroBotControllerDelegate delegate) {
        
        this.delegate = delegate;
        
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
        //  3. Opponent State (possession)
        //      a. wait until a ball is out of frame
        //      b. once ball leaves frame for at least "t2" seconds, goto bot state
        
        


}
