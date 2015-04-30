package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import edu.stanford.cs276.util.Dictionary;
import edu.stanford.cs276.util.Pair;

public class RunCorrector {

	public static LanguageModel languageModel;
	public static NoisyChannelModel nsm;
	
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		// Parse input arguments
		String uniformOrEmpirical = null;
		String queryFilePath = null;
		String goldFilePath = null;
		String extra = null;
		BufferedReader goldFileReader = null;
		if (args.length == 2) {
			// Run without extra and comparing to gold
			uniformOrEmpirical = args[0];
			queryFilePath = args[1];
		}
		else if (args.length == 3) {
			uniformOrEmpirical = args[0];
			queryFilePath = args[1];
			if (args[2].equals("extra")) {
				extra = args[2];
			} else {
				goldFilePath = args[2];
			}
		} 
		else if (args.length == 4) {
			uniformOrEmpirical = args[0];
			queryFilePath = args[1];
			extra = args[2];
			goldFilePath = args[3];
		}
		else {
			System.err.println(
					"Invalid arguments.  Argument count must be 2, 3 or 4" +
					"./runcorrector <uniform | empirical> <query file> \n" + 
					"./runcorrector <uniform | empirical> <query file> <gold file> \n" +
					"./runcorrector <uniform | empirical> <query file> <extra> \n" +
					"./runcorrector <uniform | empirical> <query file> <extra> <gold file> \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt data/gold.txt \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt extra \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt extra data/gold.txt \n");
			return;
		}
		
		if (goldFilePath != null ){
			goldFileReader = new BufferedReader(new FileReader(new File(goldFilePath)));
		}
		
		// Load models from disk
		System.out.println("Load models from disk");
		languageModel = LanguageModel.load(); 
	//	System.out.println("loaded");
		nsm = NoisyChannelModel.load();
	//	System.out.println("loaded");
		BufferedReader queriesFileReader = new BufferedReader(new FileReader(new File(queryFilePath)));
		nsm.setProbabilityType(uniformOrEmpirical);
		long loadTime   = System.currentTimeMillis();
		long totalLoadTime = loadTime - startTime;
		System.out.println("LOADING TIME: "+totalLoadTime/1000+" seconds ");
		System.out.println("Total unigrams="+languageModel.unigram.termCount()+", Total bigrams="+languageModel.bigram.termCount());
		int totalCount = 0;
		int yourCorrectCount = 0;
		String query = null;
		
		/*
		 * Each line in the file represents one query.  We loop over each query and find
		 * the most likely correction
		 */
 
