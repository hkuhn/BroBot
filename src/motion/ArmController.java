package motion;

import java.io.File;
import java.lang.*;
import java.util.*;
import java.awt.*;

import april.jmat.Matrix;
import lcm.lcm.*;
import lcmtypes.*;

import april.jmat.MathUtil;
import april.util.TimeUtil;
import learning.LookupTable;
import learning.util.DataReader;

// controls commands to the arm
public class ArmController {

    static final private double FIRST_JOINT_THROW_ANGLE = 0;
    static final private double WRIST_JOINT_THROW_ANGLE = 0;
    static final private double OPEN_CLAW_ANGLE = Math.toRadians(50);
    static final private double CLOSE_CLAW_ANGLE = Math.toRadians(119);
    static final private double CUP_HEIGHT = 0.1140;
    static final private double DefaultOpenAngle = Math.toRadians(50);
    static final private double DistanceAdjust = 0.35;
    static final private double AngleAdjust = -0.15;
    static final private long CockBackTime = 4000;
    static final private double AngleConstraints = 3 * (Math.PI / 8);

    private double rotateJoint;
    private double firstJoint;
    private double secondJoint;
    private double wristJoint;
    private double clawRotateJoint;
    private double claw;
    private double throwingAngle;
    private static final LookupTable lookupTable;

    protected StatusReceiver statusReceiver;
    private dynamixel_command_list_t cmdlist;


    static {
        File dataFile = new File("/home/slessans/BroBot/learning-data/data_table");
        DataReader dataReader = new DataReader (dataFile);

        LookupTable lt = null;
        try {
            dataReader.parse();
            Matrix angles = dataReader.getParsedInput();
            Matrix distance = dataReader.getParsedOutput();

            ArrayList<double[]> anglesList = new ArrayList<double[]>();
            ArrayList<Double> distanceList = new ArrayList<Double>();

            for (int i=0; i < angles.getRowDimension(); i++) {
                anglesList.add(angles.getRow(i).copyArray());
                distanceList.add (distance.getRow(i).copyArray()[0]);
            }

            lt = new LookupTable (distanceList, anglesList);
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

        lookupTable = lt;

    }

    public ArmController() {

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

    public void executeThrow(final double angle, final double distance) {

        double ang = angle;
        if ( angle > AngleConstraints || angle < AngleConstraints ) {
            ang = 0;
        }
        ang += AngleAdjust;

        setRotateJoint(ang);
        setClaw(DefaultOpenAngle);
        sendCommands(false);

        double [] angles = lookupTable.getAngles(distance + DistanceAdjust);
        setFirstJoint(angles[0]);
        setSecondJoint(angles[1]);
        setWristJoint(angles[2]); // initial position of arm


        // slow the speed during cockback
        for (int i=0; i < cmdlist.len; i++) {
            cmdlist.commands[i].speed = 0.5;
            cmdlist.commands[i].max_torque = 0.3;
        }

        sendCommands(false);


        try {
            Thread.sleep(CockBackTime);
        } catch (Exception e) {
            System.out.println(e);
        }


        //setWristJoint (-Math.PI/4);
        setWristJoint(0);
        setFirstJoint(0);
        setSecondJoint(0);

        for (int i=0; i < cmdlist.len; i++) {
            cmdlist.commands[i].speed = 1.0;
            cmdlist.commands[i].max_torque = 1.0;
        }

        sendCommands(false);

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
                if (stats.statuses[3].position_radians > (effectiveAngle - threshold)
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


}
