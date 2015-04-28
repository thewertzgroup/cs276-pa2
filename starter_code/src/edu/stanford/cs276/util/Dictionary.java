package edu.stanford.cs276.util;

import java.io.Serializable;
import java.util.HashMap;

public class Dictionary<A> implements Serializable {

	private int termCount;
	private HashMap<A, Integer> map;

	public int termCount() {
		return termCount;
	}

	public Dictionary() {
		termCount = 0;
		map = new HashMap<A, Integer>();
	}

	public void add(A term) {

		termCount++;
		if (map.containsKey(term)) {
			map.put(term, map.get(term) + 1);
		} else {
			map.put(term, 1);
		}
	}

	public int count(A term) {

		if (map.containsKey(term)) {
			return map.get(term);
		} else {
			return 0;
		}
	}
}
