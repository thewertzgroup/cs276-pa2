package edu.stanford.cs276;

import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.cs276.util.Dictionary;
import edu.stanford.cs276.util.Pair;

public class CandidateGenerator implements Serializable {


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

	// Generate all candidates for the target query with edit distance 1
	public Set<Pair<String, ArrayList<Character>>> getEdit1(String query, boolean finalEdit, LanguageModel lm) throws Exception {
		 
			Set<Pair<String, ArrayList<Character>>> edit1 = new HashSet<Pair<String, ArrayList<Character>>>();	
			String oneCandidate; 
			// splits
			ArrayList<Pair<String, String>>splits = new ArrayList<>(); 
			String left, right;
			for(int charInd = 0; charInd < query.length(); charInd++)
			{ 
				left  = query.substring(0, charInd); 
				right = query.substring(charInd);
				 
				if(!finalEdit)
					splits.add(new Pair<String, String>(left,right)); 
				else 
				{ 
					int lastSpace = left.lastIndexOf(' ');
					String leftQuery=""; 
					if (lastSpace ==-1)
						splits.add(new Pair<String, String>(left,right));
					else 
						leftQuery = left.substring(0, lastSpace);
					int firstSpace = right.indexOf(' '); 
					String rightQuery=""; 
					if (firstSpace ==-1)
						splits.add(new Pair<String, String>(left,right)); 
					else
						rightQuery = right.substring(firstSpace+1);
					if(lastSpace!=-1 && firstSpace!=-1 && lm.IsInDictionary(leftQuery) && lm.IsInDictionary(rightQuery))
						splits.add(new Pair<String, String>(left,right));
						
				} 
				
				
			} 
			//deletes
			Set<Pair<String, ArrayList<Character>>>deletes = new HashSet<Pair<String, ArrayList<Character>>>();	 		
			for(int splitInd = 0; splitInd < splits.size(); splitInd++)
			{
				if(!splits.get(splitInd).getSecond().isEmpty())			
				{ 
					oneCandidate = splits.get(splitInd).getFirst() + splits.get(splitInd).getSecond().substring(1); // delete the first character in second splitted string
															
					ArrayList<Character> edits = new ArrayList<>(); 
					edits.add('d');// for delete
					if(!splits.get(splitInd).getFirst().isEmpty()) //xy typed as x
					{ 
						edits.add(splits.get(splitInd).getFirst().charAt(splits.get(splitInd).getFirst().length()-1));
					} 
					else 
						edits.add('$'); // to indicate delete at beginning of query. 
					edits.add(splits.get(splitInd).getSecond().charAt(0));											
					deletes.add(new Pair<String,ArrayList<Character> >(oneCandidate,edits));
					 
				} 
			}		
		    //transporses
			Set<Pair<String, ArrayList<Character>>>transporses = new HashSet<Pair<String, ArrayList<Character>>>();	 
			for(int splitInd = 0; splitInd < splits.size(); splitInd++)
			{
				if(splits.get(splitInd).getSecond().length()>1)			
				{ 
					oneCandidate = splits.get(splitInd).getFirst() + splits.get(splitInd).getSecond().charAt(1) 
							     + splits.get(splitInd).getSecond().charAt(0) +  splits.get(splitInd).getSecond().substring(2); // transporse the first  and secondcharacter in second splitted string
										
					ArrayList<Character> edits = new ArrayList<>(); 
					edits.add('t');// for transporse
					edits.add(splits.get(splitInd).getSecond().charAt(0)); // xy typed as yx
					edits.add(splits.get(splitInd).getSecond().charAt(1));										
					transporses.add(new Pair<String,ArrayList<Character> >(oneCandidate,edits));
					
				} 
			}	
			// substitutes
			Set<Pair<String, ArrayList<Character>>>replaces = new HashSet<Pair<String, ArrayList<Character>>>();	 
			for(int splitInd = 0; splitInd < splits.size(); splitInd++)
			{
				if(!splits.get(splitInd).getSecond().isEmpty())			
				{ 
					for(int alphInd = 0; alphInd < alphabet.length; alphInd++)
					{ 
						oneCandidate = splits.get(splitInd).getFirst() + alphabet[alphInd]  
									 +  splits.get(splitInd).getSecond().substring(1); // replace the first  character in second splitted string by a character from alphabet						
						
						ArrayList<Character> edits = new ArrayList<>(); 
						edits.add('s');// for substitutes
						edits.add(alphabet[alphInd]); // y typed as x
						edits.add(splits.get(splitInd).getSecond().charAt(0));																	
						replaces.add(new Pair<String, ArrayList<Character>>(oneCandidate,edits));
					 
					} 
				} 
			}
					
			// inserts
			Set<Pair<String, ArrayList<Character>>>inserts = new HashSet<Pair<String, ArrayList<Character>>>();	 
			for(int splitInd = 0; splitInd < splits.size(); splitInd++)
			{
					for(int alphInd = 0; alphInd < alphabet.length; alphInd++)
					{ 
						oneCandidate = splits.get(splitInd).getFirst() + alphabet[alphInd]  
									 +  splits.get(splitInd).getSecond().substring(0); // Insert before the first  character in second splitted string, a character from alphabet
						ArrayList<Character> edits = new ArrayList<>(); 	
						edits.add('i');// for substitutes
						if(!splits.get(splitInd).getFirst().isEmpty()) //x typed as xy
						{ 
							edits.add(splits.get(splitInd).getFirst().charAt(splits.get(splitInd).getFirst().length()-1));
						} 
						else 
							edits.add('$'); 
						edits.add(alphabet[alphInd]);						
						inserts.add(new Pair<String, ArrayList<Character>>(oneCandidate,edits));
					} 			
			}	
			edit1.addAll(deletes);
			edit1.addAll(transporses);
			edit1.addAll(replaces); 
			edit1.addAll(inserts);
			return edit1;			
		} 
	// Generate all candidates for the target query
		// added the language model as we need it for pruning
		public Set<Pair<String,ArrayList<Character>>> getCandidates(String query, LanguageModel lm) throws Exception {
			Set<Pair<String,ArrayList<Character>>> candidates = new HashSet<Pair<String,ArrayList<Character>>>();	
			/*
			 * Your code here
			 */		
			// should we add the original query????
			//???
			boolean finalEdit = false; 
			long startTime   = System.currentTimeMillis();
			Set<Pair<String,ArrayList<Character>>> edit1, edit2;
			// get candidates at edit distance 1
			//int numEdit1 = 0; 
			//int numEdit2 = 0;
			edit1  = getEdit1(query, finalEdit, lm);
			finalEdit = true;
			// get candidates at edit distance 2
			for(Pair<String,ArrayList<Character>> e1: edit1)
			{ 
				if(lm.IsInDictionary(e1.getFirst())) // pruning, all the words in the candidate query must be in the dictionary learned from the corpus
				{ 
					candidates.add(e1); // candidate at edit distance 1, with the modification
				//	numEdit1++; 
				} 
				edit2 = getEdit1(e1.getFirst(), finalEdit, lm);
				for(Pair<String,ArrayList<Character>> e2: edit2)
				{ 				
					if(lm.IsInDictionary(e2.getFirst())) // pruning, all the words in the candidate query must be in the dictionary learned from the corpus
					{  
						e2.getSecond().addAll(e1.getSecond()); // need to add distance 1 edits   
						candidates.add(e2); // candidate at edit distance 2, with the modifications
					//	numEdit2++; 
					} 
				}
			//	System.out.println(numEdit2 + " out of " + edit2.size() + " edits 2, in dictionary ");
			//	numEdit2 = 0;
			}  
			//System.out.println(numEdit1 + " out of " + edit1.size() + " edits 1, in dictionary ");
			
			//long endTime   = System.currentTimeMillis();
			//long totalTime = endTime - startTime;
			//System.out.println("Candidate TIME: "+totalTime/1000+" seconds ");
			return candidates;
		}
	public Set<String> getEdit1Orig(String query) throws Exception {
		 
		Set<String> edit1 = new HashSet<String>();	
		String oneCandidate; 
		// splits
		ArrayList<Pair<String, String>>splits = new ArrayList<>(); 
		String left, right; 		
		for(int charInd = 0; charInd < query.length(); charInd++)
		{ 
			left  = query.substring(0, charInd); 
			right = query.substring(charInd);
			splits.add(new Pair<String, String>(left,right)); 
		} 
		//deletes
		Set<String>deletes = new HashSet<String>();	 		
		for(int splitInd = 0; splitInd < splits.size(); splitInd++)
		{
			if(!splits.get(splitInd).getSecond().isEmpty())			
			{ 
				oneCandidate = splits.get(splitInd).getFirst() + splits.get(splitInd).getSecond().substring(1); // delete the first character in second splitted string			
				//edits.add(new pair<DELETE, new Pair<>>)
				deletes.add(oneCandidate);
			} 
		}		
	    //transporses
		Set<String>transporses = new HashSet<String>();	 
		for(int splitInd = 0; splitInd < splits.size(); splitInd++)
		{
			if(splits.get(splitInd).getSecond().length()>1)			
			{ 
				oneCandidate = splits.get(splitInd).getFirst() + splits.get(splitInd).getSecond().charAt(1) 
						     + splits.get(splitInd).getSecond().charAt(0) +  splits.get(splitInd).getSecond().substring(2); // transporse the first  and secondcharacter in second splitted string
				transporses.add(oneCandidate);
			} 
		}	
		// replaces
		Set<String>replaces = new HashSet<String>();	 
		for(int splitInd = 0; splitInd < splits.size(); splitInd++)
		{
			if(!splits.get(splitInd).getSecond().isEmpty())			
			{ 
				for(int alphInd = 0; alphInd < alphabet.length; alphInd++)
				{ 
					oneCandidate = splits.get(splitInd).getFirst() + alphabet[alphInd]  
								 +  splits.get(splitInd).getSecond().substring(1); // replace the first  character in second splitted string by a character from alphabet
					replaces.add(oneCandidate);
				} 
			} 
		}
				
		// inserts
		Set<String>inserts = new HashSet<String>();	 
		for(int splitInd = 0; splitInd < splits.size(); splitInd++)
		{
				for(int alphInd = 0; alphInd < alphabet.length; alphInd++)
				{ 
					oneCandidate = splits.get(splitInd).getFirst() + alphabet[alphInd]  
								 +  splits.get(splitInd).getSecond().substring(0); // Insert before the first  character in second splitted string, a character from alphabet
					inserts.add(oneCandidate);
				} 			
		}	
		edit1.addAll(deletes);
		edit1.addAll(transporses);
		edit1.addAll(replaces); 
		edit1.addAll(inserts);
		return edit1;			
	} 
	
	// Generate all candidates for the target query
	// added the language model as we need it for pruning
	public Set<Pair<String,Integer>> getCandidatesOrig(String query, LanguageModel lm) throws Exception {
		Set<Pair<String, Integer>> candidates = new HashSet<Pair<String, Integer>>();	
		/*
		 * Your code here
		 */		
		// should we add the original query????
		//???
		Set<String> edit1, edit2;
		// get candidates at edit distance 1
		edit1  = getEdit1Orig(query); 
		// get candidates at edit distance 2
		for(String e1: edit1)
		{ 
			if(lm.IsInDictionary(e1)) // pruning, all the words in the candidate query must be in the dictionary learned from the corpus 
				candidates.add(new Pair<String, Integer>(e1, 1)); // candidate at edit distance 1
			edit2 = getEdit1Orig(e1);
			for(String e2: edit2)
			{ 				
				if(lm.IsInDictionary(e2)) // pruning, all the words in the candidate query must be in the dictionary learned from the corpus
					candidates.add(new Pair<String, Integer>(e2, 2)); // candidate at edit distance 2
			}
		}  
		return candidates;
	}


}
