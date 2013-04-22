package learning;

import java.util.*;

public class LookupTable {

    private static HashMap<double,double[]> Map;
    private static double[] keys;
    
    // CONSTRUCTOR METHOD
    public LookupTable(ArrayList<double> key_array, ArrayList<double[]> angles_array) {
        
        // init hashmap
        this.Map = new HashMap(key_array.length);
        this.keys = new double[key_array.length];
        
        // build map
        for (int i = 0; i < angles_array.size(); i++) {
            double cur_key = key_array.get(i);
            double[] angles = angles_array.get(i);
            Map.put(cur_key, angles);
            keys[i] = cur_key;
        }
    }
    
    // RETRIEVE VALUE
    public double[] getAngles(double key) {
        
        // search for key index
        int index = Array.binarySearch(keys, key);
        double key = keys[index];
        double[] angles = Map.get(key);
        
        return angles;
    }
    
    



}