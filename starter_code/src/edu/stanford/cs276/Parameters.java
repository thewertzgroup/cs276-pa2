package edu.stanford.cs276;

public class Parameters 
{

	/*
	 * P_int(w2|w1) interpolation of unigram probabilities and bigram probabilities
	 */
	public static double lambda = 0.1;
	
	/*
	 * The uniform cost edit distance model simplifies the computation of the noisy channel
	 * probability by assuming that any single edit in the Damerau-Levenshtein distance is
	 * equally likely, i.e., having the same probability. Again, try different values for that
	 * uniform probability, but in the beginning 0.01∼0.10 is appropriate.
	 */
	public static double edit_probability = .05;
	
	/*
	 * A factor to consider in this model is that our input query, R, may indeed be the right
	 * one in a majority of cases. Experiment with the different assignments to P(R|Q) in the
	 * case where R = Q , but a reasonable range is 0.90∼0.95.
	 */
	public static double P_of_R_equal_Q = 0.925;
	
	
	/*
	 * When putting the probabilities of the language model and the channel model together
	 * to score the candidates (remember to use log space), we can use a parameter to relatively
	 * weight the different models.
	 * 
	 * 		P(Q|R) ∝ P(R|Q)P(Q)^µ
	 * 
	 * At first start with µ = 1, and later, experiment with different values of µ to see which
	 * one gives you the best spelling correction accuracy.
	 */
	
	// public static double mu = 1.0;
	// 0.02% out of 0.64% possible TOTAL CORRECT: 4 / 255 CANDIDATE CORRECT: 162 / 255

	// public static double mu = 0.5;
	// 0.13% out of 0.47% possible TOTAL CORRECT: 14 / 105 CANDIDATE CORRECT: 49 / 105

	public static double mu = 0.15;
	// mu: 0.15 :: 0.48% out of 0.63% possible TOTAL CORRECT: 217 / 455 CANDIDATE CORRECT: 285 / 455

	/*
		mu: 0.50 :: 0.10% out of 0.63% possible TOTAL CORRECT: 46 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 2483 seconds 
	
		mu: 0.45 :: 0.12% out of 0.63% possible TOTAL CORRECT: 56 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 5073 seconds 
	
		mu: 0.40 :: 0.15% out of 0.63% possible TOTAL CORRECT: 68 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 7734 seconds 
	
		mu: 0.35 :: 0.18% out of 0.63% possible TOTAL CORRECT: 81 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 10352 seconds 
	
		mu: 0.30 :: 0.25% out of 0.63% possible TOTAL CORRECT: 116 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 12967 seconds 
	
		mu: 0.25 :: 0.34% out of 0.63% possible TOTAL CORRECT: 155 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 15634 seconds 
	
		mu: 0.20 :: 0.43% out of 0.63% possible TOTAL CORRECT: 194 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 18292 seconds 
	
		mu: 0.15 :: 0.48% out of 0.63% possible TOTAL CORRECT: 217 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 20965 seconds 
		
		mu: 0.10 :: 0.48% out of 0.63% possible TOTAL CORRECT: 219 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 23583 seconds 
		
		mu: 0.05 :: 0.48% out of 0.63% possible TOTAL CORRECT: 219 / 455 CANDIDATE CORRECT: 285 / 455
		RUNNING TIME: 26265 seconds 
		
		mu: 0.00 :: 0.48% out of 0.51% possible TOTAL CORRECT: 219 / 455 CANDIDATE CORRECT: 231 / 455
		RUNNING TIME: 28910 seconds 
	*/

}
