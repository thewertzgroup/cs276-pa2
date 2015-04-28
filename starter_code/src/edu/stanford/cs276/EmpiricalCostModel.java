package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import edu.stanford.cs276.util.Dictionary;
import edu.stanford.cs276.util.Pair;

public class EmpiricalCostModel implements EditCostModel{
	Dictionary<Pair<Character,Character>> delete = new Dictionary<>();
	Dictionary<Pair<Character,Character>> insert = new Dictionary<>();
	Dictionary<Pair<Character,Character>> substi = new Dictionary<>();
	Dictionary<Pair<Character,Character>> transp = new Dictionary<>();
	Dictionary<Character> charUnigram = new Dictionary<>();
	Dictionary<Pair<Character,Character>> charBigram = new Dictionary<>();
	final int cardinalityAlphabet = CandidateGenerator.alphabet.length; 
	public EmpiricalCostModel(String editsFile) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(editsFile));
		System.out.println("Constructing edit distance map...");
		String line = null;		

		Character x, y; 
		while ((line = input.readLine()) != null) {
			 
			Scanner lineSc = new Scanner(line);
			lineSc.useDelimiter("\t");
			String noisy = lineSc.next();
			String clean = lineSc.next();
			System.out.println(noisy);
			System.out.println(clean);
			// Determine type of error and record probability
			/*
			 * Your code here
			 */
			// first count the character unigrams and bigrams in the clean text, since they 
			// will be needed later to compute the probabilities
			int charInd = 0; 
			Character myUnigram; 
			Character prevUnigram = '$';
			while(charInd < clean.length())
			{ 
				myUnigram = clean.charAt(charInd);  
				charUnigram.add(myUnigram); 
				charBigram.add(new Pair<Character, Character>(prevUnigram, myUnigram));
				prevUnigram = myUnigram; 
				charInd++; 
			}
			
			// same length: substitution or transporse
			if(!noisy.equals(clean) && noisy.length() == clean.length())// added noisy!=clean since some entries are actually the same: repeats by skyguy stanford edu	repeats by skyguy stanford edu
 
			{ 
				charInd = 0; 
				while(noisy.charAt(charInd)==clean.charAt(charInd))
				{ 
					charInd++; 
				}
							
				// just substitution
				if(charInd == clean.length()-1 || noisy.charAt(charInd+1) == noisy.charAt(charInd+1)) // substitution of last character, or another character in the middle
				{ 
					y = clean.charAt(charInd); 	
					x = noisy.charAt(charInd);
					substi.add(new Pair<Character, Character>(x,y));
				} 
				// transporse
				else 
				{ 
					x = clean.charAt(charInd); 	
					y = clean.charAt(charInd+1);
					transp.add(new Pair<Character, Character>(x,y));
				}					
			}
			// insertion
			else if(noisy.length() > clean.length())
			{ 
				charInd = 0; 
				while(charInd < clean.length() && noisy.charAt(charInd)==clean.charAt(charInd))
				{ 
					charInd++; 
				}
				// 
				if(charInd>0)
				{ 
					x = clean.charAt(charInd-1); 	
					y = noisy.charAt(charInd);
				} 
				else //insertion at beginning, use special character $ for x, to indicate insertion at beginning
				{
					x = '$'; 	
					y = noisy.charAt(charInd);					
				} 
				insert.add(new Pair<Character, Character>(x,y));								
			}
			// deletion
			else if(noisy.length() < clean.length())				
			{ 
				charInd = 0; 
				while(charInd<noisy.length() && noisy.charAt(charInd)==clean.charAt(charInd) )
				{ 
					charInd++; 
				}
			 
				if(charInd>0)
				{ 
					x = clean.charAt(charInd-1); 	
					y = clean.charAt(charInd);					
				} 
				else //deletion at beginning, use special character $ for x, to indicate insertion at beginning
				{
					x = '$'; 	
					y = clean.charAt(charInd);					
				} 
				delete.add(new Pair<Character, Character>(x,y));								
			}
		}

		input.close();
		
		System.out.println("Done.");
	}



	
	// You need to update this to calculate the proper empirical cost
	@Override
	public double editProbability(String original, String R, int distance) {
		return 0.5;
		/*
		 * Your code here
		 */
		// get the probabilities: including smoothening
				// substitution
				//for()		
	
		
	}
	
	// You need to update this to calculate the proper empirical cost
	@Override
	public double editProbability(String original, String R, ArrayList<Character> edits) {
			//return 0.5;
			/*
			 * Your code here
			 */		
			int distance = edits.size()/3;			
			double p=1;
			int editInd = 0; 
			double log_pRgivenQ = 0;  

			// get the probabilities: including smoothening
			Character x, y; 
			Pair<Character, Character> xyPair;  
			while(editInd<distance)
			{ 
				x = edits.get(editInd*3 + 1);
				y = edits.get(editInd*3 + 2);
				xyPair = new Pair<Character, Character>(x,y); 
				
				if(edits.get(editInd*3) == 'd' || edits.get(editInd*3) == 't')//delete or transporse
				{ 										
					p = ((double)(delete.count(xyPair ) + 1) )/(charBigram.count(xyPair) + cardinalityAlphabet); 
					 
				}
				else if(edits.get(editInd*3) == 'i')//insert
				{ 
					p = ((double)(insert.count(xyPair ) + 1) )/(charUnigram.count(x) + cardinalityAlphabet); 
				}
				else if(edits.get(editInd*3) == 's')//substitute
				{ 
					p = ((double)(substi.count(xyPair) + 1) )/(charUnigram.count(y) + cardinalityAlphabet); 
				}				
				
				log_pRgivenQ += Math.log(p);/// very important to work in log domain, otherwise you get underflow and everything is equal to zero
				editInd++; 
			}
			
			
			return log_pRgivenQ;
	
			
		}
}
