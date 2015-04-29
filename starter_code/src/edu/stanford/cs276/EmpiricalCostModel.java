package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Scanner;

public class EmpiricalCostModel implements EditCostModel{
	
	private static boolean debug = false;
	
	public EmpiricalCostModel(String editsFile) throws IOException 
	{
		BufferedReader input = new BufferedReader(new FileReader(editsFile));
		System.out.println("Constructing edit distance map...");
		String line = null;
		while ((line = input.readLine()) != null) 
		{
			Scanner lineSc = new Scanner(line);
			lineSc.useDelimiter("\t");
			String noisy = lineSc.next();
			String clean = lineSc.next();
			
			// Determine type of error and record probability
			/*
			 * Your code here
			 */
			
			if (debug) System.out.println("noisy: " + noisy + " clean: " + clean);
		}

		input.close();
		System.out.println("Done.");
	}

	
	// You need to update this to calculate the proper empirical cost
	@Override
	public double editProbability(String Q, String R, int distance) 
	{
		return 0.5;
		/*
		 * Your code here
		 */
	}


	@Override
	public double P_of_R_given_Q(String R, Entry<String, Integer> Q) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
