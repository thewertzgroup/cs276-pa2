package edu.stanford.cs276;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.stanford.cs276.util.Pair;

public class CandidateGenerator implements Serializable {

	private static boolean debug = false;

	private static CandidateGenerator cg_;
	
	// Don't use the constructor since this is a Singleton instance
	private CandidateGenerator() {}
	
	public static CandidateGenerator get() throws Exception{
		if (cg_ == null ){
			cg_ = new CandidateGenerator();
		}
		return cg_;
	}
	
	
	public static final Character[] alphabet = {
					'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
					'o','p','q','r','s','t','u','v','w','x','y','z',
					'0','1','2','3','4','5','6','7','8','9',
					' ',','};
	
	// Generate all candidates for the target query
	public Map<String, Integer> getCandidates(String query, Integer distance) throws Exception {
		/*
		 * Your code here
		 */
		
		LanguageModel languageModel = LanguageModel.load(); 

		Map<String, Integer> candidates = new HashMap<>();
		Map<String, Integer> additions = new HashMap<>();
		Map<String, Integer> deletions = new HashMap<>();
		Map<String, Integer> transpositions = new HashMap<>();
		Map<String, Integer> substitutions = new HashMap<>();
		
		if (debug) System.out.println(query + " : \n");
		
		String candidate;

		// Additions (user added a letter - delete it)
		candidate = query.substring(1,query.length());
		if (distance == 1 || languageModel.inDictionary(candidate)) additions.put(candidate, distance);
		if (debug) System.out.println("\t" + candidate);
		for (int i=1; i<query.length()-1; i++)
		{
			candidate = query.substring(0,i) + query.substring(i+1,query.length());
			if (distance == 1 || languageModel.inDictionary(candidate)) additions.put(candidate, distance);
			if (debug) System.out.println("\t" + candidate);
		}
		candidate = query.substring(0, query.length()-1);
		if (distance == 1 || languageModel.inDictionary(candidate)) additions.put(candidate, distance);
		if (debug) System.out.println("\t" + candidate);
		
		if (debug) System.out.println("Candidates after additions: " + additions.size() + "\n\n");		

		// Deletions (user deleted a letter - add it back in)
		for (Character c : alphabet)
		{
			for (int i=0; i<=query.length(); i++)
			{
				candidate = query.substring(0, i) + c + query.substring(i,query.length());
				if (distance == 1 || languageModel.inDictionary(candidate)) deletions.put(candidate, distance);
				if (debug) System.out.println("\t" + candidate);
			}
		}
		if (debug) System.out.println("Candidates after deletions: " + deletions.size() + "\n\n");
		
		// Transpositions (user transposed two letters - swap back)
		for (int i=0; i<query.length()-1; i++)
		{
			char[] queryArray = query.toCharArray();
			char temp = queryArray[i];
			queryArray[i] = queryArray[i+1];
			queryArray[i+1] = temp;
			candidate = new String(queryArray);
			if (distance == 1 || languageModel.inDictionary(candidate)) transpositions.put(candidate, distance);
			if (debug) System.out.println("\t" + candidate);
		}
		if (debug) System.out.println("Candidates after transpositions: " + transpositions.size() + "\n\n");
		
		// Substitutions (user substituted a letter - replace it)
		for (Character c : alphabet)
		{
			for (int i=0; i<query.length(); i++)
			{
				char[] queryArray = query.toCharArray();
				queryArray[i] = c;
				candidate = new String(queryArray);
				if (distance == 1 || languageModel.inDictionary(candidate)) substitutions.put(candidate, distance);
				if (debug) System.out.println("\t" + candidate);
			}
		}
		if (debug) System.out.println("Candidates after substitutions: " + substitutions.size() + "\n\n");

		// Candidates > 1 edit distance away from query will need to be pruned
		
		candidates.putAll(additions);
		candidates.putAll(deletions);
		candidates.putAll(transpositions);
		candidates.putAll(substitutions);
		
		if (distance == 1) 
		{
			Map<String, Integer> finalCandidates = new HashMap<>();

			for (String c : candidates.keySet())
			{
				finalCandidates.putAll(getCandidates(c, 2));
				if (languageModel.inDictionary(c)) finalCandidates.put(c, distance);
			}
			return finalCandidates;
		}
				
		return candidates;
	}


}
