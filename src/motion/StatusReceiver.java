package motion;

import java.io.IOException;
import lcmtypes.*;
import lcm.lcm.*;

public class StatusReceiver implements LCMSubscriber {

    private volatile dynamixel_status_list_t stats;

    // gets last stats and then sets to null
    public synchronized dynamixel_status_list_t getStats() {
    	dynamixel_status_list_t stats = this.stats;
    	this.stats = null;
    	return stats;
    }

    public synchronized void setStats(dynamixel_status_list_t stats){
        this.stats = stats;
    }

    public void messageReceived(LCM lcm, String channel, lcm.lcm.LCMDataInputStream ins) {
        try {
            dynamixel_status_list_t stats = new dynamixel_status_list_t(ins);
            this.setStats(stats);
        } catch(IOException e) {
            System.err.println("Shit -- could not encode as dynamixel_status_list_thing in status receiver");
            e.printStackTrace(System.err);
        }
    }

}
