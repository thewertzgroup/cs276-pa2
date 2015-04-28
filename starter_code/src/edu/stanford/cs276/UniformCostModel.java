package edu.stanford.cs276;

import java.util.ArrayList;

public class UniformCostModel implements EditCostModel {
	
	@Override
	public double editProbability(String original, String R, int distance) {
		//return 0.5;
		/*
		 * Your code here
		 */
		
	/*	double pRgivenEqualQ = 0.90; // try values in range 0.90~0.95
		if(distance == 0) 
			return pRgivenEqualQ;*/ 
		
		double pEdit = 0.05; // try values in range 0.01~0.10
		double log_pRgivenQ = 0;  
		for(int factorInd = 0; factorInd<distance; factorInd++)
			log_pRgivenQ += Math.log(pEdit);  /// very important to work in log domain, otherwise you get underflow and everything is equal to zero
		return log_pRgivenQ; 
	}
	@Override
	public double editProbability(String original, String R, ArrayList<Character> edits) {
		int distance = edits.size()/3; 
		double pEdit = 0.05; // try values in range 0.01~0.10
		double log_pRgivenQ = 0;  
		for(int factorInd = 0; factorInd<distance; factorInd++)
			log_pRgivenQ += Math.log(pEdit);  /// very important to work in log domain, otherwise you get underflow and everything is equal to zero
		return log_pRgivenQ; 	
			
		}
}
