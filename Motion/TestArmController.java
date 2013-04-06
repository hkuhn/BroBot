package Motion;

import java.lang.*;
import java.awt.*;
import java.util.*;

public class TestArmController {

    public static void main (String[] args) {
        ArmController arm = new ArmController();

            arm.setOpenClaw();

            //arm.handMeBall();
            
            arm.setCloseClaw();

            arm.sendCommands(false);
            try {
                Thread.sleep(10000);
            } catch (Exception e) {}
            arm.setCloseClaw();
            arm.sendCommands(false);

            arm.setFirstJoint(0);
            arm.setSecondJoint(0);
            arm.setRotateJoint(0);
            arm.setWristJoint(0);
            arm.setClawRotateJoint(0);
            arm.sendCommands(false);

            


            arm.setThrowingAngle(-Math.PI/4);
            arm.executeThrow();



    }






}
