package learning;

import learning.util.*;
import java.io.*;
import java.awt.*;
import java.io.FileReader;

public class PutInData {

	public static void main (String[] args) {
		
		File dataFile = new File ("../../learning_data/data_type"); 
		DataReader dataReader = new DataReader (dataFile);

		dataReader.parse();
		LookUpTable lookUpTable = new LookUpTable (dataReader.getParsedOutput(), dataReader.getParsedInput());

				

	}  





}

