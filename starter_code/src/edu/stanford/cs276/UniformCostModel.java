package edu.stanford.cs276;

import java.util.Map.Entry;

public class UniformCostModel implements EditCostModel 
{
	
	@Override
	public double editProbability(String Q, String R, int distance) 
	{
		/*
		 * Your code here
		 */
		// Use Log probability P(edit)^d = (0.01~0.10)^d
		double probability = distance * Math.log( Parameters.edit_probability );

		return probability;
	}

	@Override
	public double P_of_R_given_Q(String R, Entry<String, Integer> Q) 
	{
		return editProbability(Q.getKey(), R, Q.getValue());
	}
}
