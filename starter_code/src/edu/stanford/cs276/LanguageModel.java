package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map.Entry;

import edu.stanford.cs276.util.Debug;
import edu.stanford.cs276.util.Dictionary;


public class LanguageModel implements Serializable 
{
	private static boolean debug = false;

	private static LanguageModel lm_;
	/* Feel free to add more members here.
	 * You need to implement more methods here as needed.
	 * 
	 * Your code here ...
	 */	
	Dictionary unigram = new Dictionary();
	Dictionary bigram = new Dictionary();
	
	// Do not call constructor directly since this is a Singleton
	private LanguageModel(String corpusFilePath) throws Exception 
	{
		constructDictionaries(corpusFilePath);
	}
	
	
	public boolean inDictionary(String Q)
	{
		String[] tokens = Q.trim().split("\\s+");
		for (String token : tokens)
		{
			if (0 == unigram.count(token)) return false;
		}
		return true;
	}
	
	
	public double P_of_Q(Entry<String, Integer> q)
	{
		String[] tokens = q.getKey().trim().split("\\s+");

		double P_of_Q = 0.0;
		
		double P_MLE_of_w1 = P_MLE_of_w1(tokens[0]);
		
		P_of_Q += P_MLE_of_w1 != 0.0 ? Math.log( P_MLE_of_w1 ) : P_MLE_of_w1;
		
		for (int i=1; i<tokens.length; i++)
		{
			double P_MLE_of_w2_given_w1 = P_MLE_of_w2_given_w1(tokens[i], tokens[i-1]);
			
			P_of_Q += P_MLE_of_w2_given_w1 != 0.0 ? Math.log( P_MLE_of_w2_given_w1 ) : P_MLE_of_w2_given_w1;
		}
		
		return P_of_Q;
	}
	
	
	/*
	 * P(w1)
	 */
	private double P_MLE_of_w1(String w1)
	{
		// P_MLE(w1) = count(w1) / T
		
		double result = unigram.count(w1) / unigram.termCount();
		
		if (debug)
		{
			System.out.println("P_MLE_of_w1:");
			System.out.println("\tw1: " + w1);
			System.out.println("\tcount: " + unigram.count(w1));
			System.out.println("\ttermCount: " + unigram.termCount());
			System.out.println("\tP_MLE(w1): " + result);
		}
		
		return result;
	}


	/*
	 * P_MLE(w2|w1)
	 */
	private double P_MLE_of_w2_given_w1(String w2, String w1)
	{
		// P_MLE(w2|w1) = P(w1,w2) / P(w1) = count(w1,w2) / count(w1)
		
		double bigram_count = bigram.count(w1 + " " + w2);
		double unigram_count = unigram.count(w1);
		
		double result = unigram_count != 0.0 ? bigram_count / unigram_count : 0.0; // TODO: Handle divide by zero
		
		if (debug)
		{
			System.out.println("P_MLE_of_w2_given_w1:");
			System.out.println("\tw1: " + w1);
			System.out.println("\tw2: " + w2);
			System.out.println("\tbigram count: " + bigram_count);
			System.out.println("\tunigram count: " + unigram_count);
			System.out.println("\tP_MLE(w2|w1): " + result);
		}
		
		return result;
	}
	
	
	/*
	 * Pint(w2|w1)
	 */
	private double P_int_of_w2_given_w1(String w2, String w1)
	{
		// P_int(w2|w1) = L*P_mle(w2) + (1-L)*P_mle(w2|w1)
		
		double P_MLE_of_w2 = P_MLE_of_w1(w2);
		double P_MLE_of_w2_given_w1 = P_MLE_of_w2_given_w1(w2, w1);
		
		double result = Parameters.lambda * P_MLE_of_w2 + (1 - Parameters.lambda) * P_MLE_of_w2_given_w1;
		
		if (debug)
		{
			System.out.println("P_int_of_w2_given_w1:");
			System.out.println("\tw1: " + w1);
			System.out.println("\tw2: " + w2);
			System.out.println("\tP_MLE_of_w2: " + P_MLE_of_w2);
			System.out.println("\tP_MLE_of_w2_given_w1: " + P_MLE_of_w2_given_w1);
			System.out.println("\tP_int(w2|w1): " + result);
		}
		
		return result;
	}
	
	
	public void constructDictionaries(String corpusFilePath) throws Exception 
	{

		Debug d = new Debug();
		d.setDev("dev");
		
		String separator = " ";

		System.out.println("Constructing dictionaries...");
		File dir = new File(corpusFilePath);
		for (File file : dir.listFiles()) 
		{
			if (".".equals(file.getName()) || "..".equals(file.getName())) 
			{
				continue; // Ignore the self and parent aliases.
			}
			System.out.printf("Reading data file %s ...\n", file.getName());
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = input.readLine()) != null) 
			{
				/*
				 * Your code here
				 */
				String[] tokens = line.trim().split("\\s+");
				for (int i=0; i<tokens.length-1; i++)
				{
					unigram.add(tokens[i]);
					bigram.add(tokens[i] + separator + tokens[i+1]);
				}
				unigram.add(tokens[tokens.length-1]);
			}
			input.close();
		}
		d.dev("Unigram term count: " + unigram.termCount());
		d.dev("Bigram term count: " + bigram.termCount());
		System.out.println("Done.");
	}
	
	// Loads the object (and all associated data) from disk
	public static LanguageModel load() throws Exception 
	{
		try 
		{
			if (lm_==null)
			{
				FileInputStream fiA = new FileInputStream(Config.languageModelFile);
				ObjectInputStream oisA = new ObjectInputStream(fiA);
				lm_ = (LanguageModel) oisA.readObject();
			}
		} 
		catch (Exception e)
		{
			throw new Exception("Unable to load language model.  You may have not run build corrector");
		}
		return lm_;
	}
	
	// Saves the object (and all associated data) to disk
	public void save() throws Exception
	{
		FileOutputStream saveFile = new FileOutputStream(Config.languageModelFile);
		ObjectOutputStream save = new ObjectOutputStream(saveFile);
		save.writeObject(this);
		save.close();
	}
	
	// Creates a new lm object from a corpus
	public static LanguageModel create(String corpusFilePath) throws Exception 
	{
		if(lm_ == null )
		{
			lm_ = new LanguageModel(corpusFilePath);
		}
		return lm_;
	}
}