		final double pRgivenEqualQ = 0.90; // if the query itself is a candidate.
		double log_pRgivenQ, log_pQgivenR, log_pQ;  
		long endTimeInsideFor = 0, startTimeInsideFor= 0; 
	//	System.out.println("query = queriesFileReader.readLine())");
		while ((query = queriesFileReader.readLine()) != null) {
						
			/*
			 * Your code here
			 */	
			/*
			// generate the candidates (already pruned)
			Set<Pair<String,Integer>> candidates = CandidateGenerator.get().getCandidates(query, languageModel);
			
			// score the candidates
			ArrayList<Pair<String, Double>> candidatesScores = new ArrayList<>();
			// first the query itself can be a candidate
			if(languageModel.IsInDictionary(query))
			{ 				 
				if(languageModel.IsInDictionary(query))
				{
					log_pQ = languageModel.computePQ(query);				
					log_pQgivenR = Math.log(pRgivenEqualQ) + BuildModels.MU*log_pQ; 
				//	System.out.println("pRgivenEqualQ  = " + pRgivenEqualQ + " log_pQ = " + log_pQ + " log_pQgivenR=" + log_pQgivenR);
					candidatesScores.add(new Pair<String, Double>(query, log_pQgivenR));
				} 
			} 
			
			for(Pair<String, Integer> candidate: candidates)
			{ 
				log_pRgivenQ = nsm.ecm_.editProbability(candidate.getFirst(), query, candidate.getSecond()); //  = BuildModels.MU;				
				log_pQ = languageModel.computePQ(candidate.getFirst());
				log_pQgivenR = log_pRgivenQ + BuildModels.MU*log_pQ;
				//System.out.println("log_pRgivenQ  = " + log_pRgivenQ + " log_pQ = " + log_pQ + " log_pQgivenR=" + log_pQgivenR);
				candidatesScores.add(new Pair<String, Double>(candidate.getFirst(), log_pQgivenR));
			} 
			*/
		//	startTimeInsideFor   = System.currentTimeMillis();
			// generate the candidates (already pruned)
			Set<Pair<String,ArrayList<Character>>> candidates = CandidateGenerator.get().getCandidates(query, languageModel);
			
			// score the candidates
			ArrayList<Pair<String, Double>> candidatesScores = new ArrayList<>();
			// first the query itself can be a candidate
			if(languageModel.IsInDictionary(query))
			{ 				 
				log_pQ = languageModel.computePQ(query);				
				log_pQgivenR = Math.log(pRgivenEqualQ) + BuildModels.MU*log_pQ; 
				//	System.out.println("pRgivenEqualQ  = " + pRgivenEqualQ + " log_pQ = " + log_pQ + " log_pQgivenR=" + log_pQgivenR);
				candidatesScores.add(new Pair<String, Double>(query, log_pQgivenR));
			} 
			
			for(Pair<String, ArrayList<Character>> candidate: candidates)
			{ 
				log_pRgivenQ = nsm.ecm_.editProbability(candidate.getFirst(), query, candidate.getSecond()); //  = BuildModels.MU;				
				log_pQ = languageModel.computePQ(candidate.getFirst());
				log_pQgivenR = log_pRgivenQ + BuildModels.MU*log_pQ;
				//System.out.println("log_pRgivenQ  = " + log_pRgivenQ + " log_pQ = " + log_pQ + " log_pQgivenR=" + log_pQgivenR);
				candidatesScores.add(new Pair<String, Double>(candidate.getFirst(), log_pQgivenR));
			} 
			
			//sort the candidates based on their score
		//	System.out.println("# of candidates is " + candidatesScores.size()); 
			Pair<String, Double> bestCandidate = Collections.max(candidatesScores, new Comparator<Pair<String, Double>>() 
			{
				@Override 
				public int compare(Pair<String, Double> x, Pair<String, Double> y) 
				{
				//	if(x.getSecond().equals(y.getSecond()))
				//		return x.getFirst().compareTo(y.getFirst());
				//	else 
						return x.getSecond().compareTo(y.getSecond());
				}
			}
			);
			
		//	String correctedQuery = candidatesScores.get(candidatesScores.size()-1).getFirst(); // highest score at the end
			String correctedQuery = bestCandidate.getFirst(); // bestCandidate
		//	endTimeInsideFor   = System.currentTimeMillis();
		//	System.out.println("lowest score = " + candidatesScores.get(0).getSecond().toString()); 
		//	System.out.println("highest score = " + candidatesScores.get(candidatesScores.size()-1).getSecond().toString()); 
			if ("extra".equals(extra)) {
				/*
				 * If you are going to implement something regarding to running the corrector, 
				 * you can add code here. Feel free to move this code block to wherever 
				 * you think is appropriate. But make sure if you add "extra" parameter, 
				 * it will run code for your extra credit and it will run you basic 
				 * implementations without the "extra" parameter.
				 */	
			}
			

			// If a gold file was provided, compare our correction to the gold correction
			// and output the running accuracy
			if (goldFileReader != null) {
				String goldQuery = goldFileReader.readLine();
				if (goldQuery.equals(correctedQuery)) {
					yourCorrectCount++;
				}
				totalCount++;
			//	System.out.println("totalCount = " + totalCount + ", yourCorrectCount = " + yourCorrectCount);				
			}
		//	System.out.println(correctedQuery);
		}
		queriesFileReader.close();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		//System.out.println("INSIDE FOR TIME: "+(endTimeInsideFor-startTimeInsideFor)/1000+" seconds ");
		System.out.println("RUNNING TIME: "+totalTime/1000+" seconds ");
		System.out.println("totalCount = " + totalCount);
		System.out.println("yourCorrectCount = " + yourCorrectCount);
	}	
}
