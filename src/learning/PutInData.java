package learning;

import learning.util.*;
import java.io.*;
import java.awt.*;
import java.io.FileReader;
import java.util.*;

import april.jmat.*;


public class PutInData {

	public static void main (String[] args) {
		
		File dataFile = new File ("../../learning_data/data_type"); 
		DataReader dataReader = new DataReader (dataFile);

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

			LookupTable lookUpTable = new LookupTable (distanceList, anglesList);

			double[] out = lookUpTable.getAngles(1.5);
			System.out.println(out[0] + " " + out[1] + " " + out[2]);
		} catch (Exception e) {
			System.out.println("ERROR");
		}

				

	}  





}

