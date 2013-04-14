package Motion;

import java.lang.*;
import java.awt.*;
import java.util.*;

public class TestArmController {

    public static void main (String[] args) {
        ArmControllerGUI arm = new ArmControllerGUI();

            arm.setOpenClaw();

            //arm.handMeBall();

            arm.setCloseClaw();

            arm.sendCommands(false);
            //try {
            //    Thread.sleep(1000);
            //} catch (Exception e) {}
            arm.setCloseClaw();
            arm.setClawRotateJoint(Math.PI/2);
            arm.sendCommands(false);

            arm.setFirstJoint(0);
            arm.setSecondJoint(0);
            arm.setRotateJoint(0);
            arm.setWristJoint(0);
            arm.sendCommands(false);

            arm.setThrowingAngle(Math.PI/4);
            //arm.executeThrowGUI();



    }






}
