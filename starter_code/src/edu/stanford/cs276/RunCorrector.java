package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Set;

public class RunCorrector {
	
	private static boolean debug = true;

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
		
		
// Turn on when searching for optimal parameters.
boolean profile=true;
for (Parameters.lambda = 0.1; Parameters.lambda <= 0.50; Parameters.lambda += 0.05)
//for (Parameters.edit_probability = 0.01; Parameters.edit_probability <= 0.10; Parameters.edit_probability += 0.01)
//for (Parameters.mu = 0.0; Parameters.mu <= 1.0; Parameters.mu += 0.05)
{	
		if (goldFilePath != null ){
			goldFileReader = new BufferedReader(new FileReader(new File(goldFilePath)));
		}

		// Load models from disk
		languageModel = LanguageModel.load(); 
		nsm = NoisyChannelModel.load();
		BufferedReader queriesFileReader = new BufferedReader(new FileReader(new File(queryFilePath)));
		nsm.setProbabilityType(uniformOrEmpirical);
		
		int totalCount = 0;
		int candidateCorrectCount = 0;
		int yourCorrectCount = 0;
		String R = null;
		
		/*
		 * Each line in the file represents one query.  We loop over each query and find
		 * the most likely correction
		 */
		while ((R = queriesFileReader.readLine()) != null) {
			
			String correctedQuery = R;
			/*
			 * Your code here
			 */			

			// P(Q|R) = P(R|Q)P(Q)
			
			SortedMap<Double, String> candidateMap = new TreeMap<>();
			
			Map<String, Integer> candidates = CandidateGenerator.get().getCandidates(correctedQuery, 1);
			
			// Consider Q == R, 0 edit distance.
			candidates.put(R, 0);
			
			// Score candidates.
			for (Map.Entry<String, Integer> Q : candidates.entrySet())
			{
				double P_of_Q_given_R = nsm.P_of_R_given_Q(R,Q) + Parameters.mu * languageModel.P_of_Q(Q) ;
				candidateMap.put(P_of_Q_given_R, Q.getKey());
			}			
			
			double max = 1.0;
			if (!candidateMap.isEmpty())
			{
				// Chose best candidate.
				max = candidateMap.lastKey();
				
				//correctedQuery = bestCandidate
				correctedQuery = candidateMap.get(max);
			}
			
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

if (profile) System.out.print(".");
if (!profile) System.out.println(correctedQuery);
				if (goldQuery.equals(correctedQuery)) {
					yourCorrectCount++;
				}
				
else
{
	if (!profile) System.out.println("Max: " + max + " Candidates contain gold?: " + candidateMap.containsValue(goldQuery));
}

				if (candidateMap.containsValue(goldQuery)) candidateCorrectCount++;

				totalCount++;
if (!profile) {
System.out.printf("%1.2f%% out of %1.2f%% possible", (double)yourCorrectCount / (double)totalCount, (double)candidateCorrectCount / (double)totalCount);
System.out.println(" TOTAL CORRECT: " + yourCorrectCount + " / " + totalCount + " CANDIDATE CORRECT: " + candidateCorrectCount + " / " + totalCount + "\n");
long currentTime = System.currentTimeMillis();
long remainingTime = ((currentTime - startTime) / totalCount) * (455 - totalCount);
Date eta = new Date(currentTime + remainingTime);
DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
System.out.println("REMAINING TIME: " + (remainingTime / 1000 / 60) + " minutes.  TOTAL TIME: " + (((currentTime - startTime) / totalCount) * 455 / 1000 / 60) + " minutes.  ETA: " + formatter.format(eta));
System.out.println();
}
			}
		}
		System.out.println();
		queriesFileReader.close();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.printf("lambda: %1.2f :: %1.2f%% out of %1.2f%% possible", Parameters.lambda, (double)yourCorrectCount / (double)totalCount, (double)candidateCorrectCount / (double)totalCount);
		System.out.printf("\nmu: %1.2f :: %1.2f%% out of %1.2f%% possible", Parameters.mu, (double)yourCorrectCount / (double)totalCount, (double)candidateCorrectCount / (double)totalCount);
		System.out.printf("\nP(edit): %1.2f :: %1.2f%% out of %1.2f%% possible", Parameters.edit_probability, (double)yourCorrectCount / (double)totalCount, (double)candidateCorrectCount / (double)totalCount);
		System.out.println(" TOTAL CORRECT: " + yourCorrectCount + " / " + totalCount + " CANDIDATE CORRECT: " + candidateCorrectCount + " / " + totalCount + "\n");
		System.out.println("RUNNING TIME: "+totalTime/1000/60 +" minutes ");
}

	}
}
