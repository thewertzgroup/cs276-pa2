package edu.stanford.cs276;

import java.io.Serializable;
import java.util.Map.Entry;

public interface EditCostModel extends Serializable {

	public double editProbability(String original, String R, int distance);

	public double P_of_R_given_Q(String R, Entry<String, Integer> Q);
}
