package learning;

import learning.util.*;
import java.io.*;
import java.awt.*;
import java.io.FileReader;
import java.util.*;


public class PutInData {

	public static void main (String[] args) {
		
		File dataFile = new File ("../../learning_data/data_type"); 
		DataReader dataReader = new DataReader (dataFile);

		dataReader.parse();
		Matrix angles = new Matrix (dataReader.getParsedInput());
		Matrix distance = new Matrix (dataReader.getParsedOutput());		

		ArrayList<double[]> anglesList = new ArrayList<double>();
		ArrayList<double> distanceList = new ArrayList<double>();

		for (int i=0; i < angles.getRowDimension(); i++) {
			anglesList.add(angles.getRow(i).copyArray());
			distanceList.add (distance.getRow(i).copyArray());
		}

		LookUpTable lookUpTable = new LookUpTable (anglesList, distanceList);

				

	}  





}

