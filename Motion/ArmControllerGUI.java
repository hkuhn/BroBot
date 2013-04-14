package Motion;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;

import lcm.lcm.*;
import lcmtypes.*;

import april.util.*;
import javax.swing.JFrame;

import april.jmat.MathUtil;
import april.util.TimeUtil;
// controls commands to the arm
public class ArmControllerGUI {

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

    // GUI STUFF
    JFrame jf = new JFrame("GUI Testing");
    ParameterGUI pg = new ParameterGUI();


    protected AngleCalculator angleCalculator;
    protected StatusReceiver statusReceiver;
    private dynamixel_command_list_t cmdlist;



    public ArmControllerGUI() {

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

        angleCalculator = new AngleCalculator();
        statusReceiver = new StatusReceiver();
        LCM.getSingleton().subscribe ("ARM_STATUS", statusReceiver);


        // INITIALIZE THE GUI
        initGUI();


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
        setRotateJoint (0);
        setFirstJoint (Math.PI/8);
        setSecondJoint (Math.PI/6);
        setWristJoint (Math.PI/6); // initial position of arm

        sendCommands(false);
        try {
            Thread.sleep(2500);
        } catch (Exception e) {
            System.out.println(e);
        }
        setWristJoint (-Math.PI/4);
        setFirstJoint (0);
        setSecondJoint (0);
        //setWristJoint (this.throwingAngle - threshold);
        sendCommands(true);



    }
    public void executeThrowGUI() {

        double threshold = Math.PI/10;
        setRotateJoint (pg.gd("base_b4"));
        setFirstJoint (pg.gd("1st_b4"));
        setSecondJoint (pg.gd("2nd_b4"));
        setWristJoint (pg.gd("wrist_b4")); // initial position of arm

        sendCommands(false);
        try {
            Thread.sleep(2500);
        } catch (Exception e) {
            System.out.println(e);
        }
        //setWristJoint (-Math.PI/4);
        setWristJoint (0);
        setFirstJoint (0);
        setSecondJoint (0);
        //setWristJoint (this.throwingAngle - threshold);
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




    public void initGUI()
    {
        /******* GUI STUFF ********/
        // pg documentation
        // http://code.google.com/p/lcm/source/browse/trunk/lcm-java/lcm/util/ParameterGUI.java?r=35
        // check javax.swing

        // SLIDERS

        // Servo Configs Before
        pg.addDoubleSlider("base_b4","base_b4",0,Math.PI, 0); // BASE ROTATION
        pg.addDoubleSlider("1st_b4","1st_b4",-Math.PI/8,Math.PI/8, Math.PI/8); // 1st 
        pg.addDoubleSlider("2nd_b4","2nd_b4",-Math.PI/6,Math.PI/6, Math.PI/6); // 2nd
        pg.addDoubleSlider("wrist_b4","wrist_b4",-Math.PI/6,Math.PI/6, Math.PI/6); // WRIST
        pg.addDoubleSlider("claw_b4","claw_b4",0,Math.PI/2, OPEN_CLAW_ANGLE); // CLAW
        
        // Servo Configs After
        pg.addDoubleSlider("base_after","base_after",0,Math.PI, 0); // BASE ROTATION
        pg.addDoubleSlider("1st_after","1st_after",-Math.PI/8,Math.PI/8, Math.PI/8); // 1st 
        pg.addDoubleSlider("2nd_after","2nd_after",-Math.PI/6,Math.PI/6, Math.PI/6); // 2nd
        pg.addDoubleSlider("wrist_after","wrist_after",-Math.PI/6,Math.PI/6, Math.PI/6); // WRIST
        pg.addDoubleSlider("claw_after","claw_after",0,Math.PI/2, OPEN_CLAW_ANGLE); // CLAW
        
        pg.addButtons("launch_button", "Launch!!");
        




        // LAYOUT OF THE GUI
        jf.setLayout(new BorderLayout());
        jf.add(pg, BorderLayout.SOUTH);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(512, 300);
    }


}
