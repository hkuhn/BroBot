package Motion;

import java.lang.*;
import java.util.*;
import java.awt.*;

import lcm.lcm.*;
import lcmtypes.*;

import april.jmat.MathUtil;
import april.util.TimeUtil;
// controls commands to the arm
// TODO: include lcmtypes
public class ArmController {

    static final private double FIRST_JOINT_THROW_ANGLE = 0;
    static final private double WRIST_JOINT_THROW_ANGLE = 0;



    private double rotateJoint;
    private double firstJoint;
    private double secondJoint;
    private double wristJoint;
    private double clawRotateJoint;
    private double claw;
    private double throwingAngle;

    private StatusReceiver statusReceiver;
    private dynamixel_command_list_t cmdlist;

    public ArmController() {
        //TODO: add subscriber and receiver

        cmdlist = new dynamixel_command_list_t();
        cmdlist.len = 6;
        cmdlist.commands = new dynamixel_command_t[cmdlist.len];

        // initialize cmdlist's array of commands
        for (int i=0; i < cmdlist.len; i++) {
            dynamixel_command_t cmd = new dynamixel_command_t();
            cmd.position_radians = MathUtil.mod2pi(0);
            cmd.utime = TimeUtil.utime();
            cmd.speed = 1.0;
            cmd.max_torque = 1.0;
            cmdlist.commands[i] = cmd;
        }

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

    public void setOpenClaw () {

        this.claw = 0;
        setCmd (5, 0);
    }

    public void setCloseClaw () {
        this.claw = Math.toRadians(120);
        setCmd (5, Math.toRadians(120));
    }

    protected void setCmd (int index, double angle) {
        cmdlist.commands[index].position_radians = MathUtil.mod2pi(angle);

    }

    public void sendCommands (boolean openClaw) {
        //TODO: send to LCM
        LCM.getSingleton().publish ("ARM_COMMAND", cmdlist);
        if (openClaw == false)
            waitUntilDone();
        else
            openClawAtAngle(this.throwingAngle);
    }

    public void executeThrow () {

        setSecondJoint (-Math.PI/2); // initial position of arm
        sendCommands(false);
        try {
            Thread.sleep(300);
        } catch (Exception e) {
            System.out.println(e);
        }
        setSecondJoint (Math.PI/2);
        sendCommands(true);



    }

    protected void openClawAtAngle (double angle) {

        double effectiveAngle = MathUtil.mod2pi(angle);
        double threshold = Math.PI/8;
        dynamixel_status_list_t previousStats = null;
            while (true) {
                dynamixel_status_list_t stats = statusReceiver.getStats();

                if ( stats == null ) continue;
                if ( previousStats == null ) {
                    previousStats = stats;
                    continue;
                }
                System.out.println(stats.statuses[2].position_radians);
                if (stats.statuses[2].position_radians > (angle - threshold)
                    /*&& stats.statuses[2].position_radians < (angle + threshold)*/) {
                    setOpenClaw();
                    sendCommands(false);
                    System.out.println ("GOT TO ANGLE");
                    break;
                } else {

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


}
