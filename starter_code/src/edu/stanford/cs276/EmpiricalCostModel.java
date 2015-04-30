package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import edu.stanford.cs276.util.Pair;

public class EmpiricalCostModel implements EditCostModel{
	
	private static boolean debug = true;
	
	Map<Pair<Character, Character>, Integer> del = new HashMap<>();
	Map<Pair<Character, Character>, Integer> ins = new HashMap<>();
	Map<Pair<Character, Character>, Integer> sub = new HashMap<>();
	Map<Pair<Character, Character>, Integer> trans = new HashMap<>();
	
	private Pair<Character, Character> diff(String clean, String noisy)
	{
		if (noisy.equals(clean)) throw new RuntimeException("Equal strings in diff(String, String).");

		StringBuffer sb = new StringBuffer();
		char[] c = clean.toCharArray();
		char[] n = noisy.toCharArray();
		Pair<Character, Character> p = null;
		
		int i=0;
		while (i < c.length && i < n.length)
		{
			if (c[i] != n[i]) 
			{
				if (c.length > n.length)
				{
					// char was deleted
					char x = i > 0 ? c[i-1] : '\0';
					char y = c[i];
					p = new Pair(x,y);
				}
				else
				{
					// return inserted char
					char x = i > 0 ? c[i-1] : '\0';
					char y = n[i];
					p = new Pair(x,y);
				}
				return p;
			}
			else 
			{ 
				i++; 
			}
		}
		if (c.length > n.length)
		{
			// return deleted char
			char x = c[i-1];
			char y = c[i];
			p = new Pair(x, y);
		}
		else
		{
			// return inserted char
			char x = c[i-1];
			char y = n[i];
			p = new Pair(x, y);
		}
		
		return p;
	}
	
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
			if (noisy.length() != clean.length())
			{
				if (debug) System.out.println("clean:\t" + clean + "\nnoisy:\t" + noisy);
				if (noisy.length() > clean.length())
				{
					// Inserted character
					Pair<Character, Character> p = diff(clean, noisy);
					System.out.println("ins['" + p.getFirst() + "', '" + p.getSecond() + "']\n");
					if (null == ins.get(p))
					{
						ins.put(p, 1);
					}
					else
					{
						ins.put(p,  ins.get(p) + 1);
					}
				}
				else
				{
					// Deleted character
					Pair<Character, Character> p = diff(clean, noisy);
					System.out.println("del['" + p.getFirst() + "', '" + p.getSecond() + "']\n");
					if (null == del.get(p))
					{
						del.put(p,  1);
					}
					else
					{
						del.put(p,  del.get(p) + 1);
					}
				}
			}
			else
			{
				// Find transpositions xy --> yx and substitutions x --> y
				if (debug) System.out.println("clean:\t" + clean + "\nnoisy:\t" + noisy);
				System.out.println("Transposition or Substitution\n");
			}
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
