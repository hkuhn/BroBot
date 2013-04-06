import java.awt.*;
import java.io.*;

import javax.swing.*;

import lcm.lcm.*;
import april.jmat.*;
import april.util.*;
import april.vis.*;
import armlab.lcmtypes.*;


public class RobotArmGUI implements LCMSubscriber
{
    VisWorld vw = new VisWorld();
    VisLayer vl  = new VisLayer(vw);
    VisCanvas vc = new VisCanvas(vl);
    ParameterGUI pg = new ParameterGUI();

    public RobotArmGUI()
    {
        JFrame jf = new JFrame();
        jf.setLayout(new BorderLayout());
        jf.add(vc, BorderLayout.CENTER);

        pg.addDoubleSlider("t1", "Joint 1", -180, 180, 0);
        pg.addDoubleSlider("t2", "Joint 2", -180, 180, 0);
        pg.addDoubleSlider("t3", "Joint 3", -180, 180, 0);
        jf.add(pg, BorderLayout.SOUTH);

        pg.addListener(new JointParamListener());
        update();

        /* Point vis camera the right way */
        vl.cameraManager.uiLookAt(
                new double[] {-2.27870, -6.35237, 4.75098 },
                new double[] { 0,  0, 0.00000 },
                new double[] { 0.13802,  0.40084, 0.90569 }, true);

        jf.setSize(800, 800);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static void main(String[] args)
    {
        RobotArmGUI gui = new RobotArmGUI();

        /* Subscribe to ARM_STATUS */
        LCM.getSingleton().subscribe("ARM_STATUS", gui);
    }

    class JointParamListener implements ParameterListener
    {
        public void parameterChanged(ParameterGUI pg, String name)
        {
            // TODO: Send commands to servos
            // NOTE: Check validity of angles and self collision
            update();
        }
    }

    void update()
    {
        VisChain arm = new VisChain();

        VzBox segBottom = new VzBox(0.5, 0.5, 0.5, new VzMesh.Style(Color.red));
        arm.add(segBottom);

        VzBox seg1 = new VzBox(0.5, 0.5, 1, new VzMesh.Style(Color.orange));
        double theta1 = pg.gd("t1") * Math.PI/180;
        double theta2 = pg.gd("t2") * Math.PI/180;
        arm.add(LinAlg.rotateZ(theta1),
                /* we rotate about an end instead of the center by first translating it appropriately */
                LinAlg.translate(0, 0, 0.5), LinAlg.rotateX(theta2), LinAlg.translate(0, 0, 0.5), seg1);

        /* build a gripper by chaining fingers together */
        VzBox fingerShort = new VzBox(0.1, 0.1, 0.8, new VzMesh.Style(Color.darkGray));
        VisChain segFixed = new VisChain(
                LinAlg.translate(-0.07, 0, 0), fingerShort,
                LinAlg.translate(+0.15, 0, 0), fingerShort );
        arm.add(LinAlg.translate(0, 0, 1.3), segFixed);

        /* build a 3 finger gripper */
        VzBox fingerLong = new VzBox(0.1, 0.1, 1, new VzMesh.Style(Color.gray));
        VisChain segGripper = new VisChain(
                LinAlg.translate(-0.15, 0, 0), fingerLong,
                LinAlg.translate(+0.15, 0, 0), fingerLong,
                LinAlg.translate(+0.15, 0, 0), fingerLong );
        double theta3 = pg.gd("t3") * Math.PI/180;
        arm.add(LinAlg.translate(0, 0.25, -0.5), LinAlg.rotateX(theta3), LinAlg.translate(0, 0, 0.5), segGripper);

        VisWorld.Buffer vb = vw.getBuffer("arm");
        vb.addBack(arm);
        vb.swap();
    }

    @Override
    public void messageReceived(LCM lcm, String channel, LCMDataInputStream dins)
    {
        try {
            dynamixel_status_list_t arm_status = new dynamixel_status_list_t(dins);
            /* access positions using arm_status.statuses[i].position_radians */
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
