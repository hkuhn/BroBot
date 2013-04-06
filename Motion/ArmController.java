package Motion;

import java.lang.*;
import java.util.*;
import java.awt.*;

import lcm.lcm.*;
import lcmtypes.*;

import april.jmat.MathUtil;
import april.util.TimeUtil;
// controls commands to the arm
public class ArmController {

    static final private double FIRST_JOINT_THROW_ANGLE = 0;
    static final private double WRIST_JOINT_THROW_ANGLE = 0;
    static final private double OPEN_CLAW_ANGLE = Math.toRadians(50);
    static final private double CLOSE_CLAW_ANGLE = Math.toRadians(119);
    static final private double CUP_HEIGHT = 0.1140;



    private double rotateJoint;
    private double firstJoint;
    private double secondJoint;
    private double wristJoint;
    private double clawRotateJoint;
    private double claw;
    private double throwingAngle;

    protected AngleCalculator angleCalculator;
    protected StatusReceiver statusReceiver;
    private dynamixel_command_list_t cmdlist;



    public ArmController() {

        cmdlist = new dynamixel_command_list_t();
        cmdlist.len = 6;
        cmdlist.commands = new dynamixel_command_t[cmdlist.len];

        // initialize cmdlist's array of commands
        for (int i=0; i < cmdlist.len; i++) {
            dynamixel_command_t cmd = new dynamixel_command_t();
            cmd.position_radians = MathUtil.mod2pi(0);
            cmd.utime = TimeUtil.utime();
            cmd.speed = 0.1;
            cmd.max_torque = 0.5;
            cmdlist.commands[i] = cmd;
        }

        angleCalculator = new AngleCalculator();
        statusReceiver = new StatusReceiver();
        LCM.getSingleton().subscribe ("ARM_STATUS", statusReceiver);


    }

    public void setThrowingAngle (double angle) {
        this.throwingAngle = angle;
    }


    public void setRotateJoint (double angle) {
        this.rotateJoint = angle;
        setCmd (0, angle);
    }

    public void setFirstJoint (double angle) {
        this.firstJoint = angle;
        setCmd (1, angle);
    }

    public void setSecondJoint (double angle) {
        this.secondJoint = angle;
        setCmd (2, angle);
    }

    public void setWristJoint (double angle) {
        this.wristJoint = angle;
        setCmd (3, angle);
    }


    public void setClawRotateJoint (double angle) {
        this.clawRotateJoint = angle;
        setCmd (4, angle);
    }

    public void setClaw (double angle) {

        this.claw = angle;
        setCmd (5, angle);
    }

    public void setOpenClaw () {
        this.claw = OPEN_CLAW_ANGLE;
        setCmd (5, OPEN_CLAW_ANGLE);

    }

    public void setCloseClaw () {
        this.claw = CLOSE_CLAW_ANGLE;
        setCmd (5, CLOSE_CLAW_ANGLE);

    }

    public void handMeBall() {
        setOpenClaw();
        for (int i=0; i < cmdlist.len; i++) {
            dynamixel_command_t cmd = new dynamixel_command_t();
            //cmd.position_radians = MathUtil.mod2pi(0);
            cmd.utime = TimeUtil.utime();
            cmd.speed = 0.3;
            cmd.max_torque = 2.0;
            cmdlist.commands[i] = cmd;
        }

        setCloseClaw();
        // reset to old values
        for (int i=0; i < cmdlist.len; i++) {
            dynamixel_command_t cmd = new dynamixel_command_t();
            cmd.position_radians = MathUtil.mod2pi(0);
            cmd.utime = TimeUtil.utime();
            cmd.speed = 1.0;
            cmd.max_torque = 2.0;
            cmdlist.commands[i] = cmd;
        }
    }

    protected void setCmd (int index, double angle) {
        cmdlist.commands[index].position_radians = MathUtil.mod2pi(angle);

    }

    public void sendCommands (boolean throwing) {
        //TODO: send to LCM
        LCM.getSingleton().publish ("ARM_COMMAND", cmdlist);
        if (throwing == false)
            waitUntilDone();
        else
            openClawAtAngle(this.throwingAngle);
    }

    public void executeThrow () {

        double threshold = Math.PI/10;
        setFirstJoint (Math.PI/2 - threshold); // initial position of arm
        sendCommands(false);
        try {
            Thread.sleep(300);
        } catch (Exception e) {
            System.out.println(e);
        }
        setFirstJoint (-Math.PI/2 + threshold);
        sendCommands(true);



    }

    protected void openClawAtAngle (double angle) {

        double effectiveAngle = MathUtil.mod2pi(angle);
        System.out.println("angle is " + effectiveAngle);
        double threshold = Math.PI/6;
        dynamixel_status_list_t previousStats = null;
            while (true) {
                dynamixel_status_list_t stats = statusReceiver.getStats();

                if ( stats == null ) continue;
                if ( previousStats == null ) {
                    previousStats = stats;
                    continue;
                }
               // System.out.println(stats.statuses[1].position_radians);
                if (stats.statuses[1].position_radians > (effectiveAngle - threshold)
                    /*&& stats.statuses[2].position_radians < (effectiveAngle + threshold)*/) {
                    setClaw(OPEN_CLAW_ANGLE);
                    sendCommands(false);
                    System.out.println ("GOT TO ANGLE");
                    break;
                } else {
/*
                    boolean allEqual = true;
                    for ( int i = 0; i < stats.len; i++ ) {
                        boolean equal = stats.statuses[i].position_radians == previousStats.statuses[i].position_radians;
                        allEqual = (allEqual && equal);
                    }
                    previousStats = stats;
                    if ( allEqual ) {
                        break;
                     }
               */ }
                //previousStats = stats;
            }
    }

    protected void waitUntilDone() {

        dynamixel_status_list_t previousStats = null;
            while (true) {
                dynamixel_status_list_t stats = statusReceiver.getStats();

                if ( stats == null ) continue;
                if ( previousStats == null ) {
                    previousStats = stats;
                    continue;
                }
                boolean allEqual = true;
                for ( int i = 0; i < stats.len; i++ ) {
                    boolean equal = stats.statuses[i].position_radians == previousStats.statuses[i].position_radians;
                    allEqual = (allEqual && equal);
                }
                previousStats = stats;
                if ( allEqual ) {
                    break;
                 }
            }
    }

    public void returnToNeutral () { // return arm to straight up position
        setRotateJoint (0);
        setFirstJoint(0); // align and straighten
        sendCommands(false);

        setSecondJoint(0);
        setWristJoint(0);
        setClawRotateJoint(0);
        setClaw (OPEN_CLAW_ANGLE);
        sendCommands(false);


    }

    public void pickUpCup (double x, double y) { // pick up at at this point

        double angles[] = new double[6];
        angles = angleCalculator.calcNextAngles(x, y, CUP_HEIGHT);
        setRotateJoint (angles[0]);
        setFirstJoint (angles[1]);
        setSecondJoint (angles[2]);
        setWristJoint (angles[3]);
        setClawRotateJoint (angles[4]);
        setClaw (CLOSE_CLAW_ANGLE);


    }


}
