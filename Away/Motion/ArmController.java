package Motion;

import java.lang.*;
import java.util.*;
import java.awt.*;

// controls commands to the arm
// TODO: include lcmtypes
public class ArmController {

    private double rotateJoint;
    private double firstJoint;
    private double secondJoint;
    private double wristJoint;
    private double claw;
    
    public ArmController() {
        //TODO: add subscriber and receiver
    }
    
    public void setRotateJoint (double angle) {
        
        this.rotateJoint = angle;
    }
    
    public void setFirstJoint (double angle) {
        this.firstJoint = angle;
    }
    
    public void setSecondJoint (double angle) {
        this.secondJoint = angle;
    }

    public void setWristJoint (double angle) {
        this.wristJoint = angle;
    }
    
    public void openClaw () {
        
        this.claw = 0;
    }
    
    public void closeClaw () {
        this.claw = Math.toRadians(120);
    }
    
    public void sendCommands () {
        //TODO: send to LCM
    }

}