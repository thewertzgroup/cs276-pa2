package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import edu.stanford.cs276.util.Dictionary;
import edu.stanford.cs276.util.Pair;


public class LanguageModel implements Serializable {

	private static LanguageModel lm_;
	/* Feel free to add more members here.
	 * You need to implement more methods here as needed.
	 * 
	 * Your code here ...
	 */
	Dictionary<String> unigram = new Dictionary<>();
	Dictionary<Pair<String,String>> bigram = new Dictionary<>();
	
	public boolean IsInDictionary(String query)
	{ 
		String[] words = query.trim().split("\\s+");			
		for(String word:words)
		{ 
			if(unigram.count(word)==0) //pruning
			{ 
				return false; 				
			}
		} 
		return true; 
	}
	// most likely in dictionary: not 100%
	public boolean IsInDictionary(int firstIndex, int lastIndex, ArrayList<Pair<Integer, Integer>> errorIntervals)
	{ 
		
		for(Pair<Integer, Integer> errorInterval: errorIntervals)
		{ 
			if( (firstIndex<=(errorInterval.getFirst()+1) &&  (errorInterval.getFirst()+1) <= lastIndex) || (firstIndex<=(errorInterval.getSecond()-1) &&  (errorInterval.getSecond()-1) <= lastIndex) )
				return false; 
		}
		return true;  
	}
	
	public ArrayList<Pair<Integer, Integer>> findError(String query)
	{ 
		ArrayList<Pair<Integer, Integer>> wrongWords = new ArrayList<>();  
		String[] words = query.trim().split("\\s+");
		int beginIndex, justAfterLastIndex; // with one extra index before the word and one extra index after it
		for(String word:words)
		{ 
			if(unigram.count(word)==0) 
			{ 
				beginIndex = Math.max(0,query.indexOf(word)-1); // go back one index
				justAfterLastIndex = Math.min(beginIndex + word.length() + 1, query.length()); // go forward one index
				wrongWords.add(new Pair<Integer, Integer>(beginIndex, justAfterLastIndex));				
			}
		} 
		return wrongWords; 
	}
	
	public double computePQ(String query) // the function expects that the query is in the dictionary (call IsInDictionary first)
	{ 		
		String[] words = query.trim().split("\\s+");
		
		double countw1 = unigram.count(words[0]);
		double log_pQ = Math.log(countw1/unigram.termCount()); // first factor p(w1) )
		double countw1w2; 
		double countw2; 
		// now get the bigrams
		for(int wordInd = 1; wordInd<words.length; wordInd++)
		{ 			
			// Also need smoothening, since the bigram may not be in the dictionary 
			countw1w2 = bigram.count(new Pair<String, String>(words[wordInd-1], words[wordInd])); 
			countw2 = unigram.count(words[wordInd]); 
			log_pQ += Math.log( BuildModels.LAMBDA * countw2/unigram.termCount() 
			    +   (1-BuildModels.LAMBDA) *countw1w2/countw1 ); // multiply by P(w2/w1) = count(w1,w2)/count(w1)	
			countw1 = countw2; 
		} 
		return log_pQ; 
	}
	
	// Do not call constructor directly since this is a Singleton
	private LanguageModel(String corpusFilePath) throws Exception {
		constructDictionaries(corpusFilePath);
	}


	public void constructDictionaries(String corpusFilePath)
			throws Exception {

		System.out.println("Constructing dictionaries...");
		File dir = new File(corpusFilePath);
		for (File file : dir.listFiles()) {
			if (".".equals(file.getName()) || "..".equals(file.getName())) {
				continue; // Ignore the self and parent aliases.
			}
			System.out.printf("Reading data file %s ...\n", file.getName());
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line = null;
			
			while ((line = input.readLine()) != null) {
				/*
				 * Your code here
				 */
				String[] tokens = line.trim().split("\\s+");
				String prevToken = tokens[0]; 
				String token = tokens[0]; 
				unigram.add(token);
				for(int tokenInd = 1; tokenInd < tokens.length; tokenInd++)
				{ 					
					token = tokens[tokenInd]; 
					unigram.add(token);					
					bigram.add(new Pair<String, String>(prevToken, token));
					prevToken = token;
				} 
				
			}
			input.close();
			
		}
		System.out.println("Done.");
	}
	
	// Loads the object (and all associated data) from disk
	public static LanguageModel load() throws Exception {
		try {
			if (lm_==null){
				FileInputStream fiA = new FileInputStream(Config.languageModelFile);
				ObjectInputStream oisA = new ObjectInputStream(fiA);
				lm_ = (LanguageModel) oisA.readObject();
			}
		} catch (Exception e){
			throw new Exception("Unable to load language model.  You may have not run build corrector");
		}
		return lm_;
	}
	
	// Saves the object (and all associated data) to disk
	public void save() throws Exception{
		FileOutputStream saveFile = new FileOutputStream(Config.languageModelFile);
		ObjectOutputStream save = new ObjectOutputStream(saveFile);
		save.writeObject(this);
		save.close();
	}
	
	// Creates a new lm object from a corpus
	public static LanguageModel create(String corpusFilePath) throws Exception {
		if(lm_ == null ){
			lm_ = new LanguageModel(corpusFilePath);
		}
		return lm_;
	}
}
