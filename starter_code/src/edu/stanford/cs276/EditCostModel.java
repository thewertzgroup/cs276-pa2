package edu.stanford.cs276;

import java.io.Serializable;
import java.util.ArrayList;

public interface EditCostModel extends Serializable {

	public double editProbability(String original, String R, int distance);
	public double editProbability(String original, String R, ArrayList<Character> edits);
}
