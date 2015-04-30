package edu.stanford.cs276;

public class Parameters 
{

	/*
	 * P_int(w2|w1) interpolation of unigram probabilities and bigram probabilities
	 * 
	 * To take into account the data sparsity problem where some bigrams that appear in
	 * the queries might not be in our training corpus, we interpolate unigram probabilities
	 * with the bigram probabilities to get our final interpolated conditional probabilities.
	 * 
	 * 		Pint(w2|w1) = λPmle(w2) + (1 − λ)Pmle(w2|w1)
	 * 
	 * Try setting λ to a small value in the beginning, say 0.1, and later experiment with
	 * varying this parameter to see if you can get better correction accuracies on the development
	 * dataset. However, be careful not to overfit your development dataset.
	 */
	public static boolean interpolated = true;
	public static double lambda = 0.01;
	/*
	lambda: 0.01 :: 0.78% out of 0.88% possible
	mu: 0.50 :: 0.78% out of 0.88% possible
	P(edit): 0.05 :: 0.78% out of 0.88% possible TOTAL CORRECT: 353 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 1 minutes 

	lambda: 0.02 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 1 minutes 

	lambda: 0.03 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 2 minutes 

	lambda: 0.04 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 3 minutes 

	lambda: 0.05 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 3 minutes 

	lambda: 0.06 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 4 minutes 

	lambda: 0.07 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 4 minutes 

	lambda: 0.08 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 5 minutes 

	lambda: 0.09 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 6 minutes 

	lambda: 0.10 :: 0.77% out of 0.88% possible
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455	
	RUNNING TIME: 6 minutes 
	 */
	
	/*
	 * The uniform cost edit distance model simplifies the computation of the noisy channel
	 * probability by assuming that any single edit in the Damerau-Levenshtein distance is
	 * equally likely, i.e., having the same probability. Again, try different values for that
	 * uniform probability, but in the beginning 0.01∼0.10 is appropriate.
	 */
	public static double edit_probability = .05;
	/*	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.01 :: 0.77% out of 0.88% possible TOTAL CORRECT: 351 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 1 minutes 
	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.02 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 1 minutes 
	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.03 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 2 minutes 
	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.04 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 3 minutes 
	
	mu: 0.50 :: 0.78% out of 0.88% possible
	P(edit): 0.05 :: 0.78% out of 0.88% possible TOTAL CORRECT: 353 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 4 minutes 
	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.06 :: 0.77% out of 0.88% possible TOTAL CORRECT: 351 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 5 minutes 
	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.07 :: 0.77% out of 0.88% possible TOTAL CORRECT: 351 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 5 minutes 
	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.08 :: 0.77% out of 0.88% possible TOTAL CORRECT: 349 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 6 minutes 
	
	mu: 0.50 :: 0.76% out of 0.88% possible
	P(edit): 0.09 :: 0.76% out of 0.88% possible TOTAL CORRECT: 348 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 7 minutes 
	
	mu: 0.50 :: 0.77% out of 0.88% possible
	P(edit): 0.10 :: 0.77% out of 0.88% possible TOTAL CORRECT: 350 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 8 minutes 
*/
	
	
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
	
	public static double mu = 0.50;
	/*
	mu: 0.00 :: 0.48% out of 0.80% possible
	P(edit): 0.05 :: 0.48% out of 0.80% possible TOTAL CORRECT: 219 / 455 CANDIDATE CORRECT: 364 / 455
	RUNNING TIME: 1 minutes 
	
	mu: 0.05 :: 0.66% out of 0.88% possible
	P(edit): 0.05 :: 0.66% out of 0.88% possible TOTAL CORRECT: 301 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 1 minutes 
	
	mu: 0.10 :: 0.67% out of 0.88% possible
	P(edit): 0.05 :: 0.67% out of 0.88% possible TOTAL CORRECT: 304 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 2 minutes 
	
	mu: 0.15 :: 0.70% out of 0.88% possible
	P(edit): 0.05 :: 0.70% out of 0.88% possible TOTAL CORRECT: 320 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 3 minutes 
	
	mu: 0.20 :: 0.73% out of 0.88% possible
	P(edit): 0.05 :: 0.73% out of 0.88% possible TOTAL CORRECT: 334 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 4 minutes 

	mu: 0.25 :: 0.76% out of 0.88% possible
	P(edit): 0.05 :: 0.76% out of 0.88% possible TOTAL CORRECT: 347 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 4 minutes 

	mu: 0.30 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 351 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 5 minutes 

	mu: 0.35 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 6 minutes 
	
	mu: 0.40 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 351 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 7 minutes 
	
	mu: 0.45 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 352 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 7 minutes 

	mu: 0.50 :: 0.78% out of 0.88% possible
	P(edit): 0.05 :: 0.78% out of 0.88% possible TOTAL CORRECT: 353 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 8 minutes 
	
	mu: 0.55 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 351 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 9 minutes 
	
	mu: 0.60 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 349 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 10 minutes 
	
	mu: 0.65 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 350 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 10 minutes 
	
	mu: 0.70 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 349 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 11 minutes 
	
	mu: 0.75 :: 0.77% out of 0.88% possible
	P(edit): 0.05 :: 0.77% out of 0.88% possible TOTAL CORRECT: 349 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 12 minutes 
	
	mu: 0.80 :: 0.76% out of 0.88% possible
	P(edit): 0.05 :: 0.76% out of 0.88% possible TOTAL CORRECT: 347 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 13 minutes 
	
	mu: 0.85 :: 0.76% out of 0.88% possible
	P(edit): 0.05 :: 0.76% out of 0.88% possible TOTAL CORRECT: 344 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 13 minutes 
	
	mu: 0.90 :: 0.75% out of 0.88% possible
	P(edit): 0.05 :: 0.75% out of 0.88% possible TOTAL CORRECT: 341 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 14 minutes 
	
	mu: 0.95 :: 0.74% out of 0.88% possible
	P(edit): 0.05 :: 0.74% out of 0.88% possible TOTAL CORRECT: 337 / 455 CANDIDATE CORRECT: 402 / 455
	RUNNING TIME: 15 minutes 
	*/

}
