#!/bin/bash
# runs this to communicate with the robot arm
cd armlab/java/src
# the number at the end might need adjusting depending on the result of --> ls /dev/ttyUSB*
java -cp "$CLASSPATH:~/armlab/java/armlab.jar" armlab.arm.ArmDriver -d /dev/ttyUSB[0]

