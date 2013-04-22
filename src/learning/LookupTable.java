package learning;

import java.util.*;
import april.jmat.*;

public class LookupTable {

    // const
    private static final double cup_offset = -0.127;
    
    // args
    private static HashMap<Double,double[]> Map;
    private static double[] keys;
    
    // CONSTRUCTOR METHOD
    public LookupTable(ArrayList<Double> key_array, ArrayList<double[]> angles_array) {
        
        // init hashmap
        this.Map = new HashMap(key_array.size());
        this.keys = new double[key_array.size()];
        
        // build map
        for (int i = 0; i < angles_array.size(); i++) {
            double cur_key = key_array.get(i);
            double[] angles = new double[3];
			angles[0] = angles_array.get(i)[0];
			angles[1] = angles_array.get(i)[1];
			angles[2] = angles_array.get(i)[2];
            Map.put(cur_key, angles);
            keys[i] = cur_key;
        }

		Arrays.sort(keys);
    }
    
    // RETRIEVE VALUE
    public double[] getAngles(double key) {
        
		//for (int i = 0; i < keys.length; i++) {
			//System.out.println(keys[i]);
		//}
        // search for key index
        double corrected_key = key + (double)cup_offset;
		//System.out.println("key: " + corrected_key);

        int index = Arrays.binarySearch(keys, corrected_key);

        if ( index < 0 ) {
            index += 1;
            index *= -1;
        }

        if ( index >= keys.length ) {
            index = keys.length - 1;
        }

        if ( index < 0 ) {
            index = 0;
        }

		//System.out.println(index);
        double hash_key = keys[index];

        double[] angles = Map.get(hash_key);
        
        return angles;
    }
    
    



}
