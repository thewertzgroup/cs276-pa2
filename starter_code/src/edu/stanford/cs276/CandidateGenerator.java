package edu.stanford.cs276;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.stanford.cs276.util.Pair;

public class CandidateGenerator implements Serializable, Callable {

	private static boolean debug = false;
	
	private static CandidateGenerator cg_;
	
	private String query_;
	private Integer distance_;

	// Don't use the constructor since this is a Singleton instance
	private CandidateGenerator() {}
	
	public static CandidateGenerator get() throws Exception{
		if (cg_ == null ){
			cg_ = new CandidateGenerator();
		}
		return cg_;
	}
	
	public CandidateGenerator(String query, Integer distance)
	{
		query_ = query;
		distance_ = distance;
	}
	
	
	public static final Character[] alphabet = {
					'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
					'o','p','q','r','s','t','u','v','w','x','y','z',
					'0','1','2','3','4','5','6','7','8','9',
					' ',','};
	
	@Override
	public Map<String, Integer> call() throws Exception {
		return getCandidates(query_, distance_);
	}

	// Generate all candidates for the target query
	public Map<String, Integer> getCandidates(String query, Integer distance) throws Exception {
		/*
		 * Your code here
		 */
		
		LanguageModel languageModel = LanguageModel.load(); 

		Map<String, Integer> candidates = new HashMap<>();
		
		/* For speed, just use "candidates".
		Map<String, Integer> additions = new HashMap<>();
		Map<String, Integer> deletions = new HashMap<>();
		Map<String, Integer> transpositions = new HashMap<>();
		Map<String, Integer> substitutions = new HashMap<>();
		*/
		if (debug) System.out.println(query + " : \n");
		
		String candidate;
		int maxDistance = 2;

		// Additions (user added a letter - delete it)
		candidate = query.substring(1,query.length());
		if (distance + languageModel.distance(candidate) + languageModel.bigramDistance(candidate) <= maxDistance) /*additions*/candidates.put(candidate, distance);
		if (debug) System.out.println("\t" + candidate);
		for (int i=1; i<query.length()-1; i++)
		{
			candidate = query.substring(0,i) + query.substring(i+1,query.length());
			if (distance + languageModel.distance(candidate) + languageModel.bigramDistance(candidate) <= maxDistance) /*additions*/candidates.put(candidate, distance);
			if (debug) System.out.println("\t" + candidate);
		}
		candidate = query.substring(0, query.length()-1);
		if (distance + languageModel.distance(candidate) + languageModel.bigramDistance(candidate) <= maxDistance) /*additions*/candidates.put(candidate, distance);
		if (debug) System.out.println("\t" + candidate);
		
		if (debug) System.out.println("Candidates after additions: " + /*additions*/candidates.size() + "\n\n");		

		// Deletions (user deleted a letter - add it back in)
		for (Character c : alphabet)
		{
			for (int i=0; i<=query.length(); i++)
			{
				candidate = query.substring(0, i) + c + query.substring(i,query.length());
				if (distance + languageModel.distance(candidate) + languageModel.bigramDistance(candidate) <= maxDistance) /*deletions*/candidates.put(candidate, distance);
				if (debug) System.out.println("\t" + candidate);
			}
		}
		if (debug) System.out.println("Candidates after deletions: " + /*deletions*/candidates.size() + "\n\n");
		
		// Transpositions (user transposed two letters - swap back)
		for (int i=0; i<query.length()-1; i++)
		{
			char[] queryArray = query.toCharArray();
			char temp = queryArray[i];
			queryArray[i] = queryArray[i+1];
			queryArray[i+1] = temp;
			candidate = new String(queryArray);
			if (distance + languageModel.distance(candidate) + languageModel.bigramDistance(candidate) <= maxDistance) /*transpositions*/candidates.put(candidate, distance);
			if (debug) System.out.println("\t" + candidate);
		}
		if (debug) System.out.println("Candidates after transpositions: " + /*transpositions*/candidates.size() + "\n\n");
		
		// Substitutions (user substituted a letter - replace it)
		for (Character c : alphabet)
		{
			for (int i=0; i<query.length(); i++)
			{
				char[] queryArray = query.toCharArray();
				queryArray[i] = c;
				candidate = new String(queryArray);
				if (distance + languageModel.distance(candidate) + languageModel.bigramDistance(candidate) <= maxDistance) /*substitutions*/candidates.put(candidate, distance);
				if (debug) System.out.println("\t" + candidate);
			}
		}
		if (debug) System.out.println("Candidates after substitutions: " + /*substitutions*/candidates.size() + "\n\n");

		// Candidates > 1 edit distance away from query will need to be pruned
		
		/* For speed, just use "candidates".
		candidates.putAll(additions);
		candidates.putAll(deletions);
		candidates.putAll(transpositions);
		candidates.putAll(substitutions);
		*/
		
		if (distance < maxDistance) 
		{
	        final ExecutorService service;
	        service = Executors.newFixedThreadPool(8);
	        
			ArrayList<Future<Map<String, Integer>>> tasks = new ArrayList<Future<Map<String, Integer>>>();

			Map<String, Integer> finalCandidates = new ConcurrentHashMap<>();
			
			for (String c : candidates.keySet())
			{
				tasks.add(service.submit(new CandidateGenerator(c, distance+1)));
				if (distance + languageModel.distance(c) + languageModel.bigramDistance(c) <= maxDistance-1) finalCandidates.put(c, distance);				
			}
			for (Future<Map<String, Integer>> task : tasks)
			{
				//Map<String, Integer> c2Candidates = getCandidates(c, 2); // Implemented in Call(...);
				Map<String, Integer> c2Candidates = task.get(); 
				for (String c2 : c2Candidates.keySet())
				{
					if (null == finalCandidates.get(c2)) finalCandidates.put(c2, distance+1); // Don't step on a level 1 candidate.
				}

			}
			
			if (service.shutdownNow().size() > 0) throw new RuntimeException("All getCandidates() tasks not finished.");
			
			return finalCandidates;
		}
				
		return candidates;
	}


}
