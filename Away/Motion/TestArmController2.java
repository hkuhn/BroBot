package Motion;

import java.lang.*;
import java.awt.*;
import java.util.*;

public class TestArmController {

    public static void main (String[] args) {
        ArmController arm = new ArmController();

            arm.setFirstJoint(0);
            arm.setSecondJoint(0);
            arm.setRotateJoint(0);
            arm.setWristJoint(0);
            arm.setClawRotateJoint(0);
            arm.setCloseClaw();
            arm.sendCommands(false);

            arm.setThrowingAngle(-Math.PI/4);
            arm.executeThrow();



    }






}
