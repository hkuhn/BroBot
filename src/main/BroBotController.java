package main;


public class BroBotController implements Runnable {
    
    // const
    
    // args
    final protected BroBotControllerDelegate        delegate;
    
    protected boolean                               isExecuting;
    protected boolean                               endOfGame;


    // CONSTRUCTOR METHOD
    public BroBotController(BroBotControllerDelegate delegate) {
        
        this.delegate = delegate;
        this.endOfGame = false;
        
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
    
    
    
    // STATE METHODS
    protected void gotoBotState() {
        // find circles
        // send centers to stereo reconstruction
        // find correspondences with minimum error
        // retrieve angles from lookup table
        // send angles to servos
        
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
