package edu.stanford.cs276;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.cs276.util.Pair;

public class CandidateGenerator implements Serializable {

	private static boolean debug = true;

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
	public Set<String> getCandidates(String query) throws Exception {
		Set<String> candidates = new HashSet<String>();	
		Set<String> additions = new HashSet<String>();
		Set<String> deletions = new HashSet<String>();
		Set<String> transpositions = new HashSet<String>();
		Set<String> substitutions = new HashSet<String>();
		/*
		 * Your code here
		 */
		
		if (debug) System.out.println(query + " : \n");
		
		// Additions (user added a letter - delete it)
		String candidate;
		additions.add(candidate = query.substring(1,query.length()));
		if (debug) System.out.println("\t" + candidate);
		for (int i=1; i<query.length()-1; i++)
		{
			additions.add(candidate = query.substring(0,i) + query.substring(i+1,query.length()));
			if (debug) System.out.println("\t" + candidate);
		}
		additions.add(candidate = query.substring(0, query.length()-1));
		if (debug) System.out.println("\t" + candidate);
		
		if (debug) System.out.println("Candidates after additions: " + additions.size() + "\n\n");		

		// Deletions (user deleted a letter - add it back in)
		for (Character c : alphabet)
		{
			for (int i=0; i<=query.length(); i++)
			{
				deletions.add(candidate = query.substring(0, i) + c + query.substring(i,query.length()));
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
			transpositions.add(candidate = new String(queryArray));
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
				substitutions.add(candidate = new String(queryArray));
				if (debug) System.out.println("\t" + candidate);
			}
		}
		if (debug) System.out.println("Candidates after substitutions: " + substitutions.size() + "\n\n");

		// Candidates > 1 edit distance away from query will need to be pruned
		
		candidates.addAll(additions);
		candidates.addAll(deletions);
		candidates.addAll(transpositions);
		candidates.addAll(substitutions);
		
		
		System.exit(0);
		
		return candidates;
	}


}
